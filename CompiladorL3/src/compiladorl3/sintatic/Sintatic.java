package compiladorl3.sintatic;

import compiladorl3.lexical.Lexical;
import compiladorl3.lexical.ReservedWorld;
import compiladorl3.lexical.Token;

public class Sintatic {

    private Lexical lexicalAnalyzer;
    private Token token;

    public Sintatic(Lexical lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void analyzeProgram() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_INT)) {
            throw new RuntimeException("Iih rapaz! Cadê o tipo de retorno do main?");
        } else {
            this.token = lexicalAnalyzer.nextToken();
            if(!this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_MAIN)) {
                throw new RuntimeException("Iih rapaz! Cadê o main?");
            }
        }

        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexeme().equals("(")) {
            throw new RuntimeException("Ai você me quebra! Abre o parênteses do main.");
        } else {
            this.token = lexicalAnalyzer.nextToken();
            if(!this.token.getLexeme().equals(")")) {
                throw new RuntimeException("Ai você me quebra! Fecha o parênteses do main.");
            }
        }

        this.token = lexicalAnalyzer.nextToken();

        this.block();

        if(this.token.getType() == Token.TYPE_END_OF_CODE) {
            System.out.println("Boa minha fera! Escreveu o código certinho, botou pra lascar!");
        } else {
            throw new RuntimeException("Lascou! Deu bronca logo perto do fim do programa :(");
        }
    }

    private void block() throws Exception {
        if(!this.token.getLexeme().equals("{")) {
            throw new RuntimeException("Como você é burro cara! Abre as chaves do método.");
        }

        this.token = this.lexicalAnalyzer.nextToken();
        
        this.InvalidMethod();
        this.goToDeclarationOrComand();
        
        if(!this.token.getLexeme().equals("}")) {
            throw new RuntimeException("Como você é burro cara! Fecha as chaves do método.");
        }

        this.token = this.lexicalAnalyzer.nextToken();
    }

    private void InvalidMethod() {
		if(this.token.getLexeme().equals("=")) {
			throw new RuntimeException("Lascou! Qual é o identificador bença?");
		} else if(this.token.getLexeme().equals("(")) {
			throw new RuntimeException("Cade a palavra reservada da condicional pra começar bença?");
		}
	}

	private void command() throws Exception {
        if(this.token.getType() == Token.TYPE_IDENTIFIER || this.token.getLexeme().equals("{")){
            this.basicCommand();
        } else if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_WHILE)) {
            this.iteration();
        } else if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_IF)) {
            this.token = lexicalAnalyzer.nextToken();

            if(!this.token.getLexeme().equals("(")) {
                throw new RuntimeException("Ei comparça! Bora, abre o parênteses do if.");
            }
            
            this.relationalExpression();

            if(!this.token.getLexeme().equals(")")) {
                throw new RuntimeException("Ei comparça! Bora, fecha o parênteses do if.");
            }

            this.token = lexicalAnalyzer.nextToken();
            
            if(!this.token.getLexeme().equals("{")) {
                throw new RuntimeException("Ei comparça! Bora, abre o '{' do if.");
            }
           
            if(!isComand()) {
                throw new RuntimeException("Poxa comparça! Estava esperando você declara algum comando pertinho de "+token.getLexeme());
            }

            this.command(); 

            if(!this.token.getLexeme().equals("else")) {
                throw new RuntimeException("Ei comparça, cade o ELSE do teu if?");
            }

            this.token = lexicalAnalyzer.nextToken();
            
            if(!this.token.getLexeme().equals("{")) {
                throw new RuntimeException("Ei comparça! Bora, abre o '{' do else.");
            }

            if(!isComand()) {
                throw new RuntimeException("Poxa comparça! Estava esperando você declara algum comando pertinho de "+this.token.getLexeme());
            }

            this.command();
        } else {
            throw new RuntimeException("Poxa comparça! Estava esperando você declara algum comando pertinho de "+this.token.getLexeme());
        }
    }

    private void goToDeclarationOrComand() throws Exception {
        if(ReservedWorld.isReservedWorld(this.token.getLexeme()) || this.token.getLexeme().equals("{") || isComand()) {

            if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_RETURN)) {
                this.token = lexicalAnalyzer.nextToken();

                if(this.token.getLexeme().equals("0")) {
                    this.token = lexicalAnalyzer.nextToken();

                    if(this.token.getLexeme().equals(";")) {
                        this.token = lexicalAnalyzer.nextToken();
                    }
                }
                return;
            }

            this.declarationOrComand();
            this.goToDeclarationOrComand();
        }
    }

    private void declarationOrComand() throws Exception {
        if(ReservedWorld.isType(this.token.getLexeme())) {
            this.declarationVariables();
        } else if (isComand()) {
            this.command();
        } else {
            throw new RuntimeException("Lamentavel! Estava esperando tu declarar um comando pertinho de "+this.token.getLexeme());
        }
    }

    private boolean isComand() {
        return this.token.getType() == Token.TYPE_IDENTIFIER 
                || this.token.getLexeme().equals("{") 
                || this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_WHILE)
                || this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_IF);
    }
    
    private void basicCommand() throws Exception {
        if(this.token.getType() == Token.TYPE_IDENTIFIER) {
            this.assignment();
        } else if (this.token.getLexeme().equals("{")) {
            this.block();
        } else {
            throw new RuntimeException("Ei bença, complicado em! Cade o identificador ou { para iniciar o bloco?");
        }
    }

    private void iteration() throws Exception {
        if(!this.token.getLexeme().equalsIgnoreCase(ReservedWorld.RESERVEDWORLD_WHILE)) {
            throw new RuntimeException("Cade o while pra começar bença?");
        }

        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexeme().equalsIgnoreCase("(")) {
            throw new RuntimeException("Ô Bença coloca o ( para iniciar a expreção relacional do while");
        }
        
        this.relationalExpression();

        if(!this.token.getLexeme().equalsIgnoreCase(")")) {
            throw new RuntimeException("Ô Bença coloca o ) para finalizar a expreção relacional do while");
        }

        this.token = lexicalAnalyzer.nextToken();

        this.command();
    }

    private void assignment() throws Exception {
        if(this.token.getType() != Token.TYPE_IDENTIFIER) {
            throw new RuntimeException("Lascou! Qual é o identificador bença?");
        }
        
        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getType() != Token.TYPE_ASSIGNMENT_OPERATOR) {
            throw new RuntimeException("Lascou! Cade o operador de atribuição bença?");
        }
        
        this.token = lexicalAnalyzer.nextToken();

        if(!isValidTerm() && !this.token.getLexeme().equals("(")) {
        	throw new RuntimeException("Sim, essa expressao vai receber o que? coloca o valor bença!");
        }

        this.arithmeticExpression();
        
        if(!this.token.getLexeme().equals(";")) { 
            throw new RuntimeException("Finaliza a atribuição ae bença, coloca o ';'");
        }
        
        this.token = lexicalAnalyzer.nextToken();
    }

    private void Type() throws Exception {
        if(ReservedWorld.isType(this.token.getLexeme())) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("Cadê o valor que será atribuído bença?");
        }
    }

    public void declarationVariables() throws Exception {
        this.Type();

        if(this.token.getType() != Token.TYPE_IDENTIFIER){
            throw new RuntimeException("Cade o identificador bença?");
        }

        this.token = lexicalAnalyzer.nextToken();

        // if(token.getLexema().equalsIgnoreCase(",")){
        //     this.declarationVariables();
        // }
        
        if(this.token.getType() == Token.TYPE_ASSIGNMENT_OPERATOR) {
            this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpression();
        }

        if(!this.token.getLexeme().equalsIgnoreCase(";")) {
            throw new RuntimeException("Eeeeeeeeei bença? Tu vai finalizar a declação de variavel não? "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();
    }

    private void relationalExpression() throws Exception {
        if (this.token.getLexeme().equals("(") || isValidTerm()) {
        	this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpression();
        } else {
            throw new RuntimeException("Rapaz, isso aqui nao e uma expressao aritmetica nao visse..."); 
        }

        if(this.token.getType() != Token.TYPE_RELATIONAL_OPERATOR) {
            throw new RuntimeException("Erro, relaciona ai o que foi! Ta faltando o operador relacional mermao...");
        }

        this.token = lexicalAnalyzer.nextToken();

        if (!(isValidTerm()) && !(this.token.getLexeme().equals("("))) {
        	throw new RuntimeException("Rapaz, isso aqui nao e uma expressao aritmetica nao visse...");
        } 
        
        this.arithmeticExpression();
    }

    private void arithmeticExpression() throws Exception {
        this.arithmeticExpressionTerm();
        this.arithmeticExpressionDerivated();
    }

    private void arithmeticExpressionDerivated() throws Exception {
        //Modified by @aquiles
        if (this.token.getType() == Token.TYPE_ARITHMETIC_OPERATOR) {
            this.arithmeticExpressionOperator();
            this.arithmeticExpressionTerm();
            this.arithmeticExpressionDerivated();
        }
        //else nothing will happen, just return
    }

    private void arithmeticExpressionTerm() throws Exception {
        if(this.token.getLexeme().equals("(")) {
            this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpressionWithParentesis();
        } else if (isValidTerm()) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("Ta de comedia, ne pirra? Ta faltando um termo valido (id, int, float ou char) nessa expressao");
        }
    }

    private boolean isValidTerm() {
        return this.token.getType() == Token.TYPE_IDENTIFIER || 
                this.token.getType() == Token.TYPE_INT ||
                this.token.getType() == Token.TYPE_FLOAT ||
                this.token.getType() == Token.TYPE_CHAR;
    }

    private void arithmeticExpressionWithParentesis() throws Exception {
        this.arithmeticExpression();

        if(!this.token.getLexeme().equals(")")){
            throw new RuntimeException("Ai você me quebra! Fecha o parênteses perto da expressao aritmetica");
        }

        this.token = lexicalAnalyzer.nextToken();
    }

    private void arithmeticExpressionOperator() throws Exception {
        //Modified by @aquilesR
        if (this.token.getType() == Token.TYPE_ARITHMETIC_OPERATOR) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("Ei boy, tiracao! Ta faltando um operador aritmetico (+,-,/,*) nessa expressao");
        }
    }

}