lexer grammar BuriLexer;

TEMPLATE_PARAM: '@('.*? ')';
EXPRESSION: '@{' .*? '}';
ESCAPE: '@@';

COMMENT_START: '@-';
COMMENT_END: '-@';
LINE_COMMENT_START: '@//';

BLOCK_COMMENT:	COMMENT_START .*? COMMENT_END -> skip;
LINE_COMMENT:	LINE_COMMENT_START ~[\r\n]* -> skip;

IF: '@if';
ELSE: 'else';
ELSE_IF: 'else if';
FOR: '@for';
BRAKE: '@brake';
CONTINUE: '@continue';

BRACKET_START: '(' -> pushMode(BRACKET);

BLOCK_START: '{' ->pushMode(BLOCK);

BLANK:  (' '|'\t'|'\r'? '\n')+;

HTML_TEXT: ~'@'+;

mode BRACKET;
CONDITION: ~[\(\)]+?;
BRACKET_END: ')' -> popMode;

mode BLOCK;
BLOCK_END: '}' -> popMode;
