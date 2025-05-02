# Java2Python Converter ☕➞🐍

A Gen AI-powered service that converts partial Java classes into clean, idiomatic Python 3 code using GitHub Copilot and
OpenAI models.

---

## 📚 Overview

This tool automates the conversion of Java class structures into Python 3 syntax, making it easier to modernize legacy
Java codebases. It parses Java classes using Abstract Syntax Tree (AST) traversal and converts relevant components such
as class-level fields, constructors, inner classes, and methods.

---

## 🔹 Key Features

- ✅ Converts Java class skeletons (fields, constructors, and inner classes)
- ✅ Handles nested inner classes recursively with proper outer references
- ✅ Supports class inheritance and interface translation to Python multiple inheritance
- ✅ Preserves structure and naming in idiomatic Python style
- ✅ Leverages both GitHub Copilot and OpenAI chat models for prompt-based code generation
- ✅ Exposed via REST API for easy integration and testing

---

## 👨‍💻 Technologies Used

| Technology      | Purpose                                                         |
|-----------------|-----------------------------------------------------------------|
| Java 21         | Core application codebase                                       |
| Spring Boot 3.3 | REST API service                                                |
| JavaParser      | To parse Java source code into AST for analysis                 |
| OpenAI Chat API | Prompt-based code generation                                    |
| GitHub Copilot  | Alternative AI source for generating Python code                |
| Lombok          | Boilerplate code reduction (@Service, @RequiredArgsConstructor) |

---

---

## 🧠 Why JavaParser?

`JavaParser` is used to extract specific components of a Java class—like methods, fields, constructors, and inner
classes.\
Instead of sending the entire class to the LLM, only individual parts are sent. This helps avoid token limit issues for
large files and ensures more accurate and modular translations.

---

## 📉 Key Classes to Explore

| Package         | Class Name                      | Description                                                             |
|-----------------|---------------------------------|-------------------------------------------------------------------------|
| `controller`    | `ConvertController`             | Exposes the REST endpoint `/api/convert` for receiving Java class input |
| `service`       | `PythonClassConverter`          | Main service class handling Java to Python conversion logic recursively |
| `service`       | `GithubAiService`               | Interacts with the OpenAI chat model for code generation                |
| `service`       | `JavaToPythonConversionService` | Service coordinating the overall class/method conversion flow           |
| `promptbuilder` | `JavaToPythonPromptBuilder`     | Builds structured prompts for OpenAI API for class/method conversion    |
| `model`         | `ClassContext`                  | DTO holding parsed Java class components (fields, methods, etc.)        |
| `service`       | `JavaClassExtractor`            | Uses JavaParser to extract components of the Java class                 |

---

## ⚙️ How to Run Locally

1. **Clone the Repository**

   ```bash
   git clone https://github.com/manishpandey0626/java-python-converter.git
   cd java2python-converter
   ```

2. **Set Your OpenAI API Key**

   Add the following to `application.properties` or as an environment variable:

   ```properties
   openai.api.key=your-openai-api-key
   ```

3. **Start the Application**

   ```bash
   ./mvnw spring-boot:run
   ```

4. **Use the REST API**
   Send a `POST` request to:

   ```
   http://localhost:8080/api/convert
   ```

   with a request body containing your Java class content.\
   You’ll receive Python-converted code in the response.

## 🔮 Future Scope

- Support conversion of entire Java projects (multiple files/packages)
- Generate unit tests for the translated Python code
- Integrate a UI for drag-and-drop Java file uploads
- Enable downloadable output as `.py` files or ZIP packages

---

## ✅ Highlights

- First Gen AI-based tool combining static analysis + LLM
- REST API–based design, modular and extensible
- Useful for learning Java-to-Python translations and accelerating migrations

---

## 📁 Repository

GitHub: https://github.com/manishpandey0626/java-python-converter

