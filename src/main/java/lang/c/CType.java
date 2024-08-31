package lang.c;

public class CType {
	public static final int T_err		= 0;		// 型エラー
	public static final int T_int		= 1;		// int
	public static final int T_pint		= 2;		// int*
	public static final int T_int_array	= 3;		// int[]
	public static final int T_pint_array= 4; 		// int*[]
	public static final int T_bool		= 5;		// bool

	private static CType[] typeArray = {
		new CType(T_err,	"error"),
		new CType(T_int,	"int"),
		new CType(T_pint,	"int*"),
		new CType(T_int_array, "int[]"),
		new CType(T_pint_array, "int*[]"),
		new CType(T_bool,	"bool"),
	};

	private int type;
	private String string;

	private CType(int type, String s) {
		this.type = type;
		this.string = s;
	}
	
	public static CType getCType(int type) {
		return typeArray[type];
	}

	public boolean isCType(int t) {
		return t == type;
	}
	
	public int getType() {
		return type;
	}

	public String toString(){
		return string;
	}
}
