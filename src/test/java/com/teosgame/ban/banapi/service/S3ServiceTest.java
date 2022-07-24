package com.teosgame.ban.banapi.service;

import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.HttpMethod;
import com.amazonaws.util.IOUtils;

@SpringBootTest
public class S3ServiceTest {

    @Autowired
    S3Service service;

    @Autowired
    RestTemplate restTemplate;

    private final String FILE_NAME = "file.png";

    @BeforeEach
    public void create_Object() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            InputStream in = getClass().getResourceAsStream("../resources/teosRequest.png");

            HttpEntity<byte[]> entity = new HttpEntity<>(IOUtils.toByteArray(in), headers);
            URL url = service.generatePreSignedUrl(FILE_NAME, HttpMethod.PUT);
            System.out.printf("\n\n\nURL beofre req %s\n\n\n", url.toString());
            restTemplate.put(url.toURI(), entity);
        } catch (Exception e) {
            System.out.printf("Error Creating Object for S3: %s", e.getMessage());
        }
    }

    @AfterEach
    public void delete_Object() {
        try {
            URL url = service.generatePreSignedUrl(FILE_NAME, HttpMethod.DELETE);
            restTemplate.delete(url.toURI());
        } catch (Exception e) {
            System.out.printf("Error Deleting Object from S3: %s", e.getMessage());
        }
    }

    @Test
    public void test_GeneratePresignedUrl_Get() throws Exception {
        URL url = service.generatePreSignedUrl(FILE_NAME, HttpMethod.GET);
        restTemplate.getForEntity(url.toURI(), Void.class);
    }

    @Test
    public void test_GeneratePresignedUrl_PUT() {
        create_Object();
    }

    @Test
    public void test_GeneratePresignedUrl_DELETE() {
        delete_Object();
    }
    
}
