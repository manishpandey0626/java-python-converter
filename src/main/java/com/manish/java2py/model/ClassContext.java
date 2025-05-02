package com.manish.java2py.model;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

public record ClassContext(
        String className,
        String fields,
        String constructor,
        String imports,
        String extended,
        String implemented,
        String outerClassName,
        String outerClassFields,
        List<MethodDeclaration> methods,
        List<ClassContext> innerClasses
) {
}
