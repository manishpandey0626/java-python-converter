package com.manish.java2py.controller;

import com.manish.java2py.service.JavaToPythonConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/convert")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JavaToPythonController {

    private final JavaToPythonConversionService conversionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InputStreamResource> convert(@RequestParam("file") MultipartFile file) throws IOException {
        File tempJavaFile = File.createTempFile("uploaded", ".java");
        file.transferTo(tempJavaFile);

        String pythonChunks = conversionService.convertJavaFileToPython(tempJavaFile);
        File outputPythonFile = File.createTempFile("converted", ".py");

        Files.writeString(outputPythonFile.toPath(), pythonChunks);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(outputPythonFile));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.py")
                .contentType(MediaType.parseMediaType("text/x-python"))
                .body(resource);
    }
}

