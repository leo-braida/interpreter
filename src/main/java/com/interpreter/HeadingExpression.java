package com.interpreter;

public class HeadingExpression implements Expression {
    private final int level;
    private final Expression inner;

    public HeadingExpression(int level, Expression inner) {
        if (level < 1) {
            this.level = 1;
        } else if (level > 6) {
            this.level = 6;
        } else {
            this.level = level;
        }
        this.inner = inner;
    }

    @Override
    public String interpret(Context context) {
        context.incrementHeadingCount();
        String content = (inner != null) ? inner.interpret(context) : "";
        return "<h" + level + ">" + content + "</h" + level + ">";
    }
}
