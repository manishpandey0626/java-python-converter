package com.manish.java2py.service;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.manish.java2py.model.ClassContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PythonClassConverter {

    private final GithubAiService githubAiService;
    private final OpenAiService openAiService;

    public String processClassContextRecursively(ClassContext classContext, int indentLevel, StringBuilder pythonCode) {
        // 1. Generate prompt and get Python code for the class body
        String classPrompt = JavaToPythonPromptBuilder.buildPromptForClass(classContext);
        String classCode = generatePythonCode(classPrompt);
        pythonCode.append(indent(classCode, indentLevel)).append("\n\n");

        // 2. Process methods
        for (MethodDeclaration method : classContext.methods()) {
            String methodPrompt = JavaToPythonPromptBuilder.buildPromptForMethod(classContext, method.toString());
            String methodCode = generatePythonCode(methodPrompt);
            pythonCode.append(indent(methodCode, indentLevel + 1)).append("\n\n");
        }

        // 3. Recursively process inner classes
        for (ClassContext innerClass : classContext.innerClasses()) {
            processClassContextRecursively(innerClass, indentLevel + 1, pythonCode); // fills both maps recursively
        }


        return pythonCode.toString();
    }

    private String generatePythonCode(String prompt) {

        // uncomment this line to use OpenAI API
        //return fetchCodeBlock(openAiService.getPythonCode(prompt));

        return fetchCodeBlock(githubAiService.chat(prompt).block());
    }

    private String fetchCodeBlock(String pythonCode) {
        Pattern pattern = Pattern.compile("```python(.*?)```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pythonCode);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        // If no code block is found, return the original string
        return pythonCode;
    }

    private String indent(String code, int level) {
        String indent = "    ".repeat(level);
        return Arrays.stream(code.split("\n"))
                .map(line -> indent + line)
                .collect(Collectors.joining("\n"));
    }
}

