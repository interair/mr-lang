package me.interair.lexer.mr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MrLexerTest {

    private MrLexer lexerForCode(String code) {
        return new MrLexer(CharStreams.fromString(code));
    }

    public List<String> tokens(MrLexer lexer) {

        List<String> tokens = new ArrayList<>();
        for (; ; ) {
            Token token = lexer.nextToken();
            if (token.getType() == -1) {
                return tokens;
            }
            if (token.getType() != MrLexer.WS) {
                tokens.add(MrLexer.ruleNames[token.getType() - 1]);
            }
        }
    }

    @Test
    public void parseVarDeclarationAssignedAnIntegerLiteral() {
        assertEquals(asList("VAR", "ID", "ASSIGN", "INTLIT"),
                tokens(lexerForCode("var a = 1")));
    }

    @Test
    public void parseMathematicalExpression() {
        assertEquals(asList("INTLIT", "PLUS", "ID", "ASTERISK", "INTLIT", "DIVISION", "INTLIT", "MINUS", "INTLIT"),
                tokens(lexerForCode("1 + a * 3 / 4 - 5")));
    }
}