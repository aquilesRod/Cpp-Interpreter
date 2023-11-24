package compiladorl3;

import compiladorl3.lexical.Lexical;
import compiladorl3.lexical.Token;
import compiladorl3.sintatic.Sintatic;
import compiladorl3.semantic.Semantic;

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

        sintatic.analyzeProgram();
    }

    public void runSemantic(String codeFilePath) throws Exception {
		this.lexicon = new Lexical(codeFilePath);
		this.semantic = new Semantic();
        
        Token token = this.lexicon.nextToken();
        token = this.lexicon.nextToken();
		token = this.lexicon.nextToken();
        
		do {
			if (token.getType() != Token.TYPE_END_OF_CODE)
				semanticAnalize(token);
			
			//System.out.println(token.toString());
			token = this.lexicon.nextToken();
		} while (token != null);
		
		this.lexicon.setIndiceConteudo(0);
    }

    private void semanticAnalize(Token token) throws Exception {
		if((token.getType() != 7 && !token.getLexeme().equals("(") &&
				!token.getLexeme().equals(")")) || token.getLexeme().equals("int") ||
				token.getLexeme().equals("char") || token.getLexeme().equals("float")){
			this.semantic.runSemantic(token);
		}
	}
    
    public Lexical getLexicon() {
		return this.lexicon;
	}

    public Sintatic getSintatic() {
        return this.sintatic;
    }

}
