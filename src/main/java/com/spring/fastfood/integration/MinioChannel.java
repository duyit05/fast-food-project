package com.spring.fastfood.integration;


import com.spring.fastfood.exception.BusinessException;
import com.spring.fastfood.utils.ConverterUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioChannel {

    private final MinioClient minioClient;
    private static final String BUCKET = "resources";


    @PostConstruct
    private void init() {
        createBucket(BUCKET);
    }

    @SneakyThrows
    private void createBucket(String name) {
        // CHECK BUCKET EXIST OR NOT
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(name)
                        .build()

        );

        if (!found) {
            // CREATE BUCKET IF NOT EXIST
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(name)
                            .build()
            );
            // SET BUCKET IS PUBLIC TO SET POLICY
            String policy = """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                           {
                              "Effect": "Allow",
                              "Principal": "*",
                              "Action": "s3:GetObject",
                              "Resource": "arn:aws:s3:::%s/*"
                            }
                          ]
                        }
                    """.formatted(name);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(name).config(policy).build());
        } else {
            log.info("Bucket %s đã tồn tại.".formatted(name));
        }
    }

    public String update(final MultipartFile file) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        log.info("Bucket : {} , file size : {}", BUCKET, file.getSize());
        String fileName = file.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(file.getOriginalFilename())
                            .contentType(Objects.isNull(file.getContentType()) ? "img/png ; img/jpg" : file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
        } catch (Exception ex) {
            log.info("Error saving image \n {}", ex.getMessage());
            throw new BusinessException("400", "Unable to upload file", ex);
        }
        return minioClient.getPresignedObjectUrl(
                io.minio.GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(BUCKET)
                        .object(fileName)
                        .build()

        );
    }

    public byte[] download(String bucket, String name) {
        try {
            GetObjectResponse inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(name)
                    .build());
            String contentLength = inputStream.headers().get(HttpHeaders.CONTENT_LENGTH);
            int size = StringUtils.isEmpty(contentLength) ? 0 : Integer.parseInt(contentLength);
            return ConverterUtils.readBytesFromInputStream(inputStream, size);
        } catch (Exception ex) {
            throw new BusinessException("400", "Unable to upload file", ex);
        }
    }
}
