package org.example.jellyin;

import lombok.extern.slf4j.Slf4j;
import org.example.jellyin.metric.Information;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class JellyinApplication {

  public static void main(String[] args) {
    SpringApplication.run(JellyinApplication.class, args);
  }


//  @Scheduled(fixedDelay = 1,timeUnit = TimeUnit.SECONDS)
  public void printMemoryInformation(){
    log.info(Information.getMemoryInformation(0));
  }}
