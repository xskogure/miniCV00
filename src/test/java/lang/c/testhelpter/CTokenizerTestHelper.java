package lang.c.testhelpter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;

import lang.IOContext;
import lang.InputStreamForTest;
import lang.PrintStreamForTest;
import lang.c.CParseContext;
import lang.c.CToken;
import lang.c.CTokenRule;
import lang.c.CTokenizer;

public class CTokenizerTestHelper {
    InputStreamForTest inputStream;
    PrintStreamForTest outputStream;
    PrintStreamForTest errorOutputStream;
    IOContext context;
    CTokenizer tokenizer;
    CParseContext cpContext;

    @Before
    public void setUp() {
        inputStream = new InputStreamForTest();
        outputStream = new PrintStreamForTest(System.out);
        errorOutputStream = new PrintStreamForTest(System.err);
        context = new IOContext(inputStream, outputStream, errorOutputStream);
        tokenizer = new CTokenizer(new CTokenRule());
        cpContext = new CParseContext(context, tokenizer);
    }

    @After
    public void tearDown() {
        inputStream = null;
        outputStream = null;
        errorOutputStream = null;
        context = null;
        tokenizer = null;
        cpContext = null;
    }

    public void resetEnvironment() {
        tearDown();
        setUp();
    }

    public void acceptList(String testData, CToken[] tokenList) {
        resetEnvironment();
        inputStream.setInputString(testData);
        CToken actualToken;
        for (int i=0; i<tokenList.length; i++) {
            actualToken = tokenizer.getNextToken(cpContext);
            accept("token"+(i+1)+tokenList[i]+" test: ", actualToken, tokenList[i]);
        }
    }

    public void accept(String testData, CToken token) {
        resetEnvironment();
        inputStream.setInputString(testData);
        CToken actualToken = tokenizer.getNextToken(cpContext);
        accept("token"+testData + " test: ", actualToken,token);
    }

    public void acceptNumber(String testData, CToken token, int number) {
        resetEnvironment();
        inputStream.setInputString(testData);
        CToken actualToken = tokenizer.getNextToken(cpContext);
        acceptNumber("token"+testData + " test: ", actualToken,token, number);
    }

    public void accept(String message, CToken token, int type, String text, int lineNo, int columnNo) {
        assertThat(message + ":" + "Type: ", token.getTokenString(), is(CToken.tokenString(type)));
        assertThat(message + ":" + "Text: ", token.getText(), is(text));
        assertThat(message + ":" + "LineNo: ", token.getLineNo(), is(lineNo));
        assertThat(message + ":" + "ColumnNo: ", token.getColumnNo(), is(columnNo));
    }

    public void accept(String message, CToken actualToken, CToken expectedToken) {
        accept(message, actualToken, 
                expectedToken.getType(), expectedToken.getText(), expectedToken.getLineNo(), expectedToken.getColumnNo());
    }

    public void acceptNumber(String message, CToken token, int type, String text, int lineNo, int columnNo, int number ) {
        accept(message, token, type, text, lineNo, columnNo);
        assertThat(token.toExplainString() + " Number chek: ", token.getIntValue(), is(number));
    }

    public void acceptNumber(String message, CToken actualToken, CToken expectedToken, int number) {
        acceptNumber(message, actualToken, 
                 expectedToken.getType(), expectedToken.getText(), expectedToken.getLineNo(), expectedToken.getColumnNo(),
                 number);
    }
}
