package com.interpreter;

public class ItalicExpression implements Expression {
    private final Expression inner;

    public ItalicExpression(Expression inner) {
        this.inner = inner;
    }

    @Override
    public String interpret(Context context) {
        if (inner == null) {
            return "<em></em>";
        }
        return "<em>" + inner.interpret(context) + "</em>";
    }
}
