package lang.c;

import org.junit.Test;

import lang.c.testhelpter.CTokenizerTestHelper;

public class T00_21CTokenizerTest {

    CTokenizerTestHelper helper = new CTokenizerTestHelper();

    @Test
    public void decimalNumber() {
        String testString = "100";
        CToken[] exceptedTokenList = {
            new CToken(CToken.TK_NUM, 1, 1, "100"),
            new CToken(CToken.TK_EOF, 1, 4, "end_of_file")
        };
        helper.resetEnvironment();
        helper.acceptList(testString, exceptedTokenList);
    }

    @Test
    public void decimalNumber2() {
        String testString = "100";
        CToken exceptedToken = new CToken(CToken.TK_NUM, 1, 1, "100");
        helper.resetEnvironment();
        helper.acceptNumber(testString, exceptedToken, 100);
    }

    @Test
    public void plusNumber() {
        String testString = "+100";
        CToken[] exceptedTokenList = {
            new CToken(CToken.TK_PLUS, 1, 1, "+"),
            new CToken(CToken.TK_NUM, 1, 2, "100"),
            new CToken(CToken.TK_EOF, 1, 5, "end_of_file")
        };
        helper.acceptList(testString, exceptedTokenList);
    }

    @Test
    public void expression2() {
        String testString = "13 + 7 + 2";
        CToken[] exceptedTokenList = {
            new CToken(CToken.TK_NUM, 1, 1, "13"),
            new CToken(CToken.TK_PLUS, 1, 4, "+"),
            new CToken(CToken.TK_NUM, 1, 6, "7"),
            new CToken(CToken.TK_PLUS, 1, 8, "+"),
            new CToken(CToken.TK_NUM, 1, 10, "2"),
            new CToken(CToken.TK_EOF, 1, 11, "end_of_file")
        };
        helper.acceptList(testString, exceptedTokenList);
    }
}
