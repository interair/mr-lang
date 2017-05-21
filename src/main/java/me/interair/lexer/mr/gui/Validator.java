package me.interair.lexer.mr.gui;

import me.interair.lexer.mr.eval.Issue;

import java.util.List;

interface Validator {
    List<Issue> validate(String code);

}