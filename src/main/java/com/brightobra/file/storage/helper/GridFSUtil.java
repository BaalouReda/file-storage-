package com.brightobra.file.storage.helper;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GridFSUtil {

    @Value("${spring.data.mongodb.database}")
    private static String databaseName;


    public static GridFSBucket createGridFSBucket(MongoClient mongoClient,String bucketName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return GridFSBuckets.create(database, bucketName);
    }

}
