package compiladorl3;

import semantic.Semantic;

public class CompiladorL3 {
	private Lexico lexico;
    private Sintatico sintatico;
    private Semantic semantic;

    public void runLexico(String codeFilePath) throws Exception {
        this.lexico = new Lexico(codeFilePath);

        Token t = null;
        while((t = this.lexico.nextToken()) != null){
            System.out.println(t.toString());
        }
        this.lexico.setIndiceConteudo(0);
    }
    
    public void runSintatic(String codeFilePath) throws Exception {
        this.lexico = new Lexico(codeFilePath);
        this.sintatico = new Sintatico(this.lexico);

        sintatico.s();
    }

    public void runSemantic(String codeFilePath) throws Exception {
		this.lexico = new Lexico(codeFilePath);
		this.semantic = new Semantic();
        
        Token token = this.lexico.nextToken();
        token = this.lexico.nextToken();
		token = this.lexico.nextToken();
		do {
			if (token.getTipo() != Token.TIPO_FIM_CODIGO)
				semanticAnalize(token);
			
			//System.out.println(token.toString());
			token = this.lexico.nextToken();
		} while (token != null);
		
		this.lexico.setIndiceConteudo(0);


    }

    private void semanticAnalize(Token token) throws Exception {
		if((token.getTipo() != 7 && !token.getLexema().equals("(") &&
				!token.getLexema().equals(")")) || token.getLexema().equals("int") ||
				token.getLexema().equals("char") || token.getLexema().equals("float")){
			this.semantic.runSemantic(token);
		}
	}
    
    public Lexico getLexico() {
		return this.lexico;
	}

    public Sintatico getSintatico() {
        return this.sintatico;
    }

}
