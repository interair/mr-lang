package me.interair.lexer.mr.gui;

import me.interair.lexer.mr.eval.Evaluator;
import me.interair.lexer.mr.eval.Issue;
import me.interair.lexer.mr.eval.MrEvaluator;
import me.interair.lexer.mr.eval.Result;

import java.util.List;

public class MrValidator implements Validator {

    @Override
    public List<Issue> validate(String code) {
        Evaluator evaluator = new MrEvaluator();
        Result evaluate = evaluator.evaluate(code);
        return evaluate.getIssues();
    }

  
}
