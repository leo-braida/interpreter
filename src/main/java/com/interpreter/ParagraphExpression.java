package com.interpreter;

public class ParagraphExpression implements Expression {
    private final Expression inner;

    public ParagraphExpression(Expression inner) {
        this.inner = inner;
    }

    @Override
    public String interpret(Context context) {
        String content = (inner != null) ? inner.interpret(context) : "";
        return "<p>" + content + "</p>";
    }
}
