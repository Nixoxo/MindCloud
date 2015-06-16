package de.pm.mindcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by samuel on 16.06.15.
 */
@SpringBootApplication
@ComponentScan("de.pm.mindcloud")
public class MindCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindCloudApplication.class, args);
    }
}