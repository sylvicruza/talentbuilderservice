package com.talentbuilder.talentbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@EnableAutoConfiguration(exclude = { FreeMarkerAutoConfiguration.class })
@SpringBootApplication
public class TalentbuilderApplication {

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {

        String username = (String) event.getAuthentication().getPrincipal();
        System.out.println("Log: " + username);
    }

    public static void main(String[] args) {
        SpringApplication.run(TalentbuilderApplication.class, args);
    }

}
