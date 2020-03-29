package io.zipcoder.tc_spring_poll_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.annotation.WebServlet;

@SpringBootApplication
public class QuickPollApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickPollApplication.class, args);
    }


}