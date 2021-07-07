package net.alenkin.alenkindrive.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Slf4j
@Component
public class AmazonUtils {

    private String authKey;

    private String secretKey;

    private String bucket;

    private AmazonS3 s3client;

    private AWSCredentials credentials;

    public AmazonUtils(@Value("${first}") String authKey,
                       @Value("${second") String secretKey,
                       @Value("${bucket}") String bucket) {
        this.authKey = authKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        credentials = new BasicAWSCredentials(authKey, secretKey);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_1)
                .build();
    }

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3client.putObject(new PutObjectRequest(bucket, fileName, fileObj));
        fileObj.delete();
        return s3client.getUrl(bucket, fileName).toString();
    }

    public String deleteFile(String fileUrl) {
        AmazonS3URI uri = new AmazonS3URI(fileUrl);
        s3client.deleteObject(uri.getBucket(), uri.getKey());
        return uri + " removed ...";
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

}
