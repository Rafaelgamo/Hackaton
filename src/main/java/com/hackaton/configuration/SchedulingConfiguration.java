package com.hackaton.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    Logger logger = LoggerFactory.getLogger(SchedulingConfiguration.class);

    @PostConstruct
    public void init(){
        logger.info("Spring Job Scheduling enabled");
    }
}
