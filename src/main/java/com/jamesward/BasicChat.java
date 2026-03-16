package com.jamesward;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasicChat {

    @Bean
    @Profile("default")
    CommandLineRunner runDefault(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).build();

            var resp = chatClient.prompt().user("say hello").call().content();
            IO.println(resp);
        };
    }

    @Bean
    @Profile("structuredOutput")
    CommandLineRunner runStructuredOutput(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {

        record City(
                String name,
                Integer population,
                @JsonPropertyDescription("Most popular food")
                String food) {}

        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).build();

            var cities = chatClient.prompt().user("Get 10 big cities").call().entity(new ParameterizedTypeReference<List<City>>() {});

            cities.forEach(city ->
                IO.println(city.name() + " has " + city.population() + " people and likes " + city.food())
            );
        };
    }

    @Bean
    @Profile("streaming")
    CommandLineRunner runStreaming(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).build();
            chatClient.prompt().user("write a short story about the java programming language").stream().content()
                    .doOnNext(IO::print)
                    .blockLast();
        };
    }

    @Bean
    @Profile("systemPrompt")
    CommandLineRunner runSystemPrompt(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).defaultSystem("You are a wookie").build();
            var resp = chatClient.prompt().user("say hello").call().content();
            IO.println(resp);
        };
    }

    @Bean
    @Profile("noTools")
    CommandLineRunner runNoTools(ChatClient.Builder builder, @Qualifier("loggingAdvisors") List<Advisor> loggingAdvisors) {
        return _ -> {
            var chatClient = builder.defaultAdvisors(loggingAdvisors).build();

            var resp = chatClient.prompt().user("what time is it?").call().content();

            IO.println(resp);
        };
    }
}
