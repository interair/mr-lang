package me.interair.lexer.mr.gui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Issue {
    private IssueType issueType;
    private String message;
    private int line;
    private int offset;
    private int length;

}
