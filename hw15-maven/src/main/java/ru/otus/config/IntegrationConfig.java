package ru.otus.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.MessageChannel;
import ru.otus.domains.IndicationDto;
import ru.otus.services.*;
import ru.otus.services.exceptions.UnknownSingleJsonFormatException;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel handledErrorChannel() {
        return MessageChannels.publishSubscribe()
                .get();
    }


    @Bean
    public IntegrationFlow jsonFlow(
            JsonParserService jsonParserService,
            @Qualifier("vipIndicationFlow") IntegrationFlow vipIndicationFlow,
            @Qualifier("indicationFlow") IntegrationFlow indicationFlow) {
        return IntegrationFlow.from("jsonChannel")
                .handle(jsonParserService, "parse")
                .channel("parsersChannel")
                .<IndicationDto, Boolean>route(
                        IndicationDto::getVip,
                        mapping -> mapping
                                .subFlowMapping(
                                        false,
                                        flow -> flow.gateway(indicationFlow))
                                .subFlowMapping(
                                        true,
                                        flow -> flow.gateway(vipIndicationFlow))
                )
                .channel("outputChannel")
                .get();
    }

    @Bean
    public IntegrationFlow listJsonFlow(
            JsonListParserService jsonListParserService,
            @Qualifier("vipIndicationFlow") IntegrationFlow vipIndicationFlow,
            @Qualifier("indicationFlow") IntegrationFlow indicationFlow) {
        return IntegrationFlow.from("listJsonChannel")
                .handle(jsonListParserService, "parse")
                .channel("listParsersChannel")
                .split()
                .<IndicationDto, Boolean>route(
                        IndicationDto::getVip,
                        mapping -> mapping
                                .subFlowMapping(
                                        false,
                                        flow -> flow.gateway(indicationFlow))
                                .subFlowMapping(
                                        true,
                                        flow -> flow.gateway(vipIndicationFlow))
                )
                .aggregate()
                .transform(Transformers.objectToString())
                .channel("outputChannel")
                .get();
    }

    @Bean
    public IntegrationFlow errorFlow() {
        return IntegrationFlow.from("handledErrorChannel")
                .routeByException(
                        mapping -> mapping
                                .subFlowMapping(
                                        UnknownSingleJsonFormatException.class,
                                        flow -> flow
                                                .transform("payload.failedMessage.payload")
                                                .channel("listJsonChannel")
                                )
                )
                .get();
    }

    @Bean("vipIndicationFlow")
    public IntegrationFlow vipIndicationFlow(
            VipIndicationParserService vipIndicationParserService,
            VipIndicationConsumerService vipIndicationConsumerService) {
        return IntegrationFlow.from("vipDtoChannel")
                .handle(vipIndicationParserService, "parse")
                .handle(vipIndicationConsumerService, "consume")
                .channel("outputChannel")
                .get();
    }

    @Bean("indicationFlow")
    public IntegrationFlow indicationFlow(
            IndicationParserService indicationParserService,
            IndicationConsumerService indicationConsumerService) {
        return IntegrationFlow.from("dtoChannel")
                .handle(indicationParserService, "parse")
                .handle(indicationConsumerService, "consume")
                .channel("outputChannel")
                .get();
    }

}
