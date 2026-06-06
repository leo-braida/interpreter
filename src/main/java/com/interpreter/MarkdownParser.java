package com.interpreter;

import java.util.ArrayList;
import java.util.List;

public class MarkdownParser {

    public Expression parse(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return new SequenceExpression(new ArrayList<>());
        }

        List<Expression> blocks = new ArrayList<>();
        String[] lines = markdown.split("\\r?\\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
              }

              int hashCount = 0;
              while (hashCount < line.length() && line.charAt(hashCount) == '#') {
                  hashCount++;
              }

              if (hashCount >= 1 && hashCount <= 6 && hashCount < line.length() && line.charAt(hashCount) == ' ') {
                  String content = line.substring(hashCount + 1);
                  blocks.add(new HeadingExpression(hashCount, parseInline(content)));
              } else {
                  blocks.add(new ParagraphExpression(parseInline(line)));
              }
          }

          return new SequenceExpression(blocks);
      }

      public Expression parseInline(String text) {
          if (text == null || text.isEmpty()) {
              return new TextExpression("");
          }

          for (int i = 0; i < text.length(); i++) {
              if (text.charAt(i) == '*') {
                  boolean isBold = (i < text.length() - 1 && text.charAt(i + 1) == '*');
                  if (isBold) {
                      int closingIdx = findClosingBold(text, i + 2);
                      if (closingIdx != -1) {
                          Expression before = parseInline(text.substring(0, i));
                          Expression inside = parseInline(text.substring(i + 2, closingIdx));
                          Expression after = parseInline(text.substring(closingIdx + 2));
                          return combine(before, new BoldExpression(inside), after);
                      }
                      i++;
                  } else {
                      int closingIdx = findClosingItalic(text, i + 1);
                      if (closingIdx != -1) {
                          Expression before = parseInline(text.substring(0, i));
                          Expression inside = parseInline(text.substring(i + 1, closingIdx));
                          Expression after = parseInline(text.substring(closingIdx + 1));
                          return combine(before, new ItalicExpression(inside), after);
                      }
                  }
              }
          }

          return new TextExpression(text);
      }

      private int findClosingBold(String text, int start) {
          int i = start;
          while (i < text.length() - 1) {
              if (text.charAt(i) == '*' && text.charAt(i + 1) == '*') {
                  return i;
              }
              i++;
          }
          return -1;
      }

      private int findClosingItalic(String text, int start) {
          int i = start;
          while (i < text.length()) {
              if (text.charAt(i) == '*') {
                  boolean prevIsAsterisk = (i > 0 && text.charAt(i - 1) == '*');
                  boolean nextIsAsterisk = (i < text.length() - 1 && text.charAt(i + 1) == '*');
                  if (!prevIsAsterisk && !nextIsAsterisk) {
                      return i;
                  }
                  if (nextIsAsterisk) {
                      i++;
                  }
              }
              i++;
          }
          return -1;
      }

      private Expression combine(Expression before, Expression middle, Expression after) {
          List<Expression> list = new ArrayList<>();
          addIfNotEmpty(list, before);
          list.add(middle);
          addIfNotEmpty(list, after);
          if (list.size() == 1) {
              return list.get(0);
          }
          return new SequenceExpression(list);
      }

      private void addIfNotEmpty(List<Expression> list, Expression expr) {
          if (expr instanceof TextExpression) {
              TextExpression te = (TextExpression) expr;
              if (te.getText() == null || te.getText().isEmpty()) {
                  return;
              }
          }
          list.add(expr);
      }
  }
