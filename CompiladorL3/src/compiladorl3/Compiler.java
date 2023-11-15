package compiladorl3;

import compiladorl3.lexical.Lexical;
import compiladorl3.lexical.Token;
import compiladorl3.sintatic.Sintatic;
import semantic.Semantic;

public class Compiler {
	private Lexical lexicon;
    private Sintatic sintatic;
    private Semantic semantic;

    public void runLexico(String codeFilePath) throws Exception {
        this.lexicon = new Lexical(codeFilePath);

        Token t = null;
        while((t = this.lexicon.nextToken()) != null){
            System.out.println(t.toString());
        }
        this.lexicon.setIndiceConteudo(0);
    }
    
    public void runSintatic(String codeFilePath) throws Exception {
        this.lexicon = new Lexical(codeFilePath);
        this.sintatic = new Sintatic(this.lexicon);

        sintatic.s();
    }

    public void runSemantic(String codeFilePath) throws Exception {
		this.lexicon = new Lexical(codeFilePath);
		this.semantic = new Semantic();
        
        Token token = this.lexicon.nextToken();
        token = this.lexicon.nextToken();
		token = this.lexicon.nextToken();
		do {
			if (token.getTipo() != Token.TIPO_FIM_CODIGO)
				semanticAnalize(token);
			
			//System.out.println(token.toString());
			token = this.lexicon.nextToken();
		} while (token != null);
		
		this.lexicon.setIndiceConteudo(0);
    }

    private void semanticAnalize(Token token) throws Exception {
		if((token.getTipo() != 7 && !token.getLexema().equals("(") &&
				!token.getLexema().equals(")")) || token.getLexema().equals("int") ||
				token.getLexema().equals("char") || token.getLexema().equals("float")){
			this.semantic.runSemantic(token);
		}
	}
    
    public Lexical getLexico() {
		return this.lexicon;
	}

    public Sintatic getSintatico() {
        return this.sintatic;
    }

}
