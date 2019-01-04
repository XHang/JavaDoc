package com.generatedoc;

import com.generatedoc.config.ApplicationConfig;
import com.generatedoc.main.DocGenerateStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class Application implements CommandLineRunner {
     public static final Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String [] args){
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private DocGenerateStarter generateStarter;
    @Override
    public void run(String... args) {
        log.info("开始生成接口文档");
        generateStarter.run();
        log.info("接口文档生成结束");
    }
}
