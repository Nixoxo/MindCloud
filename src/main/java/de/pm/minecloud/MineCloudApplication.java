package de.pm.minecloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by samuel on 16.06.15.
 */
@SpringBootApplication
@ComponentScan("de.pm.minecloud")
public class MineCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(MineCloudApplication.class, args);
    }
}