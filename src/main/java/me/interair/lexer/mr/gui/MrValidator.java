package me.interair.lexer.mr.gui;

import lombok.AllArgsConstructor;
import me.interair.lexer.mr.MrLexer;
import me.interair.lexer.mr.MrParser;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.runtime.CharStreams.fromString;

public class MrValidator implements Validator {
    @Override
    public List<Issue> validate(String code) {
        List<Issue> issues = new ArrayList<>();
        MrLexer mrLexer = new MrLexer(fromString(code));
        mrLexer.removeErrorListeners();
        mrLexer.addErrorListener(new DescriptiveErrorListener(issues));
        CommonTokenStream tokens = new CommonTokenStream(mrLexer);
        MrParser mrParser = new MrParser(tokens);
        mrParser.removeErrorListeners();
        mrParser.addErrorListener(new DescriptiveErrorListener(issues));
        mrParser.mrFile();
        return issues;
    }

    @AllArgsConstructor
    private class DescriptiveErrorListener extends BaseErrorListener {

        private final List<Issue> issues;

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg, RecognitionException e) {

            issues.add(Issue.builder().line(line).message(msg).offset(charPositionInLine).issueType(IssueType.ERROR).build());
        }
    }
}
