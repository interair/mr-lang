package me.interair.lexer.mr.gui;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Lexer;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;

import javax.swing.text.Segment;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
class AntlrTokenMaker extends TokenMakerBase {

    private final AntlrLexerFactory antlrLexerFactory;

    @Override
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        if (text == null) { throw new IllegalArgumentException("text can't be null"); }
        Lexer lexer = antlrLexerFactory.create(text.toString());
        List<org.antlr.v4.runtime.Token> tokens = new LinkedList<>();
        while (!lexer._hitEOF) {
            tokens.add(lexer.nextToken());
        }
        return toList(text, startOffset, tokens);
    }

    private Token toList(Segment text, int startOffset, List<org.antlr.v4.runtime.Token> antlrTokens) {
        if (antlrTokens.isEmpty()) {
            return null;
        } else {
            org.antlr.v4.runtime.Token at = antlrTokens.get(0);
            TokenImpl t = new TokenImpl(text, text.offset + at.getStartIndex(),
                    text.offset + at.getStartIndex() + at.getText().length() - 1,
                    startOffset + at.getStartIndex(), at.getType(), 0);
            t.setNextToken(toList(text, startOffset, antlrTokens.subList(1, antlrTokens.size())));
            return t;
        }
    }
}
