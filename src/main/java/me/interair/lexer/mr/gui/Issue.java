package me.interair.lexer.mr.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
class Issue {
    private final IssueType issueType;
    private final String message;
    private final int line;
    private final int offset;
    private final int length;

}
