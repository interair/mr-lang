package me.interair.lexer.mr.gui;

import me.interair.lexer.mr.MrLexer;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;

import java.awt.*;

class MrSyntaxScheme extends SyntaxScheme {

    public MrSyntaxScheme() {
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
                color = Color.BLUE;
                break;
            case MrLexer.ID:
                color = Color.MAGENTA;
                break;
            case MrLexer.LPAREN:
            case MrLexer.RPAREN:
                color = Color.WHITE;
                break;
            case MrLexer.UNMATCHED:
                color = Color.RED;
                break;
        }
        if (color != null) {
            style.foreground = color;
        }
        return style;
    }

}
