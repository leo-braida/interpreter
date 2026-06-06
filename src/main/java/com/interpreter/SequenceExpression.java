package com.interpreter;

import java.util.ArrayList;
import java.util.List;

public class SequenceExpression implements Expression {
    private final List<Expression> expressions;

    public SequenceExpression(List<Expression> expressions) {
        this.expressions = (expressions != null) ? new ArrayList<>(expressions) : new ArrayList<>();
    }

    @Override
    public String interpret(Context context) {
        StringBuilder sb = new StringBuilder();
        for (Expression expr : expressions) {
            sb.append(expr.interpret(context));
        }
        return sb.toString();
    }
}
