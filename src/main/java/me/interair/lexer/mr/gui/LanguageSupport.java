package me.interair.lexer.mr.gui;

import org.fife.ui.rsyntaxtextarea.SyntaxScheme;

interface LanguageSupport {
    SyntaxScheme getSyntaxScheme();
    AntlrLexerFactory getAntlrLexerFactory();
    Validator getValidator();

}