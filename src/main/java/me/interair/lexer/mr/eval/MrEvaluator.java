package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.MrLexer;
import me.interair.lexer.mr.MrParser;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.runtime.CharStreams.fromString;

@Slf4j
public class MrEvaluator implements Evaluator {

    public Result evaluate(String code) {
        List<Issue> issues = new ArrayList<>();
        Result.ResultBuilder result = Result.builder().issues(issues);
        MrLexer mrLexer = new MrLexer(fromString(code));
        mrLexer.removeErrorListeners();
        DescriptiveErrorListener errorListener = new DescriptiveErrorListener(issues);
        mrLexer.addErrorListener(errorListener);
        CommonTokenStream tokens = new CommonTokenStream(mrLexer);
        MrParser mrParser = new MrParser(tokens);
        mrParser.removeErrorListeners();
        mrParser.addErrorListener(errorListener);
        try {
            Value visit = new EvalVisitor().visit(mrParser.mrFile());
            return result.value(visit).build();
        } catch (Exception e) {
            log.error("", e);
            return result.exception(e).build();
        }
    }

    @AllArgsConstructor
    public static class DescriptiveErrorListener extends BaseErrorListener {

        private final List<Issue> issues;

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg, RecognitionException e) {

            issues.add(Issue.builder()
                    .line(line)
                    .message(msg)
                    .offset(charPositionInLine)
                    .issueType(IssueType.ERROR)
                    .length(1)
                    .build());
        }
    }
}
