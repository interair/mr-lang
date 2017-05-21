package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Issue {
    private final IssueType issueType;
    private final String message;
    private final int line;
    private final int offset;
    private final int length;

    @Override
    public String toString() {
        return message + ", line=" + line + ", offset=" + offset + "; ";
    }
}
