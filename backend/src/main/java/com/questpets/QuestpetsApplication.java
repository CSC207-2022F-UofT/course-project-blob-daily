package com.questpets;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class QuestpetsApplication {
    public static void main(String[] args){
        org.springframework.boot.SpringApplication.run(QuestpetsApplication.class, args);
    }
}
