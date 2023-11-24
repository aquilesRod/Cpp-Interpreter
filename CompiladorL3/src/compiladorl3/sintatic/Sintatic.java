package compiladorl3.sintatic;

import compiladorl3.lexical.Lexical;
import compiladorl3.lexical.ReservedWorld;
import compiladorl3.lexical.Token;

public class Sintatic {

    private Lexical lexicalAnalyzer;
    private Token token;
    private String functionReturnStatement;

    public Sintatic(Lexical lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
    }

    public void analyzeProgram() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        while(ReservedWorld.isFunctionType(this.token.getLexeme())) {
            this.functionReturnStatement = this.token.getLexeme();
            this.declaration();
        }

        if(this.token.getType() == Token.TYPE_END_OF_CODE) {
            System.out.println("No sintatic problem was found!");
        } else {
            throw new RuntimeException("The token '$' is expected to finalize your program");
        }
    }

    private void declaration() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if (!(this.token.getType() == Token.TYPE_IDENTIFIER) && !(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_MAIN))) {
            System.out.println("[Error]: The identifier of the declaration was not found");
        }

        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getLexeme().equals(";") || this.token.getLexeme().equals("=")) {
            this.variableDeclaration();
        } else if(this.token.getLexeme().equals("(")) {
            this.functionDeclaration();
        } else {
            // maybe a class?
        }
    }


    private void variableDeclaration() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getType() != Token.TYPE_IDENTIFIER) {
            throw new RuntimeException("[Error]: A identifier is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getLexeme().equalsIgnoreCase(",")){
            this.variableDeclaration();
            return;
        } else if(this.token.getType() == Token.TYPE_ASSIGNMENT_OPERATOR) {
            this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpression();
        }

        if(!this.token.getLexeme().equals(";")) {
            throw new RuntimeException("[Error]: The ';' is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();
    }

    private void functionDeclaration() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        this.parameter();

        if(!this.token.getLexeme().equals(")")) {
            throw new RuntimeException("[Error]: A '(' is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        this.block(true);
    }

    private void parameter() throws Exception {
        if(ReservedWorld.isFunctionType(this.token.getLexeme())){
            this.token = lexicalAnalyzer.nextToken();

            if(this.token.getType() == Token.TYPE_IDENTIFIER) {
                this.parametersList();
            } else {
                throw new RuntimeException("[Error]: An identifier is expected before "+this.token.getLexeme());
            }
        }
    }

    private void parametersList() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getLexeme().equals(",")) {
            this.token = lexicalAnalyzer.nextToken();

            if(ReservedWorld.isFunctionType(this.token.getLexeme())) {
                this.parameter();
            } else {
                throw new RuntimeException("[Error]: An parameter type is expected before "+this.token.getLexeme());
            }
        }
    }

    private void block(boolean isFunctionBlock) throws Exception {
        if(!this.token.getLexeme().equals("{")) {
            throw new RuntimeException("[Error]: The token '{' is expected before "+this.token.getLexeme());
        }

        this.token = this.lexicalAnalyzer.nextToken();
        
        while (ReservedWorld.isFunctionType(this.token.getLexeme()) || isComand()) {
            this.declarationOrComand();
        }

        if (isFunctionBlock) {
            this.validateReturnStatement();
        }
        
        if(!this.token.getLexeme().equals("}")) {
            throw new RuntimeException("[Error]: The token '}' is expected before "+this.token.getLexeme());
        }

        this.token = this.lexicalAnalyzer.nextToken();
    }

    private void validateReturnStatement() throws Exception {
        if(functionReturnStatement.equals(ReservedWorld.RESERVEDWORLD_VOID)) {
            if (this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_RETURN)) {
                this.token = lexicalAnalyzer.nextToken();
                if (!this.token.getLexeme().equals(";")) {
                    throw new RuntimeException("[Error]: The token ';' is expected before "+this.token.getLexeme());
                }
            }
        } else {
            if (!this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_RETURN)) {
                throw new RuntimeException("[Error]: A 'return' statement is expected before "+this.token.getLexeme());
            }
            this.token = lexicalAnalyzer.nextToken();

            this.arithmeticExpression();

            if (!this.token.getLexeme().equals(";")) {
                throw new RuntimeException("[Error]: The token ';' is expected before "+this.token.getLexeme());
            }
            this.token = this.lexicalAnalyzer.nextToken();
        }
    }

    private void declarationOrComand() throws Exception {
        if(ReservedWorld.isFunctionType(this.token.getLexeme())) {
            this.variableDeclaration();
        } else if (isComand()) {
            this.command();
        } else {
            throw new RuntimeException("[Error]: The token '"+this.token.getLexeme()+"' is unexpected");
        }
    }

    private void command() throws Exception {
        if(this.token.getType() == Token.TYPE_IDENTIFIER){
            this.assignmentOrFunctionCall();
        } else if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_WHILE)) {
            this.iterationCommand();
        } else if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_IF)) {
            this.conditionalCommand();
        } else if(this.token.getLexeme().equals("{")) {
            this.block(false);
        } else {
            throw new RuntimeException("[Error]: No command interpreted before "+this.token.getLexeme());
        }
    }

    private void assignmentOrFunctionCall() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getType() == Token.TYPE_ASSIGNMENT_OPERATOR) {
            this.assignmentCommand();
        } else if (this.token.getLexeme().equals("(")){
            this.functionCall();
        } else {
            throw new RuntimeException("[Error]: Unexpected token '"+this.token.getLexeme()+"'");
        }
    }

    private void assignmentCommand() throws Exception {
        if(this.token.getType() != Token.TYPE_ASSIGNMENT_OPERATOR) {
            throw new RuntimeException("[Error]: The token '=' is expected before "+this.token.getLexeme());
        }
        
        this.token = lexicalAnalyzer.nextToken();

        if(!isValidTerm() && !this.token.getLexeme().equals("(")) {
        	throw new RuntimeException("[Error]: A value must be provided before "+this.token.getLexeme());
        }

        this.arithmeticExpression();
        
        if(!this.token.getLexeme().equals(";")) { 
            throw new RuntimeException("[Error]: The token ';' is expected before "+this.token.getLexeme());
        }
        
        this.token = lexicalAnalyzer.nextToken();
    }

    private void iterationCommand() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexeme().equalsIgnoreCase("(")) {
            throw new RuntimeException("[Error]: The token '(' is expected before "+this.token.getLexeme());
        }
        
        this.relationalExpression();

        if(!this.token.getLexeme().equalsIgnoreCase(")")) {
            throw new RuntimeException("[Error]: The token ')' is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        this.block(false);
    }

    private void conditionalCommand() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexeme().equals("(")) {
            throw new RuntimeException("[Error]: The token '(' is expected before "+this.token.getLexeme());
        }
        
        this.relationalExpression();

        if(!this.token.getLexeme().equals(")")) {
            throw new RuntimeException("[Error]: The token ')' is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        this.block(false); 

        if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_ELSE)) {
            this.token = lexicalAnalyzer.nextToken();
        
            if(this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_IF)) {
                this.conditionalCommand();
            } else {
                this.block(false);
            }
        }
    }

    private void functionCall() throws Exception {
        if(!isValidTerm() && !this.token.getLexeme().equals("(")) {
        	throw new RuntimeException("[Error]: A value must be provided before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        this.arguments();
        
        if(!this.token.getLexeme().equals(")")) { 
            throw new RuntimeException("[Error]: The token ')' is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        if(!this.token.getLexeme().equals(";")) { 
            throw new RuntimeException("[Error]: The token ';' is expected before "+this.token.getLexeme());
        }
    }

    private void arguments() throws Exception {
        if(isValidTerm()){
            this.argumentsList();
        }
    }

    private void argumentsList() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if(this.token.getLexeme().equals(",")) {
            this.token = lexicalAnalyzer.nextToken();

            if(isValidTerm()) {
                this.arguments();
            } else {
                throw new RuntimeException("[Error]: An argument is expected before "+this.token.getLexeme());
            }
        }
    }

    private void relationalExpression() throws Exception {
        this.token = lexicalAnalyzer.nextToken();

        if (this.token.getLexeme().equals("(") || isValidTerm()) {
            this.arithmeticExpressionTerm();
        } else {
            throw new RuntimeException("[Error]: A term is expected before "+this.token.getLexeme());
        }

        if(this.token.getType() != Token.TYPE_RELATIONAL_OPERATOR) {
            throw new RuntimeException("[Error]: A conditional operator is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();

        if (!(isValidTerm()) && !(this.token.getLexeme().equals("("))) {
        	throw new RuntimeException("[Error]: A term is expected before "+this.token.getLexeme());
        }
        
        this.arithmeticExpression();
    }

    private void arithmeticExpression() throws Exception {
        this.arithmeticExpressionTerm();
        this.arithmeticExpressionDerivated();
    }

    private void arithmeticExpressionDerivated() throws Exception {
        if (this.token.getType() == Token.TYPE_ARITHMETIC_OPERATOR) {
            this.arithmeticExpressionOperator();
            this.arithmeticExpressionTerm();
            this.arithmeticExpressionDerivated();
        }
    }

    private void arithmeticExpressionOperator() throws Exception {
        if (this.token.getType() == Token.TYPE_ARITHMETIC_OPERATOR) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("[Error]: An arithmetic operator is expected before "+this.token.getLexeme());
        }
    }

    private void arithmeticExpressionTerm() throws Exception {
        if(this.token.getLexeme().equals("(")) {
            this.token = lexicalAnalyzer.nextToken();
            this.arithmeticExpressionWithParentesis();
        } else if (this.token.getType() == Token.TYPE_IDENTIFIER) {
            this.token = lexicalAnalyzer.nextToken();

            if(this.token.getLexeme().equals("(")) {
                this.functionCall();
            }
        } else if (isValidTerm()) {
            this.token = lexicalAnalyzer.nextToken();
        } else {
            throw new RuntimeException("[Error]: A term is expected before "+this.token.getLexeme());
        }
    }

    private void arithmeticExpressionWithParentesis() throws Exception {
        this.arithmeticExpression();

        if(!this.token.getLexeme().equals(")")){
            throw new RuntimeException("[Error]: The token ')' is expected before "+this.token.getLexeme());
        }

        this.token = lexicalAnalyzer.nextToken();
    }

    private boolean isComand() {
        return this.token.getType() == Token.TYPE_IDENTIFIER 
                || this.token.getLexeme().equals("{") 
                || this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_WHILE)
                || this.token.getLexeme().equals(ReservedWorld.RESERVEDWORLD_IF);
    }
    
    private boolean isValidTerm() {
        return this.token.getType() == Token.TYPE_IDENTIFIER || 
                this.token.getType() == Token.TYPE_INT ||
                this.token.getType() == Token.TYPE_FLOAT ||
                this.token.getType() == Token.TYPE_CHAR;
    }
}