package compiladorl3.lexical;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Lexical {
	private char[] conteudo;
	private int indiceConteudo;

	public Lexical(String caminhoCodigoFonte) {
		try {
			String conteudoStr;
			conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
			this.conteudo = conteudoStr.toCharArray();
			this.indiceConteudo = 0;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private char nextChar() {
		return this.conteudo[this.indiceConteudo++];
	}

	private boolean hasNextChar() {
		return indiceConteudo < this.conteudo.length;
	}

	private void backIndex() {
		this.indiceConteudo--;
	}

	private boolean isLowerCaseLetter(char c) {
		return (c >= 'a') && (c <= 'z');
	}

	private boolean isDigit(char c) {
		return (c >= '0') && (c <= '9');
	}
	
	public Token nextToken() throws Exception {
		Token token = null;
		char currentChar = ' ';
		int state = 0, counter = 0;

		StringBuffer lexema = new StringBuffer();
		
		while (this.hasNextChar()) {
			
			currentChar = this.nextChar();
			
			switch (state) {
			case 0:
				state = initialStateMachine(currentChar, state, lexema);
				break;
			case 1:
				if (isPartOfAnIdentifier(currentChar)) {
					lexema.append(currentChar);
					//Modificado por @C03lh0
					if(ReservedWorld.isReservedWorld(lexema.toString())) {
						return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA);
					} else if (!this.hasNextChar()) {
						return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
					}
					state = 1;
				} else {
					this.backIndex();
					return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
				}
				break; 
			case 2:
				if (this.isDigit(currentChar)) {
					lexema.append(currentChar);
					state = 2;
				} else if (currentChar == '.') {
					lexema.append(currentChar);
					state = 3;
				} else {
					this.backIndex();
					return new Token(lexema.toString(), Token.TIPO_INTEIRO);
				}
				break;
			case 3:
				if (this.isDigit(currentChar)) {
					lexema.append(currentChar);
					state = 4;
				} else {
					throw new RuntimeException("Erro: numero float invalido \"" + lexema.toString() + "\"");
				}
				break;
			case 4:
				if (this.isDigit(currentChar)) {
					lexema.append(currentChar);
					state = 4;
				} else {
					this.backIndex();
					return new Token(lexema.toString(), Token.TIPO_REAL);
				}
				break;
			case 5:
				this.backIndex();
				return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL);
			case 6:
				if (isAssignmentOperator(currentChar)) {
					this.backIndex();
					state = 9;
				} else  {
					this.backIndex();
					return new Token(lexema.toString(), Token.TIPO_OPERADOR_DE_ATRIBUICAO);
				}
				break;
			case 7:		
				if(isPartOfChar(currentChar, lexema)) {
					state = 7;
					if (lexema.length() == 3) {
						lexema.append(currentChar);
						return new Token(lexema.toString(), Token.TIPO_CHAR);
					}
				} else {
					throw new RuntimeException("Erro: char invalido \"" + lexema.toString() + "\"");
				}
				break;
			case 9:
				if (!isPartOfAnRelationalOperator(currentChar, state, lexema) || !this.hasNextChar() || currentChar == '\n')
					return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
				break;
			case 10:
				break;
			case 11:
				if(currentChar == '\n' || !isAArithmeticOperator(currentChar)){
					this.backIndex();
					return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
				} else if (isADoublyArithmeticOperator(currentChar, lexema)) {
					return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
				//Alterado por @C03lh0	Matheus's Token (++ or +++ | -- or ---)
				} else if (this.isAArithmeticOperator(currentChar)){
					if(isArithmeticOperatorEquals(currentChar, lexema) && counter<2) {
						lexema.append(currentChar);
						counter++;
					} else {
						lexema.append(currentChar);
						throw new RuntimeException("Erro: Operadores aritimeticos n�o reconhecido! \"" + lexema.toString() + "\"");
					}
				} 
				break;
			case 99:
				return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO);
				
			}
		}
		return checkIfLexemaIsNotEmpty(token, currentChar, lexema);
	}

	private Token checkIfLexemaIsNotEmpty(Token token, char currentChar, StringBuffer lexema) {
        if(lexema.length() != 0){
            return returnCurrentToken(currentChar, lexema);
        } else {
            return token;
        }
    }

	private int initialStateMachine(char currentChar, int estado, StringBuffer lexema) {
		if (isBlankSpace(currentChar)) {
			estado = 0;
		} else if (isLetterOrUnderscore(currentChar)) {
			lexema.append(currentChar);
			estado = 1;
		} else if (this.isDigit(currentChar)) {
			lexema.append(currentChar);
			estado = 2;
		} else if (isASpecialCharacter(currentChar)) {
			lexema.append(currentChar);
			estado = 5;
		} else if (currentChar == '$') {
			lexema.append(currentChar);
			estado = 99;
			this.backIndex();
		} else if (isAssignmentOperator(currentChar)) {
			lexema.append(currentChar);
			estado = 6;
		} else if (isChar(currentChar)) {
			lexema.append(currentChar);
			estado = 7;
		} else if (isAquilesToken(currentChar)) {
			lexema.append(currentChar);
			estado = 8;
		} else if (isRelationalOperator(currentChar)) {
			lexema.append(currentChar);
			estado = 9;
		} else if (isAArithmeticOperator(currentChar)){
			lexema.append(currentChar);
			estado = 11;
        } else {
			lexema.append(currentChar);
			throw new RuntimeException("Erro: token invalido \"" + lexema.toString() + "\"");
		}
		return estado;
	}
	
	private boolean isAssignmentOperator(char c) {
		return c == '=';
	}
	
	private boolean isChar(char c) {
		String aux = "'";
		return aux.charAt(0) == c;
	}
	
	private boolean isPartOfChar (char c, StringBuffer lex) {
		if (!((this.isDigit(c) || this.isLowerCaseLetter(c)) && lex.length() == 1) 
				&& !(c == "'".charAt(0) && lex.length() == 2)) {
			return false;
		}
		lex.append(c);
		return true;
	}
	
	private boolean isAquilesToken(char c) {
		return c == '#';
	}

	private boolean isPartOfAnIdentifier(char c) {
		return this.isLowerCaseLetter(c) || this.isDigit(c) || c == '_';
	}

	private boolean isASpecialCharacter(char c) {
		return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';';
	}

	private boolean isLetterOrUnderscore(char c) {
		return this.isLowerCaseLetter(c) || c == '_';
	}

	private boolean isBlankSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	private boolean isRelationalOperator(char c) {
		return c == '<' || c == '>' || c == '=' || c == '!';
	}

	private boolean isAArithmeticOperator(char c){
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

	private boolean isPartOfAnRelationalOperator(char currentChar, int estado, StringBuffer lexema) throws Exception{
		if (currentChar == '=' && lexema.length() < 2) {
			lexema.append(currentChar);
			return true;
		}
		this.backIndex();
		return false;
	}
	
	public int getIndiceConteudo() {
		return indiceConteudo;
	}

	public void setIndiceConteudo(int indiceConteudo) {
		this.indiceConteudo = indiceConteudo;
	}

	private boolean isADoublyArithmeticOperator(char c, StringBuffer lex){
		//This method was created for cristofer's personal token
		String supposedDoublyPotency = lex.toString() + "*";
		String supposedDoublySQRT = lex.toString() + "/";

		if (supposedDoublyPotency.equals("**")||supposedDoublySQRT.equals("*/")) {
			lex.append(c);
			return true;
		}
		return false;
	}
	
	private Token returnCurrentToken(char currentChar, StringBuffer lexema) {
		if (isLetterOrUnderscore(currentChar)) {
			return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
	   } else if (this.isDigit(currentChar)) {
		   return new Token(lexema.toString(), Token.TIPO_INTEIRO);
	   } else if (isASpecialCharacter(currentChar)) {
		   return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL);
	   } else if (currentChar == '$') {
		   return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO);
	   } else if (isAssignmentOperator(currentChar)) {
		   return new Token(lexema.toString(), Token.TIPO_OPERADOR_DE_ATRIBUICAO);
	   } else if (isRelationalOperator(currentChar)) {
		   return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
	   } else if (isAArithmeticOperator(currentChar)) {
		   return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
	   }  else {
		   lexema.append(currentChar);
		   throw new RuntimeException("Erro: token invalido \"" + lexema.toString() + "\"");
	   }
	}
	
	private boolean isArithmeticOperatorEquals(char currentChar, StringBuffer lexema) {
		char [] lexemaCharArray = lexema.toString().toCharArray();
		boolean isEquals = false;
		for (int i = 0; i < lexemaCharArray.length; i++) {
			if(currentChar == lexemaCharArray[i]) {
				isEquals = true;
			} else {
				isEquals = false;
			}
		}
		return isEquals;
	}

}
