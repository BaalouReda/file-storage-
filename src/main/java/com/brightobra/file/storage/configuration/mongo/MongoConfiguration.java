package com.brightobra.file.storage.configuration.mongo;


import com.brightobra.file.storage.helper.GridFSUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private  String databaseName;

    @Value("${spring.data.mongodb.gridfs.bucket}")
    private  String bucketName;


    @Bean
    public  GridFSBucket GridFSBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return GridFSBuckets.create(database, bucketName);
    }
}
