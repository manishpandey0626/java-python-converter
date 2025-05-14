package com.manish.java2py.service;

import com.manish.java2py.model.ClassContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaToPythonPromptBuilder {

    private JavaToPythonPromptBuilder() {
        // Private constructor to prevent instantiation
    }

    public static String buildPromptForMethod(ClassContext context, String methodCode) {

        String outerClassFields = "";
        if (context.outerClassFields() != null && !context.outerClassFields().isEmpty()) {
            outerClassFields = String.format("""
                    5. Use `self.outer.<field_name>` to access outer class fields if needed.
                                               - Outer class fields: `%s`
                    """, context.outerClassFields());
        }

        String prompt = String.format("""
                        You are a code translator. Convert the following Java method to idiomatic Python 3, keeping the context in mind.
                        Do NOT include the class declaration. Only return the body of the method as a standalone Python function. Do not redefine class, imports, or constructor.
                        
                        **Instructions:**
                        1. Only convert the Java method to a Python method. Do NOT include class declaration or any boilerplate.
                        2. **Use `self.`** to reference instance fields (e.g., `self.users` instead of `users`).
                        3. Follow **Python 3 syntax and conventions**.
                        4. The class **already has the following imports, fields, and constructor** (use them as context to correctly reference instance variables):
                        %s
                        - **Class name** `%s`.
                        - **Fields (defined at the class level):**
                          %s
                        
                        - **Constructor (if any):**
                          %s
                        
                        - **Imports:**
                          %s
                        
                        
                        - **Java method:**
                          %s
                        
                        Python code:
                        """,
                outerClassFields,
                context.className(),
                context.fields(),
                context.constructor().isEmpty() ? "None" : context.constructor(),
                context.imports(),
                methodCode
        );
        log.info("Prompt for method conversion: {}", prompt);
        return prompt;
    }

    public static String buildPromptForClass(ClassContext context) {
        String outerClassNote = "";
        if (context.outerClassName() != null && !context.outerClassName().isEmpty()) {
            outerClassNote = String.format("""
                    - This class is an inner class of `%s`.
                      Pass a reference to the outer class instance in the constructor (e.g., `def __init__(self, outer):`) 
                      and store it as `self.outer = outer` so inner methods can access outer class fields.
                      - Outer class fields: %s
                      - You may access outer fields via `self.outer.<field_name>` if needed.
                    """, context.outerClassName(), context.outerClassFields().isEmpty() ? "None" : context.outerClassFields());
        }
        String prompt = String.format("""
                         You are converting a partial Java class into clean, idiomatic Python 3 code.
                        
                         - This is not the full file—only class-level imports, fields, and constructor are provided.
                         - Use standard Python modules and types. For example:
                           - Use `[]` instead of `ArrayList`
                           - Use `logging.getLogger(__name__)` instead of `Logger.getLogger(...)`
                           - Use `python libraries` instead of `Java libraries in the import section`
                         - Wrap fields and constructor inside a proper Python `class` with an `__init__` method.
                         - If a class extends another class or implements interfaces, declare them in the Python class signature using multiple inheritance: e.g., `class Dog(Animal, Pet):`
                         - If no constructor is provided, generate an empty `__init__` method (with `pass`).
                         - Convert static fields into class-level variables.
                         - Ensure proper indentation and idiomatic naming.
                         %s
                         Here is the partial Java class:
                        
                         Java Imports: %s
                         Java Class Name: %s
                         Extended class Name: %s
                         Implemented interfaces: %s
                         Java Fields:
                         %s
                         Java Constructor: %s
                        
                        Convert this to Python:
                        """,
                outerClassNote,
                context.imports(),
                context.className(),
                context.extended(),
                context.implemented(),
                context.fields(),
                context.constructor().isEmpty() ? "None" : context.constructor()
        );

        log.info("Prompt for class conversion: {}", prompt);
        return prompt;
    }
}
