package org.example.jellyin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestJellyinApplication {

  public static void main(String[] args) {
    SpringApplication.from(JellyinApplication::main).with(TestJellyinApplication.class).run(args);
  }

}
