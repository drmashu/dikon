lexer grammar BuriLexer;

ANY_TEXT: ~'@'+;

MAGIC_START: '@' -> pushMode(MAGIC);

mode MAGIC;
MAGIC_END: '@' -> popMode;

BRACKET_START: '(' -> pushMode(PARAM_MODE);

PARAM: BRACKET_START ~[)]*? BRACKET_END;



BLANK:  (' '|'\t'|('\r'? '\n'));

ID :   LETTER (LETTER | [0-9])*;
fragment LETTER
    : [a-zA-Z$_]
    | ~[\u0000-\u00FF\uD800-\uDBFF]
    | [\uD800-\uDBFF] [\uDC00-\uDFFF]
    ;

IF: 'if' -> pushMode(CONDITIONS_MODE);
FOR: 'for' -> pushMode(CONDITIONS_MODE);
BRAKE: 'brake';
CONTINUE: 'continue';
FUNCTION: 'fun';
CALL: 'call';
INSERT: BLOCK_START ~[}]*? BLOCK_END;


ELSE: 'else';
ELSE_IF: ELSE BLANK 'if';

BLOCK_START: '{';
BLOCK_END: '}';

LINE_COMMENT: '//' ~[\r\n]* -> skip;

COMMENT_START: '/*' ->pushMode(COMMENT);

mode PARAM_MODE;
BRACKET_END: ')' -> popMode;

mode CONDITIONS_MODE;

CONDITIONS: '(' BLANK* (CONDITIONS | CONDITION) BLANK* ')';

CONDITION: ~[\)]+?;

CONDITION_END: '{';

mode COMMENT;
COMMENT_BODY: .+? -> skip;
COMMENT_END: '*/' -> skip, popMode;
