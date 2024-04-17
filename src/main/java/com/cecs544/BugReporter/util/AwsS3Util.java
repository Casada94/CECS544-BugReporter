package com.cecs544.BugReporter.util;

import com.cecs544.BugReporter.crypto.CryptoUtil;
import com.cecs544.BugReporter.exceptions.CryptoException;
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsS3Util {
    @Autowired
    private CryptoUtil cryptoUtil;
    @Value("${spring.aws.s3.bucketName}")
    private String bucketName;
    @Value("${spring.aws.regionName}")
    private Region regionName;
    @Value("${spring.aws.s3.localUri}")
    private String localS3Uri;
    @Value("${spring.aws.s3.accessKey}")
    private String accessKey;
    @Value("${spring.aws.s3.secretKey}")
    private String secretKey;
    @Value("${spring.workingDir}")
    private String workingDir;

    private S3Client s3Client;

    @PostConstruct
    public void init() throws CryptoException {
        accessKey = cryptoUtil.decrypt(accessKey);
        secretKey = cryptoUtil.decrypt(secretKey);

        s3Client = S3Client.builder()
                .region(regionName)
                .endpointOverride(URI.create("http://localhost:9000"))
                .forcePathStyle(true)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public void upload(MultiFileBuffer buffer, Integer key) {
        try {
            for (String file : buffer.getFiles()) {
                PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key.toString() + "/" + file)
                        .build();

                s3Client.putObject(objectRequest, buffer.getFileData(file).getFile().toPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getFileList(Integer key) {
        return s3Client.listObjects(b -> b.bucket(bucketName).prefix(key.toString() + "/"))
                .contents()
                .stream()
                .map(s3Object -> s3Object.key().split("/")[1])
                .collect(Collectors.toList());
    }

    public File getFile(String key, String fileName) {
        File file;
        if ((file = new File(workingDir + fileName)).exists()) {
            return file;
        } else {
            Path path = Paths.get(workingDir + fileName);
            s3Client.getObject(b -> b.bucket(bucketName).key(key + "/" + fileName), path);
            return path.toFile();
        }
    }

    public void deleteAllFiles(String key, List<String> fileNames) {
        for (String fileName : fileNames)
            s3Client.deleteObject(b -> b.bucket(bucketName).key(key + "/" + fileName));
    }

}
