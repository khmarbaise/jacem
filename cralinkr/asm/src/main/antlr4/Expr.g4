grammar Expr;

@header {
    package com.soebes.cralinkr.parser;
}

prog: expr*;

expr: expr ('*'|'/') expr
    | expr ('+'|'-') expr
    | '(' expr ')'
    | NUM
    | HEX
    | BINARY
    | OCTAL
    | IDENTIFIER
    ;

OCTAL: ('0o'|'0O') [0-7][0-7_]*;// 0o1023_3333
BINARY: ('0b'|'0B') [01][_01]*; // 0b1010_1010
HEX : '$' [a-fA-F0-9][_a-fA-F0-9]*; // This allows $1000_0000_0000_0000
NUM : '-'?[0-9][_0-9]*;
IDENTIFIER: [a-zA-Z]+[a-zA-Z0-9]*;

WS: [ \t]+ -> skip;
COMMENT: (';'|'//') .*? '\r'? '\n' -> skip;

LPARENT: '(';
RPARENT: ')';
MUL: '*';
DIV: '/';
ADD: '+';
SUB: '-';
