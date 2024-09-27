package lang.c;

import java.util.HashMap;

import lang.*;

public class CParseContext extends ParseContext {
	CodeGenCommon cgc;
	HashMap<String,Integer> seqHashMap = new HashMap<String,Integer>();
	public CParseContext(IOContext ioCtx,  CTokenizer tknz) {
		super(ioCtx, tknz);
		this.cgc = new CodeGenCommon(ioCtx.getOutStream());
	}

	@Override
	public CTokenizer getTokenizer()		{ return (CTokenizer) super.getTokenizer(); }

	public int getSeqId(String name) {
		int seq = 1;
		if (seqHashMap.containsKey(name)) {
			seq = seqHashMap.get(name) + 1;
		}
		seqHashMap.put(name, seq);
		return seq; 
	}
	public CodeGenCommon getCodeGenCommon() {
		return cgc;
	}
}
