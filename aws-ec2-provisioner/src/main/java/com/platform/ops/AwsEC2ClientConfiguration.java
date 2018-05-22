package com.platform.ops;

import com.platform.ops.controller.HostProvisionRequestController;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * Spring configuraiton.
 *
 * @author Srinivasa Prasad Sunnapu
 */
@EnableWebMvc
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = HostProvisionRequestController.class)
public class AwsEC2ClientConfiguration {
}
