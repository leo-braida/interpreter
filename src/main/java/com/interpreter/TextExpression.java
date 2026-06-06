package com.interpreter;

public class TextExpression implements Expression {
    private final String text;

    public TextExpression(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String interpret(Context context) {
        if (text == null) {
            return "";
        }
        return escapeHtml(text);
    }

    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }
}
