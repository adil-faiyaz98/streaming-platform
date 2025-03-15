package com.examples.streaming_platform.catalog.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping
public class FaviconController {

    @GetMapping("/favicon.ico")
    public ResponseEntity<byte[]> getFavicon() throws IOException {
        Resource resource = new ClassPathResource("static/favicon.ico");
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        return ResponseEntity.ok().body(fileContent);
    }
}
