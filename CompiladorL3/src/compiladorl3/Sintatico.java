package compiladorl3;

public class Sintatico {
    private Lexico lexicalAnalyzer;
    private Token token;

    public Sintatico(Lexico lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void s() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_INT)) {
            throw new RuntimeException("Iih rapaz! Cadê o tipo de retorno do main?");
        } else {
            this.token = lexicalAnalyzer.nextToken();
            if(!this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_MAIN)) {
                throw new RuntimeException("Iih rapaz! Cadê o main?");
            }
        }

        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexema().equals("(")) {
            throw new RuntimeException("Ai você me quebra! Abre o parênteses do main.");
        } else {
            this.token = lexicalAnalyzer.nextToken();
            if(!this.token.getLexema().equals(")")) {
                throw new RuntimeException("Ai você me quebra! Fecha o parênteses do main.");
            }
        }

        this.token = lexicalAnalyzer.nextToken();

        this.block();

        if(this.token.getTipo() == Token.TIPO_FIM_CODIGO) {
            System.out.println("Boa minha fera! Escreveu o código certinho, botou pra lascar!");
        } else {
            throw new RuntimeException("Lascou! Deu bronca logo perto do fim do programa :(");
        }
    }

    private void block() throws Exception {
        if(!this.token.getLexema().equals("{")) {
            throw new RuntimeException("Como você é burro cara! Abre as chaves do método.");
        }

        this.token = this.lexicalAnalyzer.nextToken();
        
        this.InvalidMethod();
        this.goToDeclaretionOrComand();
        
        if(!this.token.getLexema().equals("}")) {
            throw new RuntimeException("Como você é burro cara! Fecha as chaves do método.");
        }

        this.token = this.lexicalAnalyzer.nextToken();
    }

    private void InvalidMethod() {
		if(this.token.getLexema().equals("=")) {
			throw new RuntimeException("Lascou! Qual é o identificador bença?");
		} else if(this.token.getLexema().equals("(")) {
			throw new RuntimeException("Cade a palavra reservada da condicional pra começar bença?");
		}
	}

	private void command() throws Exception {
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getLexema().equals("{")){
            this.basicCommand();
        } else if(this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_WHILE)) {
            this.iteration();
        } else if(this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_IF)) {
            this.token = lexicalAnalyzer.nextToken();

            if(!this.token.getLexema().equals("(")) {
                throw new RuntimeException("Ei comparça! Bora, abre o parênteses do if.");
            }
            
            this.relationalExpression();

            if(!this.token.getLexema().equals(")")) {
                throw new RuntimeException("Ei comparça! Bora, fecha o parênteses do if.");
            }

            this.token = lexicalAnalyzer.nextToken();
            
            if(!this.token.getLexema().equals("{")) {
                throw new RuntimeException("Ei comparça! Bora, abre o '{' do if.");
            }
           
            if(!isComand()) {
                throw new RuntimeException("Poxa comparça! Estava esperando você declara algum comando pertinho de "+token.getLexema());
            }

            this.command(); 

            if(!this.token.getLexema().equals("else")) {
                throw new RuntimeException("Ei comparça, cade o ELSE do teu if?");
            }

            this.token = lexicalAnalyzer.nextToken();
            
            if(!this.token.getLexema().equals("{")) {
                throw new RuntimeException("Ei comparça! Bora, abre o '{' do else.");
            }

            if(!isComand()) {
                throw new RuntimeException("Poxa comparça! Estava esperando você declara algum comando pertinho de "+this.token.getLexema());
            }

            this.command();
        } else {
            throw new RuntimeException("Poxa comparça! Estava esperando você declara algum comando pertinho de "+this.token.getLexema());
        }
    }

    private void goToDeclaretionOrComand() throws Exception {
        if(ReservedWorld.isReservedWorld(this.token.getLexema()) || this.token.getLexema().equals("{") || isComand()) {

            if(this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_RETURN)) {
                this.token = lexicalAnalyzer.nextToken();

                if(this.token.getLexema().equals("0")) {
                    this.token = lexicalAnalyzer.nextToken();

                    if(this.token.getLexema().equals(";")) {
                        this.token = lexicalAnalyzer.nextToken();
                    }
                }
                return;
            }

            this.declaretaionOrComand();
            this.goToDeclaretionOrComand();
        }
    }

    private void declaretaionOrComand() throws Exception {
        if(ReservedWorld.isType(this.token.getLexema())) {
            this.declarationVariables();
        } else if (isComand()) {
            this.command();
        } else {
            throw new RuntimeException("Lamentavel! Estava esperando tu declarar um comando pertinho de "+this.token.getLexema());
        }
    }

    private boolean isComand() {
        return this.token.getTipo() == Token.TIPO_IDENTIFICADOR 
                || this.token.getLexema().equals("{") 
                || this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_WHILE)
                || this.token.getLexema().equals(ReservedWorld.RESERVEDWORLD_IF);
    }
    
    private void basicCommand() throws Exception {
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
            this.assignment();
        } else if (this.token.getLexema().equals("{")) {
            this.block();
        } else {
            throw new RuntimeException("Ei bença, complicado em! Cade o identificador ou { para iniciar o bloco?");
        }
    }

    private void iteration() throws Exception {
        if(!this.token.getLexema().equalsIgnoreCase(ReservedWorld.RESERVEDWORLD_WHILE)) {
            throw new RuntimeException("Cade o while pra começar bença?");
        }

        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexema().equalsIgnoreCase("(")) {
            throw new RuntimeException("Ô Bença coloca o ( para iniciar a expreção relacional do while");
        }
        
        this.relationalExpression();

        if(!this.token.getLexema().equalsIgnoreCase(")")) {
            throw new RuntimeException("Ô Bença coloca o ) para finalizar a expreção relacional do while");
        }

        this.token = lexicalAnalyzer.nextToken();

        this.command();
    }

    private void assignment() throws Exception {
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
            throw new RuntimeException("Lascou! Qual é o identificador bença?");
        }
        
        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getTipo() != Token.TIPO_OPERADOR_DE_ATRIBUICAO) {
            throw new RuntimeException("Lascou! Cade o operador de atribuição bença?");
        }
        
        this.token = lexicalAnalyzer.nextToken();

        if(!isValidTerm() && !this.token.getLexema().equals("(")) {
        	throw new RuntimeException("Sim, essa expressao vai receber o que? coloca o valor bença!");
        }

        this.arithmeticExpression();
        
        if(!this.token.getLexema().equals(";")) { 
            throw new RuntimeException("Finaliza a atribuição ae bença, coloca o ';'");
        }
        
        this.token = lexicalAnalyzer.nextToken();
    }

    private void Type() throws Exception {
        if(ReservedWorld.isType(this.token.getLexema())) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("Cadê o valor que será atribuído bença?");
        }
    }

    public void declarationVariables() throws Exception {
        this.Type();

        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Cade o identificador bença?");
        }

        this.token = lexicalAnalyzer.nextToken();

        // if(token.getLexema().equalsIgnoreCase(",")){
        //     this.declarationVariables();
        // }

        if(!this.token.getLexema().equalsIgnoreCase(";")) {
            throw new RuntimeException("Eeeeeeeeei bença? Tu vai finalizar a declação de variavel não?");
        }

        this.token = lexicalAnalyzer.nextToken();
    }

    private void relationalExpression() throws Exception {
        if (this.token.getLexema().equals("(") || isValidTerm()) {
        	this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpression();
        } else {
            throw new RuntimeException("Rapaz, isso aqui nao e uma expressao aritmetica nao visse..."); 
        }

        if(this.token.getTipo() != Token.TIPO_OPERADOR_RELACIONAL) {
            throw new RuntimeException("Erro, relaciona ai o que foi! Ta faltando o operador relacional mermao...");
        }

        this.token = lexicalAnalyzer.nextToken();

        if (!(isValidTerm()) && !(this.token.getLexema().equals("("))) {
        	throw new RuntimeException("Rapaz, isso aqui nao e uma expressao aritmetica nao visse...");
        } 
        
        this.arithmeticExpression();
    }

    private void arithmeticExpression() throws Exception {
        this.arithmeticExpressionTerm();
        this.arithmeticExpressionDerivated();
    }

    private void arithmeticExpressionDerivated() throws Exception {
        //Modified for @aquiles
        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            this.arithmeticExpressionOperator();
            this.arithmeticExpressionTerm();
            this.arithmeticExpressionDerivated();
        }
        //else nothing will happen, just return
    }

    private void arithmeticExpressionTerm() throws Exception {
        if(this.token.getLexema().equals("(")) {
            this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpressionWithParentesis();
        } else if (isValidTerm()) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("Ta de comedia, ne pirra? Ta faltando um termo valido (id, int, float ou char) nessa expressao");
        }
    }

    private boolean isValidTerm() {
        return this.token.getTipo() == Token.TIPO_IDENTIFICADOR || 
                this.token.getTipo() == Token.TIPO_INTEIRO ||
                this.token.getTipo() == Token.TIPO_REAL ||
                this.token.getTipo() == Token.TIPO_CHAR;
    }

    private void arithmeticExpressionWithParentesis() throws Exception {
        this.arithmeticExpression();

        if(!this.token.getLexema().equals(")")){
            throw new RuntimeException("Ai você me quebra! Fecha o parênteses perto da expressao aritmetica");
        }

        this.token = lexicalAnalyzer.nextToken();
    }

    private void arithmeticExpressionOperator() throws Exception {
        //Modified for @aquilesR
        if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("Ei boy, tiracao! Ta faltando um operador aritmetico (+,-,/,*) nessa expressao");
        }
    }

}