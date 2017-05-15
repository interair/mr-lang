package me.interair.lexer.mr.gui;

import java.util.Collections;
import java.util.List;

interface Validator {
    List<Issue> validate(String code);

    class EverythingOkValidator implements Validator {

        @Override
        public List<Issue> validate(String code) {
            return Collections.emptyList();
        }
    }
}