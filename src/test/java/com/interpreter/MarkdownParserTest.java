package com.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarkdownParserTest {

    private Context context;

    @Mock
    private Expression mockExpression;

    @BeforeEach
    public void setUp() {
        context = new Context();
    }

    @Test
    public void testTextExpression() {
        TextExpression textExpr = new TextExpression("Hello World & Others");
        assertEquals("Hello World &amp; Others", textExpr.interpret(context));
    }

    @Test
    public void testTextExpressionNullAndEmpty() {
        TextExpression emptyExpr = new TextExpression("");
        assertEquals("", emptyExpr.interpret(context));

        TextExpression nullExpr = new TextExpression(null);
        assertEquals("", nullExpr.interpret(context));
    }

    @Test
    public void testBoldExpression() {
        when(mockExpression.interpret(context)).thenReturn("bold text");
        BoldExpression boldExpr = new BoldExpression(mockExpression);
        assertEquals("<strong>bold text</strong>", boldExpr.interpret(context));
    }

    @Test
    public void testItalicExpression() {
        when(mockExpression.interpret(context)).thenReturn("italic text");
        ItalicExpression italicExpr = new ItalicExpression(mockExpression);
        assertEquals("<em>italic text</em>", italicExpr.interpret(context));
    }

    @Test
    public void testHeadingExpressionValidLevels() {
        when(mockExpression.interpret(context)).thenReturn("Header");

        HeadingExpression h1 = new HeadingExpression(1, mockExpression);
        assertEquals("<h1>Header</h1>", h1.interpret(context));

        HeadingExpression h6 = new HeadingExpression(6, mockExpression);
        assertEquals("<h6>Header</h6>", h6.interpret(context));
    }

    @Test
    public void testHeadingExpressionInvalidLevelsClamping() {
        when(mockExpression.interpret(context)).thenReturn("Header");

        HeadingExpression hMin = new HeadingExpression(0, mockExpression);
        assertEquals("<h1>Header</h1>", hMin.interpret(context));

        HeadingExpression hMax = new HeadingExpression(7, mockExpression);
        assertEquals("<h6>Header</h6>", hMax.interpret(context));
    }

    @Test
    public void testParagraphExpression() {
        when(mockExpression.interpret(context)).thenReturn("some paragraph text");
        ParagraphExpression pExpr = new ParagraphExpression(mockExpression);
        assertEquals("<p>some paragraph text</p>", pExpr.interpret(context));
    }

    @Test
    public void testSequenceExpression() {
        Expression first = mock(Expression.class);
        Expression second = mock(Expression.class);
        when(first.interpret(context)).thenReturn("Part 1 ");
        when(second.interpret(context)).thenReturn("Part 2");

        SequenceExpression seqExpr = new SequenceExpression(Arrays.asList(first, second));
        assertEquals("Part 1 Part 2", seqExpr.interpret(context));
    }

    @Test
    public void testSequenceExpressionEmpty() {
        SequenceExpression emptySeq = new SequenceExpression(Collections.emptyList());
        assertEquals("", emptySeq.interpret(context));
    }

    @Test
    public void testParserPlainParagraph() {
        MarkdownParser parser = new MarkdownParser();
        Expression ast = parser.parse("Hello World");
        String result = ast.interpret(context);
        assertEquals("<p>Hello World</p>", result);
    }

    @Test
    public void testParserHeadings() {
        MarkdownParser parser = new MarkdownParser();

        assertEquals("<h1>Header 1</h1>", parser.parse("# Header 1").interpret(context));
        assertEquals("<h2>Header 2</h2>", parser.parse("## Header 2").interpret(context));
        assertEquals("<h3>Header 3</h3>", parser.parse("### Header 3").interpret(context));
        assertEquals("<h4>Header 4</h4>", parser.parse("#### Header 4").interpret(context));
        assertEquals("<h5>Header 5</h5>", parser.parse("##### Header 5").interpret(context));
        assertEquals("<h6>Header 6</h6>", parser.parse("###### Header 6").interpret(context));
    }

    @Test
    public void testParserHeadingWithExtraSpaces() {
        MarkdownParser parser = new MarkdownParser();
        assertEquals("<h1>Header  1</h1>", parser.parse("# Header  1").interpret(context));
        assertEquals("<p>#NotAHeader</p>", parser.parse("#NotAHeader").interpret(context));
        assertEquals("<p>####### Header 7</p>", parser.parse("####### Header 7").interpret(context));
    }

    @Test
    public void testParserInlineStyles() {
        MarkdownParser parser = new MarkdownParser();

        assertEquals("<p>This is <strong>bold</strong> text</p>", parser.parse("This is **bold** text").interpret(context));

        assertEquals("<p>This is <em>italic</em> text</p>", parser.parse("This is *italic* text").interpret(context));

        assertEquals("<p>This is <strong>bold</strong> and <em>italic</em> text</p>",
                parser.parse("This is **bold** and *italic* text").interpret(context));
    }

    @Test
    public void testParserNestedInlineStyles() {
        MarkdownParser parser = new MarkdownParser();

        assertEquals("<p>Nested <strong>bold with <em>italic</em> inside</strong></p>",
                parser.parse("Nested **bold with *italic* inside**").interpret(context));

        assertEquals("<p>Nested <em>italic with <strong>bold</strong> inside</em></p>",
                parser.parse("Nested *italic with **bold** inside*").interpret(context));
    }

    @Test
    public void testParserMultiLineInput() {
        MarkdownParser parser = new MarkdownParser();
        String input = "# Welcome\n\nThis is a paragraph with **bold**.\n\n## Section 1\n*Italic* text here.";
        Expression ast = parser.parse(input);
        String result = ast.interpret(context);

        String expected = "<h1>Welcome</h1>" +
                "<p>This is a paragraph with <strong>bold</strong>.</p>" +
                "<h2>Section 1</h2>" +
                "<p><em>Italic</em> text here.</p>";

        assertEquals(expected, result);
    }

    @Test
    public void testParserEmptyAndNullInput() {
        MarkdownParser parser = new MarkdownParser();
        assertEquals("", parser.parse(null).interpret(context));
        assertEquals("", parser.parse("").interpret(context));
        assertEquals("", parser.parse("   \n  \n ").interpret(context));
    }

    @Test
    public void testParserUnclosedFormatting() {
        MarkdownParser parser = new MarkdownParser();
        assertEquals("<p>This is **unclosed bold</p>", parser.parse("This is **unclosed bold").interpret(context));
        assertEquals("<p>This is *unclosed italic</p>", parser.parse("This is *unclosed italic").interpret(context));
    }
}
