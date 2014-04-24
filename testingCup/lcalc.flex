
   
import java_cup.runtime.*;
      
%%
%class Lexer

%line
%column
%cup
   

%{   
    
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {	  
    	return new Symbol(type, yyline, yycolumn, value);
    }
    
%}
   
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

dec_int_lit 	= 0 | [1-9][0-9]*("."[1-9][0-9]*)?
dec_int_id 		= [A-Za-z_][A-Za-z_0-9]*
NumEntero		= [0-9]
oprel			= [<>"==""or""and"]
oparit			= [+-][*]
 
%%  
   /* YYINITIAL is the state */
   
<YYINITIAL> {
   
   ":="               { System.out.print(":= "); return symbol(sym.ASIGNACION); }
    ";"                { return symbol(sym.PCOMA); }
    "integer"          { System.out.print("integer"); return symbol(sym.INTEGER); }
    ":"                { System.out.print(":"); return symbol(sym.DOSP); }
    "["                { System.out.print(" [ "); return symbol(sym.CABIERTO); }
    "]"                { System.out.print(" ] "); return symbol(sym.CCERRADO); }
    "real"             { System.out.print("real"); return symbol(sym.REAL); }
    "array"            { System.out.print("array"); return symbol(sym.ARRAY); }
    {NumEntero}        { System.out.print("NumEntero"); return symbol(sym.NUMENTERO); }
    "of"               { System.out.print("of"); return symbol(sym.OF); }
    "basico"           { System.out.print("basico"); return symbol(sym.BASICO); }
    "id"               { System.out.print("id "); return symbol(sym.ID); }
    "if"               { System.out.print("if"); return symbol(sym.IF); }
    "then"             { System.out.print("then"); return symbol(sym.THEN); }
    "while"            { System.out.print(" while "); return symbol(sym.WHILE); }
    "do"               { System.out.print("do"); return symbol(sym.DO); }
    "oprel"            { System.out.print("oprel"); return symbol(sym.OPREL); }
     "NumReal"         { System.out.print("NumReal "); return symbol(sym.NUMREAL); }
    "mod"              { System.out.print("mod"); return symbol(sym.MOD); }
    "oparit"           { System.out.print("oparit"); return symbol(sym.OPARIT); }
    "^"                { System.out.print("^"); return symbol(sym.POTENCIA); }
    "{"				   { System.out.print("{\n\t"); return symbol(sym.KOPEN); }
    "}"				   { System.out.print("\n}"); return symbol(sym.KCLOSE); }
    
    {dec_int_lit}      { System.out.print(yytext());
                         return symbol(sym.NUMBER, new Integer(yytext())); }
   
    {dec_int_id}       { System.out.print(yytext());
                         return symbol(sym.ID, new Integer(1));}
   
    {WhiteSpace}       { /* do nothing */ }   
}

[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
