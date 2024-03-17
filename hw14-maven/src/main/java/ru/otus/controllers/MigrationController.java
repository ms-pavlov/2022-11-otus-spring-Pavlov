package ru.otus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.PrintStream;
import java.util.Date;

@ShellComponent
@AllArgsConstructor
public class MigrationController {

    private final PrintStream out;
    private final Job bookMigrationJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Postgre to Mongo migration", key = {"migrate", "migrate-all"})
    public void migrate() throws Exception {
        JobExecution execution = jobLauncher.run(bookMigrationJob, new JobParametersBuilder()
                .addString("date", new Date().toString())
                .toJobParameters());
        out.println(execution);
    }
}
