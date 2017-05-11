parser grammar MrParser;

options { tokenVocab=MrLexer; }

mrFile : lines=line+ ;

line      : statement (NEWLINE | EOF) ;

statement : varDeclaration # varDeclarationStatement
          | assignment     # assignmentStatement
          | print          # printStatement
          | map            # mapStatement;

varDeclaration : VAR assignment ;

print : PRINT LPAREN expression RPAREN ;
map : MAP LPAREN expression COMMA ID LAMBDA expression RPAREN ;

assignment : ID ASSIGN expression
           | ID ASSIGN statement;


expression : left=expression operator=(DIVISION|ASTERISK) right=expression               # binaryOperation
           | left=expression operator=(PLUS|MINUS) right=expression                      # binaryOperation
           | left=expression operator=POWER right=expression                             # binaryOperation
           | LPAREN expression RPAREN                                                    # parenExpression
           | LRANGE expression COMMA expression RRANGE                                   # range
           | REDUCE LPAREN ID COMMA (INTLIT|DECLIT) COMMA ID ID LAMBDA expression RPAREN # reduceStatement
           | ID                                                                          # varReference
           | MINUS expression                                                            # minusExpression
           | INTLIT                                                                      # intLiteral
           | DECLIT                                                                      # decimalLiteral ;

type : INT     # integer
     | DECIMAL # decimal ;
