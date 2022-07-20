package com.market.infrastructure.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;


@Profile("test")
@Configuration
public class FakeMongoConfiguration {
    private static final String DATABASE_NAME = "TEST";

    @Bean(destroyMethod = "close")
    public MongoClient mongoClient() {
        var server = new MongoServer(new MemoryBackend());

        // bind on a random local port
        var serverAddress = server.bind();

        return MongoClients.create("mongodb:/" + serverAddress.toString());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), DATABASE_NAME);
    }
}
