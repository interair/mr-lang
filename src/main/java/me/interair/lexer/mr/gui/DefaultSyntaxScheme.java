package me.interair.lexer.mr.gui;

import me.interair.lexer.mr.MrLexer;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;

import java.awt.*;

class DefaultSyntaxScheme extends SyntaxScheme {

    public DefaultSyntaxScheme() {
        super(true);
    }

    public Style getStyle(int index) {
        Style style = super.getStyle(index);
        Color color = null;
        switch (index) {
            case MrLexer.VAR:
                color = Color.GREEN;
                break;
            case MrLexer.ASSIGN:
                color = Color.GREEN;
                break;
            case MrLexer.ASTERISK:
            case MrLexer.DIVISION:
            case MrLexer.PLUS:
            case MrLexer.MINUS:
                color = Color.WHITE;
                break;
            case MrLexer.INTLIT:
            case MrLexer.DECLIT:
                color = new Color(255, 252, 3);
                break;
            case MrLexer.ID:
                color = new Color(81, 253, 255);
                break;
            case MrLexer.COMMA:
            case MrLexer.LPAREN:
            case MrLexer.RPAREN:
                color = Color.WHITE;
                break;
            case MrLexer.MAP:
                color = new Color(154, 117, 255);
            case MrLexer.REDUCE:
                break;
            case MrLexer.UNMATCHED:
                color = Color.RED;
        }
        if (color != null) {
            style.foreground = color;
        }
        return style;
    }

}
