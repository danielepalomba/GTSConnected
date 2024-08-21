package org.app.gtsconnected3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
@Slf4j
public class S3FileService implements S3FileServiceInt {

    private String s3BucketName = System.getenv("AWS_BUCKET_NAME");

    private AmazonS3 amazonS3;

    private TripService tripService;

    @Autowired
    public S3FileService(AmazonS3 amazonS3, TripService tripService) {
        this.amazonS3 = amazonS3;
        this.tripService = tripService;
    }

    @Override
    public List<String> listAllObjects(String s3BucketName) {
        log.info("ACCOUNT OWNER" + amazonS3.getS3AccountOwner().toString());
        List<String> listOfObjects = new ArrayList<>();

        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", s3BucketName, System.getenv("AWS_REGION"), objectSummary.getKey());
            listOfObjects.add(fileUrl);
        }

        return listOfObjects;
    }
    @Override
    public List<String> listAllObjectsByName(String s3BucketName) {
        log.info("ACCOUNT OWNER" + amazonS3.getS3AccountOwner().toString());
        List<String> listOfObjects = new ArrayList<>();

        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            listOfObjects.add(objectSummary.getKey());
        }

        return listOfObjects;
    }

    @Override
    public PutObjectResult uploadObject(String s3BucketName, String objectName, MultipartFile multipartFile) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            return amazonS3.putObject(new PutObjectRequest(s3BucketName, objectName, multipartFile.getInputStream(), objectMetadata));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public void uploadFileToS3Bucket(Trip trip, MultipartFile multipartFile) {
        String objectName = multipartFile.getOriginalFilename();
        PutObjectResult result = uploadObject(s3BucketName, objectName, multipartFile);

        if (result != null) {
            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", s3BucketName, System.getenv("AWS_REGION"), objectName);
            trip.setImageUrl(fileUrl);
        }
    }

    @Override
    public void deleteObject(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }
}

