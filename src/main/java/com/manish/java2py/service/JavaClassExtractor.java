package com.manish.java2py.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.manish.java2py.model.ClassContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaClassExtractor {

    private JavaClassExtractor() {
        // Private constructor to prevent instantiation
    }

    public static ClassContext extract(ClassOrInterfaceDeclaration clazz, CompilationUnit cu) {
        String className = clazz.getNameAsString();

        // Extract methods
        List<MethodDeclaration> methods = clazz.getMethods();

        // Extract fields
        String fields = clazz.getFields().stream()
                .map(FieldDeclaration::toString)
                .collect(Collectors.joining("\n"));
        // Extract constructor
        String constructor = clazz.getConstructors().stream()
                .map(ConstructorDeclaration::toString)
                .collect(Collectors.joining("\n"));
        // Extract imports
        String imports = cu.getImports().stream()
                .map(ImportDeclaration::getNameAsString)
                .collect(Collectors.joining(", "));

        // Extract extended types
        String extended = clazz.getExtendedTypes().stream()
                .map(ClassOrInterfaceType::getNameAsString)
                .collect(Collectors.joining(", "));

        // Extract implemented types
        String implemented = clazz.getImplementedTypes().stream()
                .map(ClassOrInterfaceType::getNameAsString)
                .collect(Collectors.joining(", "));

        // Extract Outer class name and fields
        Optional<Node> parent = clazz.getParentNode();
        String outerClassName = null;
        String outerClassFields = null;
        if (parent.isPresent() && parent.get() instanceof ClassOrInterfaceDeclaration outerClass) {
            outerClassName = outerClass.getNameAsString();
            outerClassFields = outerClass.getFields().stream()
                    .map(FieldDeclaration::toString)
                    .collect(Collectors.joining("\n"));
        }


        // Recursively process inner classes
        List<ClassContext> innerClassContexts = clazz.getMembers().stream()
                .filter(ClassOrInterfaceDeclaration.class::isInstance)
                .map(ClassOrInterfaceDeclaration.class::cast)
                .map(innerClass -> extract(innerClass, cu)) // if extract is static
                .toList();


        return new ClassContext(className, fields, constructor, imports, extended, implemented, outerClassName, outerClassFields, methods, innerClassContexts);
    }
}
