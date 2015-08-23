parser grammar BuriParser;
options { tokenVocab=BuriLexer; }
@members {

}
buri: templateParam body;
templateParam: MAGIC_START param MAGIC_END;
body: (text command)+ text?;

command: MAGIC_START (command0 | command1 | command2 | command3 | command4) MAGIC_END;

command0
    : (cmdBrake | cmdContinue | cmdInsert);
command1
    : cmdCall BLANK* conditions;
command2
    :
    ( cmdIf
    | cmdElseIf
    | cmdFor
    ) BLANK* conditions BLANK* BLOCK_START;
command3
    : BLOCK_END cmdElse BLANK* BLOCK_START;
command4
    : cmdFunction BLANK* param BLANK* BLOCK_START;

cmdIf: IF;
cmdFor: FOR;
cmdInsert: INSERT;

cmdElseIf: BLOCK_END BLANK* ELSE_IF;
cmdElse: BLOCK_END BLANK* ELSE;

cmdBrake: BRAKE;
cmdContinue: CONTINUE;
cmdFunction: FUNCTION BLANK+ ID ;
cmdCall: CALL;

param: PARAM;
conditions: CONDITIONS;

text: (BLANK | escape | ANY_TEXT)+;
escape: MAGIC_START MAGIC_END;

comment: MAGIC_START COMMENT_START COMMENT_BODY? COMMENT_END MAGIC_END;

blockEnd: MAGIC_START BLOCK_END MAGIC_END;
