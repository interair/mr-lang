package me.interair.lexer.mr.gui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATN;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ParseData {
    List<String> ruleNames;
    Vocabulary vocabulary;
    ATN atn;
}

