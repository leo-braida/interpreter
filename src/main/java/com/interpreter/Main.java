package com.interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path mdPath = Paths.get("example.md");
        if (!Files.exists(mdPath)) {
            System.err.println("File example.md not found in the root directory!");
            return;
        }

        try {
            System.out.println("=== Reading expressions from example.md ===");
            String markdownContent = Files.readString(mdPath);
            System.out.println(markdownContent);

            System.out.println("\n=== Parsing and Interpreting ===");
            MarkdownParser parser = new MarkdownParser();
            Expression ast = parser.parse(markdownContent);

            Context context = new Context();
            String htmlOutput = ast.interpret(context);

            System.out.println("=== HTML Output ===");
            System.out.println(htmlOutput);

        } catch (IOException e) {
            System.err.println("Error reading example.md: " + e.getMessage());
        }
    }
}
