package org.app.gtsconnected3.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import org.app.gtsconnected3.entity.Trip;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3FileServiceInt {
    List<String> listAllObjects(String s3BucketName);
    PutObjectResult uploadObject(String s3BucketName, String objectName, MultipartFile multipartFile);
    void uploadFileToS3Bucket(Trip trip, MultipartFile multipartFile);
    void deleteObject(String bucketName, String objectName);
    List<String> listAllObjectsByName(String s3BucketName);
}
