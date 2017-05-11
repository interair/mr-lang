parser grammar MrParser;

options { tokenVocab=MrLexer; }

mrFile : lines=line+ ;

line      : statement (NEWLINE | EOF) ;

statement : varDeclaration # varDeclarationStatement
          | assignment     # assignmentStatement
          | print          # printStatement
          | map            # mapStatement ;

print : PRINT LPAREN expression RPAREN ;
map : MAP LPAREN expression COMMA ID LAMBDA expression RPAREN ;

varDeclaration : VAR assignment ;

assignment : ID ASSIGN expression
           | ID ASSIGN statement;


expression : left=expression operator=(DIVISION|ASTERISK) right=expression # binaryOperation
           | left=expression operator=(PLUS|MINUS) right=expression        # binaryOperation
           | left=expression operator=POWER right=expression               # binaryOperation
           | value=expression AS targetType=type                           # typeConversion
           | LPAREN expression RPAREN                                      # parenExpression
           | LRANGE expression COMMA expression RRANGE                     # range
           | ID                                                            # varReference
           | MINUS expression                                              # minusExpression
           | INTLIT                                                        # intLiteral
           | DECLIT                                                        # decimalLiteral ;

type : INT     # integer
     | DECIMAL # decimal ;
