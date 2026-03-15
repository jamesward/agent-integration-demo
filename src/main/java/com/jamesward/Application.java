package com.jamesward;

import com.jamesward.internal.MyLoggingAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    @Bean("loggingAdvisors")
    @Profile("debug")
    List<Advisor> debugLoggingAdvisor() {
        return List.of(
                ToolCallAdvisor.builder()
                        .conversationHistoryEnabled(true)
                        .build(),
                new MyLoggingAdvisor()
        );
    }

    @Bean("loggingAdvisors")
    @Profile("!debug")
    List<Advisor> noopLoggingAdvisor() {
        return new ArrayList<>();
    }

    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
