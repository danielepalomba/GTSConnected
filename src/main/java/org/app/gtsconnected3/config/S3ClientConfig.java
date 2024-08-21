package org.app.gtsconnected3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class S3ClientConfig {

    private final String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
    private final String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
    private final String region = System.getenv("AWS_REGION");
    private final String bucketName = System.getenv("AWS_BUCKET_NAME");

    @Bean
    public AmazonS3 getAwsS3Client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(region)
                .build();
    }
}
