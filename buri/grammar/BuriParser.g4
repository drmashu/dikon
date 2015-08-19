grammar Buri;

template: templateParam body;
templateParam: TEMPLATE_PARAM;

body: (text | element/* | comment*/)*;
text: (HTML_TEXT | BLANK | escape)+;
escape: ESCAPE;

// comment: BLOCK_COMMENT | LINE_COMMENT;

element: insert | command;// | function | call;
command: MAGIC ID? (BLANK? conditions)? (BLANK? content)?;
/*    : cmdIf = IF BLANK* conditions BLANK* content
    | cmdElseIf = ELSE_IF BLANK* content
    | cmdElse = ELSE BLANK* content
    | cmdFor = FOR BLANK* conditions BLANK* content
    | cmdBrake = BRAKE
    | cmdContinue = CONTINUE
    ;
*/
insert: MAGIC EXPRESSION;
//function: MAGIC FUNCTION BLANK* ID BLANK* conditions BLANK* content;
//call: MAGIC ID BLANK* conditions;
//test: (conditions BLANK*)+;
conditions: BRACKET_START (CONDITION | conditions)+ BRACKET_END;
content: BLOCK_START body BLOCK_END;

ID :   LETTER (LETTER | [0-9])*;
fragment LETTER
    : [a-zA-Z$_]
    | ~[\u0000-\u00FF\uD800-\uDBFF]
    | [\uD800-\uDBFF] [\uDC00-\uDFFF]
    ;

MAGIC: '@';

BLANK:  (' '|'\t'|('\r'? '\n'));
HTML_TEXT: ~'@'+;

TEMPLATE_PARAM: MAGIC BRACKET_START ~[)]*? BRACKET_END;
EXPRESSION: BLOCK_START ~[}]*? BLOCK_END;

ESCAPE: MAGIC MAGIC;

BLOCK_COMMENT: COMMENT_START .*? COMMENT_END -> skip;
fragment COMMENT_START: MAGIC COMMENT_MARK;
fragment COMMENT_END: COMMENT_MARK MAGIC;
fragment COMMENT_MARK: '-';

LINE_COMMENT: MAGIC LINE_COMMENT_MARK ~[\r\n]* -> skip;
fragment LINE_COMMENT_MARK: '//';

IF: MAGIC 'if';
ELSE: MAGIC 'else';
ELSE_IF: ELSE BLANK IF;
FOR: MAGIC 'for';
BRAKE: MAGIC 'brake';
CONTINUE: MAGIC 'continue';
FUNCTION: MAGIC 'fun';

BRACKET_START: '(';
BRACKET_END: ')';
CONDITION: ~[\)]+?;

BLOCK_START: '{';
BLOCK_END: '}';
