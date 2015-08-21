parser grammar BuriParser;

options { tokenVocab=BuriLexer; }
@members {

}
template: templateParam body;
templateParam: MAGIC_START param MAGIC_END;
body: (insert | command | blockEnd | comment | text)+;
escape: MAGIC_START MAGIC_END;

insert: MAGIC_START INSERT MAGIC_END;
command
    : cmdIf
    | cmdElseIf
    | cmdElse
    | cmdFor
    | cmdBrake
    | cmdContinue
    | cmdFunction
    | cmdCall
    ;

cmdIf: MAGIC_START IF BLANK* conditions BLANK* BLOCK_START MAGIC_END;
cmdElseIf: MAGIC_START BLOCK_END BLANK* ELSE_IF BLANK* conditions BLANK* BLOCK_START MAGIC_END;
cmdElse: MAGIC_START BLOCK_END BLANK* ELSE BLANK* BLOCK_START MAGIC_END;
cmdFor: MAGIC_START FOR BLANK* conditions BLANK* BLOCK_START MAGIC_END;
cmdBrake: MAGIC_START BRAKE MAGIC_END;
cmdContinue: MAGIC_START CONTINUE MAGIC_END;
cmdFunction: MAGIC_START FUNCTION BLANK+ ID param BLANK* BLOCK_START MAGIC_END;
cmdCall: MAGIC_START CALL BLANK+ conditions MAGIC_END;

param: PARAM;
conditions: CONDITIONS;
text: (escape | BLANK | ANY_TEXT)+;

comment: MAGIC_START COMMENT_START COMMENT_BODY? COMMENT_END MAGIC_END;

blockEnd: MAGIC_START BLOCK_END MAGIC_END;
