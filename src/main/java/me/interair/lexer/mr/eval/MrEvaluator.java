package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.MrLexer;
import me.interair.lexer.mr.MrParser;
import org.antlr.v4.runtime.*;
import org.springframework.util.CollectionUtils;

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
        MrParser.MrFileContext mrFileContext = mrParser.mrFile();
        if (CollectionUtils.isEmpty(issues)) {
            Value visit = new EvalVisitor().visit(mrFileContext);
            result.value(visit);
        }
        return result.build();
    }

    @AllArgsConstructor
    public static class DescriptiveErrorListener extends BaseErrorListener {

        private final List<Issue> issues;

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg, RecognitionException e) {

            CommonToken commonToken = (CommonToken) offendingSymbol;
            int tokenIndex = commonToken.getTokenIndex();
            issues.add(Issue.builder()
                    .line(line)
                    .message(msg)
                    .offset(tokenIndex)
                    .issueType(IssueType.ERROR)
                    .length(charPositionInLine - tokenIndex)
                    .build());
        }
    }
}
