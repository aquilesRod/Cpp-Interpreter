package compiladorl3.lexical;

public class Token {

    public static int TYPE_INT = 0;
    public static int TYPE_FLOAT = 1;
    public static int TYPE_CHAR = 2;
    public static int TYPE_IDENTIFIER = 3;
    public static int TYPE_RELATIONAL_OPERATOR = 4;
    public static int TYPE_ARITHMETIC_OPERATOR = 5;
    public static int TYPE_SPECIAL_CHARACTER = 6;
    public static int TYPE_RESERVED_WORLD = 7;
    public static int TYPE_ASSIGNMENT_OPERATOR = 8;
    public static int TYPE_BRUNO_TOKEN= 77;
    public static int TIPO_AQUILES_TOKEN = 88;
    public static int TYPE_END_OF_CODE = 99;
    
    private int type;
    private String lexeme;
    
    public Token(String lexeme, int type){
        this.lexeme = lexeme;
        this.type = type;
    }
    
    public String getLexeme(){
        return this.lexeme;
    }
    
    public int getType(){
        return this.type;
    }
    
    @Override
    public String toString()
    {
        switch(this.type){
            case 0:
                return this.lexeme + " - INTEIRO" ;
            case 1:
                return this.lexeme + " - REAL";
            case 2:
                return this.lexeme + " - CHAR";
            case 3:
                return this.lexeme + " - IDENTIFICADOR";
            case 4:
                return this.lexeme + " - OPERADOR_RELACIONAL";
            case 5:
                return this.lexeme + " - OPERADOR_ARITMETICO";
            case 6:
                return this.lexeme + " - CARACTER_ESPECIAL";
            case 7:
                return this.lexeme + " - PALAVRA_RESERVADA";
            case 8:
            	return this.lexeme + " - OPERADOR_DE_ATRIBUICAO";
            case 88:
            	return this.lexeme + " - TOKEN_AQUILES";
            case 99:
                return this.lexeme + " - FIM_CODIGO";    
        }
        return "";
    }
}
