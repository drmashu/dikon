parser grammar BuriParser;

options {tokenVocab=BuriLexer;}
template: TEMPLATE_PARAM document;
document: (text | elements)*;
text: (HTML_TEXT | BLANK | escape)+;
escape: ESCAPE;
elements: misc* element misc*;
misc: (comment | BLANK);
comment: BLOCK_COMMENT | LINE_COMMENT;
element
    : EXPRESSION
    | IF conditions content (BLANK? ELSE_IF conditions content)* (BLANK? ELSE content)?
    | FOR conditions content
    | BRAKE
    | CONTINUE
    ;
conditions: BLANK? BRACKET_START BLANK? (CONDITION | conditions)+ BRACKET_END;
content: BLANK? BLOCK_START document BLOCK_END;
