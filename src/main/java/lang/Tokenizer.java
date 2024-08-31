package lang;

public abstract class Tokenizer<TKN extends Token, PCTX extends ParseContext> {
	public abstract TKN getCurrentToken(PCTX pcx); // 既に読み込まれている最新の字句を返す

	public abstract TKN getNextToken(PCTX pcx); // 新たに字句をひとつ読み込んで返す
}
