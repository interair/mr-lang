package me.interair.lexer.mr.gui;

import org.antlr.v4.runtime.Lexer;

interface AntlrLexerFactory {
    Lexer create(String code);
}