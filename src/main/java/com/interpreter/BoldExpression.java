package com.interpreter;

public class BoldExpression implements Expression {
    private final Expression inner;

    public BoldExpression(Expression inner) {
        this.inner = inner;
    }

    @Override
    public String interpret(Context context) {
        if (inner == null) {
            return "<strong></strong>";
        }
        return "<strong>" + inner.interpret(context) + "</strong>";
    }
}
