package ru.otus.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.model.MapModel;
import ru.otus.postgre.entities.Author;
import ru.otus.postgre.entities.Book;
import ru.otus.postgre.entities.Comment;
import ru.otus.postgre.entities.Genre;
import ru.otus.processors.AuthorProcessor;
import ru.otus.processors.BookProcessor;
import ru.otus.processors.CommentProcessor;
import ru.otus.processors.GenreProcessor;
import ru.otus.readers.BookReader;
import ru.otus.readers.CommentReader;
import ru.otus.writers.BookMongoWriter;
import ru.otus.writers.CommentMongoWriter;

@Configuration
public class JobConfig {

    private final Logger logger = LoggerFactory.getLogger("Batch Log");

    public static final String BOOK_MIGRATION_JOB = "bookMigrationJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public JobConfig(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public ItemReader<Author> authorItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("AuthorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT a FROM Author a")
                .build();
    }

    @Bean
    public ItemWriter<ru.otus.mongo.entities.Author> authorItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<ru.otus.mongo.entities.Author>()
                .collection("author")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step authorMigrationStep(
            ItemReader<Author> authorItemReader,
            ItemWriter<ru.otus.mongo.entities.Author> authorItemWriter,
            AuthorProcessor authorProcessor,
            JobRepository jobRepository,
            @Value("${migration.chunk-size.author}") int chunkSize) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Author, ru.otus.mongo.entities.Author>chunk(chunkSize, platformTransactionManager)
                .reader(authorItemReader)
                .processor(authorProcessor)
                .writer(authorItemWriter)
                .build();
    }

    @Bean
    public ItemReader<Genre> genreItemReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("GenreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT g FROM Genre g")
                .build();
    }

    @Bean
    public ItemWriter<ru.otus.mongo.entities.Genre> genreItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<ru.otus.mongo.entities.Genre>()
                .collection("genre")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step genreMigrationStep(
            ItemReader<Genre> genreItemReader,
            ItemWriter<ru.otus.mongo.entities.Genre> genreItemWriter,
            GenreProcessor genreProcessor,
            JobRepository jobRepository,
            @Value("${migration.chunk-size.author}") int chunkSize) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Genre, ru.otus.mongo.entities.Genre>chunk(chunkSize, platformTransactionManager)
                .reader(genreItemReader)
                .processor(genreProcessor)
                .writer(genreItemWriter)
                .build();
    }

    @Bean
    public Flow splitFlow(
            Step authorMigrationStep,
            Step genreMigrationStep) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(
                        new FlowBuilder<SimpleFlow>("flow1")
                                .start(authorMigrationStep)
                                .build(),
                        new FlowBuilder<SimpleFlow>("flow2")
                                .start(genreMigrationStep)
                                .build())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public Step bookMigrationStep(
            BookReader reader,
            BookMongoWriter writer,
            BookProcessor processor,
            JobRepository jobRepository,
            @Value("${migration.chunk-size.book}") int chunkSize
    ) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<Book, MapModel<ru.otus.mongo.entities.Book, Long>>chunk(chunkSize, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step commentMigrationStep(
            CommentReader reader,
            CommentMongoWriter writer,
            CommentProcessor processor,
            JobRepository jobRepository,
            @Value("${migration.chunk-size.comment}") int chunkSize
    ) {
        return new StepBuilder("commentMigrationStep", jobRepository)
                .<Comment, ru.otus.mongo.entities.Comment>chunk(chunkSize, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job bookMigrationJob(
            Flow splitFlow,
            Step bookMigrationStep,
            Step commentMigrationStep) {
        return new JobBuilder(BOOK_MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .next(bookMigrationStep)
                .next(commentMigrationStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }
}
