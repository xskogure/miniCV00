package lang.c.testhelpter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import lang.FatalErrorException;
import lang.IOContext;
import lang.InputStreamForTest;
import lang.PrintStreamForTest;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenRule;
import lang.c.CTokenizer;

import org.junit.After;
import org.junit.Before;

public class IsFirstTestHelper<T extends CParseRule> {

    InputStreamForTest inputStream;
    PrintStreamForTest outputStream;
    PrintStreamForTest errorOutputStream;
    CTokenizer tokenizer;
    IOContext context;
    CParseContext cpContext;
    Class<T> c;
    Method isFirst;
    
    public IsFirstTestHelper(Class<T> c) {
        this.c = c;
        try {
            isFirst = c.getMethod("isFirst",  CToken.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
        tokenizer = null;
        context = null;
        cpContext = null;
    }

    public void resetEnvironment() {
        tearDown();
        setUp();
    }

    // isFirst() が true になるテスト List用
    public void trueListTest(String[] testDataArr) throws FatalErrorException {
        listTest(testDataArr, true);
    }
    
    // isFirst() が false になるテスト List用
    public void falseListTest(String[] testDataArr) throws FatalErrorException {
        listTest(testDataArr, false);
    }

    // isFirst() の true, false をチェックするテスト List用
    public void listTest(String[] testDataArr, boolean expected) throws FatalErrorException {
        for ( String testData: testDataArr ) {
            test(testData, expected);
        }
    }

    // isFirst() が true になるテスト 1個用
    public void trueTest(String testData) throws FatalErrorException {
        test(testData, true);
    }

    // isFirst() が true になるテスト 1個用
    public void falseTest(String testData) throws FatalErrorException {
        test(testData, false);
    }

    // isFirst() が true, false になるテスト 1個用
    public void test(String testData, boolean expected) throws FatalErrorException {
        resetEnvironment();
        inputStream.setInputString(testData);
        CToken firstToken = tokenizer.getNextToken(cpContext);
        System.err.println("isFirst: " + isFirst);
        try {
            assertThat(testData, (boolean)isFirst.invoke(null, firstToken), is(expected));
        } catch (IllegalAccessException iae) {
            fail("IllegalAccessException: isFirst() is not found. Please check declaration of \"public\" class ");
        }  catch (Exception e) {
            fail("testData\"" + testData + "\": this testdata was rejected for a reason except FatalError and IllegalAccess. ");
            e.printStackTrace();
        }
    }
}
