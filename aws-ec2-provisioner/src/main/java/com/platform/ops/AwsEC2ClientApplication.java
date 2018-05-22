package com.platform.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * Springboot application.
 *
 * @author Srinivasa Prasad Sunnapu
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = AwsEC2ClientConfiguration.class)
public class AwsEC2ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsEC2ClientApplication.class, args);
    }
}
