package compiladorl3.lexical;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Lexical {

	private char[] content;
	private int contentIndex;

	public Lexical(String caminhoCodigoFonte) {
		try {
			String stringContent;
			stringContent = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
			this.content = stringContent.toCharArray();
			this.contentIndex = 0;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private char nextChar() {
		return this.content[this.contentIndex++];
	}

	private boolean hasNextChar() {
		return contentIndex < this.content.length;
	}

	private void backIndex() {
		this.contentIndex--;
	}
	
	public Token nextToken() throws Exception {
		Token token = null;
		char currentChar = ' ';
		int state = 0, counter = 0;

		StringBuffer lexeme = new StringBuffer();
		
		while (this.hasNextChar()) {
			
			currentChar = this.nextChar();
			
			switch (state) {
			case 0:
				state = initialStateMachine(currentChar, state, lexeme);
				break;
			case 1:
				if (isPartOfAnIdentifier(currentChar)) {
					lexeme.append(currentChar);

					if(ReservedWorld.isReservedWorld(lexeme.toString())) {
						return new Token(lexeme.toString(), Token.TYPE_RESERVED_WORLD);
					} else if (!this.hasNextChar()) {
						return new Token(lexeme.toString(), Token.TYPE_IDENTIFIER);
					}
					
					state = 1;
				} else {
					this.backIndex();
					return new Token(lexeme.toString(), Token.TYPE_IDENTIFIER);
				}
				break; 
			case 2:
				if (this.isDigit(currentChar)) {
					lexeme.append(currentChar);
					state = 2;
				} else if (currentChar == '.') {
					lexeme.append(currentChar);
					state = 3;
				} else {
					this.backIndex();
					return new Token(lexeme.toString(), Token.TYPE_INT);
				}
				break;
			case 3:
				if (this.isDigit(currentChar)) {
					lexeme.append(currentChar);
					state = 4;
				} else {
					throw new RuntimeException("Error: invalid float number \"" + lexeme.toString() + "\"");
				}
				break;
			case 4:
				if (this.isDigit(currentChar)) {
					lexeme.append(currentChar);
					state = 4;
				} else {
					this.backIndex();
					return new Token(lexeme.toString(), Token.TYPE_FLOAT);
				}
				break;
			case 5:
				this.backIndex();
				return new Token(lexeme.toString(), Token.TYPE_SPECIAL_CHARACTER);
			case 6:
				if (isAssignmentOperator(currentChar)) {
					this.backIndex();
					state = 9;
				} else  {
					this.backIndex();
					return new Token(lexeme.toString(), Token.TYPE_ASSIGNMENT_OPERATOR);
				}
				break;
			case 7:		
				if(isPartOfChar(currentChar, lexeme)) {
					state = 7;
					if (lexeme.length() == 3) {
						lexeme.append(currentChar);
						return new Token(lexeme.toString(), Token.TYPE_CHAR);
					}
				} else {
					throw new RuntimeException("Error: invalid char \"" + lexeme.toString() + "\"");
				}
				break;
			case 9:
				if (!isPartOfAnRelationalOperator(currentChar, state, lexeme) || !this.hasNextChar() || currentChar == '\n')
					return new Token(lexeme.toString(), Token.TYPE_RELATIONAL_OPERATOR);
				break;
			case 10:
				break;
			case 11:
				if(currentChar == '\n' || !isAArithmeticOperator(currentChar)){
					this.backIndex();
					return new Token(lexeme.toString(), Token.TYPE_ARITHMETIC_OPERATOR);
				} else if (this.isAArithmeticOperator(currentChar)){
					if(isArithmeticOperatorEquals(currentChar, lexeme) && counter<2) {
						lexeme.append(currentChar);
						counter++;
					} else {
						lexeme.append(currentChar);
						throw new RuntimeException("Error: arithmetic operators are not recognized \"" + lexeme.toString() + "\"");
					}
				} 
				break;
			case 99:
				return new Token(lexeme.toString(), Token.TYPE_END_OF_CODE);
				
			}
		}
		return checkIfLexemaIsNotEmpty(token, currentChar, lexeme);
	}

	private int initialStateMachine(char currentChar, int estado, StringBuffer lexeme) {
		if (isBlankSpace(currentChar)) {
			estado = 0;
		} else if (isLetterOrUnderscore(currentChar)) {
			lexeme.append(currentChar);
			estado = 1;
		} else if (this.isDigit(currentChar)) {
			lexeme.append(currentChar);
			estado = 2;
		} else if (isASpecialCharacter(currentChar)) {
			lexeme.append(currentChar);
			estado = 5;
		} else if (currentChar == '$') {
			lexeme.append(currentChar);
			estado = 99;
			this.backIndex();
		} else if (isAssignmentOperator(currentChar)) {
			lexeme.append(currentChar);
			estado = 6;
		} else if (isChar(currentChar)) {
			lexeme.append(currentChar);
			estado = 7;
		} else if (isRelationalOperator(currentChar)) {
			lexeme.append(currentChar);
			estado = 9;
		} else if (isAArithmeticOperator(currentChar)){
			lexeme.append(currentChar);
			estado = 11;
        } else {
			lexeme.append(currentChar);
			throw new RuntimeException("Erro: token invalido \"" + lexeme.toString() + "\"");
		}
		return estado;
	}

	private Token checkIfLexemaIsNotEmpty(Token token, char currentChar, StringBuffer lexeme) {
        if(lexeme.length() != 0){
            return returnCurrentToken(currentChar, lexeme);
        } else {
            return token;
        }
    }

	private boolean isLetter(char c) {
		return ((c >= 'a') && (c <= 'z') || (c >= 'A') && (c <= 'Z'));
	}

	private boolean isDigit(char c) {
		return (c >= '0') && (c <= '9');
	}
	
	private boolean isAssignmentOperator(char c) {
		return c == '=';
	}
	
	private boolean isChar(char c) {
		String aux = "'";
		return aux.charAt(0) == c;
	}
	
	private boolean isPartOfChar (char c, StringBuffer lex) {
		if (!((this.isDigit(c) || this.isLetter(c)) && lex.length() == 1) 
				&& !(c == "'".charAt(0) && lex.length() == 2)) {
			return false;
		}
		lex.append(c);
		return true;
	}

	private boolean isPartOfAnIdentifier(char c) {
		return this.isLetter(c) || this.isDigit(c) || c == '_';
	}

	private boolean isASpecialCharacter(char c) {
		return c == ')' || c == '(' || c == '{' || c == '}' || c == ',' || c == ';' || c == ':' || c == '.';
	}

	private boolean isLetterOrUnderscore(char c) {
		return this.isLetter(c) || c == '_';
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

	private boolean isPartOfAnRelationalOperator(char currentChar, int estado, StringBuffer lexeme) throws Exception{
		if (currentChar == '=' && lexeme.length() < 2) {
			lexeme.append(currentChar);
			return true;
		}
		this.backIndex();
		return false;
	}

	private boolean isArithmeticOperatorEquals(char currentChar, StringBuffer lexeme) {
		char [] lexemaCharArray = lexeme.toString().toCharArray();
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
	
	public int getIndiceConteudo() {
		return contentIndex;
	}

	public void setIndiceConteudo(int contentIndex) {
		this.contentIndex = contentIndex;
	}
	
	private Token returnCurrentToken(char currentChar, StringBuffer lexeme) {
		if (isLetterOrUnderscore(currentChar)) {
			return new Token(lexeme.toString(), Token.TYPE_IDENTIFIER);
	   } else if (this.isDigit(currentChar)) {
		   return new Token(lexeme.toString(), Token.TYPE_INT);
	   } else if (isASpecialCharacter(currentChar)) {
		   return new Token(lexeme.toString(), Token.TYPE_SPECIAL_CHARACTER);
	   } else if (currentChar == '$') {
		   return new Token(lexeme.toString(), Token.TYPE_END_OF_CODE);
	   } else if (isAssignmentOperator(currentChar)) {
		   return new Token(lexeme.toString(), Token.TYPE_ASSIGNMENT_OPERATOR);
	   } else if (isRelationalOperator(currentChar)) {
		   return new Token(lexeme.toString(), Token.TYPE_RELATIONAL_OPERATOR);
	   } else if (isAArithmeticOperator(currentChar)) {
		   return new Token(lexeme.toString(), Token.TYPE_ARITHMETIC_OPERATOR);
	   }  else {
		   lexeme.append(currentChar);
		   throw new RuntimeException("Erro: token invalido \"" + lexeme.toString() + "\"");
	   }
	}
	
}
