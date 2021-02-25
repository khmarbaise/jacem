grammar expression;

@header {
    package com.soebes.cralinkr.parser;
}

prog: expr+;

expr: expr ('*'|'/') expr
    | expr ('+'|'-') expr
    | '(' expr ')'
    | NUM
    | HEX
    | HEX2
    | BINARY
    | BINARY2
    | OCTAL
    | OCTAL2
    | IDENTIFIER
    ;
OCTAL: ('0o'|'0O') [0-7][0-7_]*;// 0o1023_3333
OCTAL2: [0-7][0-7_]*('O'|'o');  // 1122O
BINARY: ('0b'|'0B') [01][_01]*; // 0b1010_1010
BINARY2: [01][_01]*('B'|'b') ;  // 1000_1000B
HEX : '$' [a-fA-F0-9][_a-fA-F0-9]*; // This allows $1000_0000_0000_0000
HEX2 : [a-fA-F0-9][_a-fA-F0-9]* ('H'|'h'); // This allows 1000_0000_0000_0000H
NUM : '-'?[0-9][_0-9]*;
IDENTIFIER: [a-zA-Z]+[a-zA-Z0-9]*;

WS: [ \t]+ -> skip;
COMMENT: (';'|'//') .*? '\r'? '\n' -> skip;

MUL: '*';
DIV: '/';
ADD: '+';
SUB: '-';
