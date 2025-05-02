package com.manish.java2py.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.manish.java2py.model.ClassContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JavaToPythonConversionService {


    private final PythonClassConverter pythonClassConverter;


    public String convertJavaFileToPython(File javaFile) throws IOException {
        String fullCode = Files.readString(javaFile.toPath());

        JavaParser javaParser = new JavaParser();
        CompilationUnit cu = javaParser.parse(fullCode).getResult().orElseThrow();

        ClassOrInterfaceDeclaration clazz = cu.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow();
        //Get java Class Context
        ClassContext classContext = JavaClassExtractor.extract(clazz, cu);

        StringBuilder pythonCode = new StringBuilder();
        String generatedPythonCode = pythonClassConverter.processClassContextRecursively(classContext, 0, pythonCode);
        pythonCode.setLength(0); // Clear the StringBuilder
        return generatedPythonCode;

    }


}