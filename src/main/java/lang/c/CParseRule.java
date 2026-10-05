package lang.c;

import java.util.HashMap;

import lang.*;

public abstract class CParseRule extends ParseRule<CParseContext> implements lang.Compiler<CParseContext>, LL1<CToken> {
	static private int ID = 0;  // Sequential ID Management for All CParseRule
	// Class-based sequential ID management
	static private HashMap<String,Integer> IDMAP = new HashMap<String,Integer>();    
	private String name;        // For non-terminal symbol name reservation
	private int id;             // Number of newly created CParseRule instances
	private String BNF_LEFT;    // Non-terminating symbol name for this contact (BFN left)
	private String BNF_RIGHT;   // This contact conversion rule (BFN right)
	private CType ctype;        // The (presumed) type of this node
	private boolean isConstant; // Does this node represent a constant?

	public CParseRule() {
		ID++;
		id = ID;
	}

	public CParseRule(String name) {
		int r=1;
		if (IDMAP.containsKey(name)) {
			r=IDMAP.get(name)+1;
		}
		IDMAP.put(name,r);
		id = r;
		this.name = name;
	}

	public String getBNF() {
		if (BNF_LEFT != null && BNF_RIGHT != null)
			return BNF_LEFT + " ::= " + BNF_RIGHT;
		if (BNF_LEFT != null)
			return BNF_LEFT;
		if (BNF_RIGHT != null)
			return BNF_RIGHT;
		return "";
	}

	public String getBNF(int id) {
		return BNF_LEFT + id + " ::= " + BNF_RIGHT;
	}

	public void setBNF(String left, String right) {
		this.BNF_LEFT  = left;
		this.BNF_RIGHT = right;
	}

	public void setBNF(String bnf) {
		String[] b = bnf.split("\s*::=\s*");
		if (b.length > 1) {
			this.BNF_LEFT  = b[0];
			this.BNF_RIGHT = b[1];
		} else {
			this.BNF_LEFT = bnf;
		}
	}

	public int getId() {
		return id;
	}

	public CType getCType() {
		return ctype;
	}

	public int getType() {
		return ctype.getType();
	}

	public void setCType(CType ctype) {
		this.ctype = ctype;
	}

	public void setCType(int type) {
		this.ctype = CType.getCType(type);
	}

	public void setConstant(boolean isConstant) {
		this.isConstant = isConstant;
	}

	public boolean isConstant() {
		return isConstant;
	}

	public String getClassName() {
		return name;
	}

	public String getMethodName() {
		return Thread.currentThread().getStackTrace()[1].getMethodName();
	}
}
