package me.interair.lexer.mr.gui;

import java.util.List;

interface Validator {
    List<Issue> validate(String code);

}