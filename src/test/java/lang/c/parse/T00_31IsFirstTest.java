package lang.c.parse;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import lang.FatalErrorException;
import lang.c.testhelpter.IsFirstTestHelper;

@RunWith(Enclosed.class)
public class T00_31IsFirstTest {

    public static class NumberTest {
        // Test that each class's isFirst() is valid
        // Distant future, you should add necessary test cases to each Test code.

        IsFirstTestHelper<Number> numberHelper = new IsFirstTestHelper<Number>(Number.class);

        // 個別正当例
        @Test
        public void accept() throws FatalErrorException {
            numberHelper.trueTest("1");
            numberHelper.trueTest("12");
        }

        // リスト正当例
        @Test
        public void acceptList() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            numberHelper.trueListTest(testDataArr);
        }

        // 個別不当例
        @Test
        public void reject() throws FatalErrorException {
            numberHelper.falseTest("@2");
            numberHelper.falseTest("+");
        }

        // リスト不当例
        public void rejectList() throws FatalErrorException {
            String[] testDataArr = { "@2", "+" };
            numberHelper.falseListTest(testDataArr);
        }

        // 個別正当不当混在例
        @Test
        public void numberFail() throws FatalErrorException {
            numberHelper.test("1", true);
            numberHelper.test("32", true);
            numberHelper.test("+", false);
            numberHelper.test("@", false);
        }
    }

    public static class TermTest {
        IsFirstTestHelper<Term> termHelper = new IsFirstTestHelper<Term>(Term.class);

        @Test
        public void term() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            termHelper.trueListTest(testDataArr);
        }

        @Test
        public void termFail() throws FatalErrorException {
            String[] testDataArr = { "@2+2", "=" };
            termHelper.falseListTest(testDataArr);
        }
    }

    public static class FactorTest {
        IsFirstTestHelper<Factor> factorHelper = new IsFirstTestHelper<Factor>(Factor.class);

        @Test
        public void factor() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            factorHelper.trueListTest(testDataArr);
        }

        @Test
        public void factorFail() throws FatalErrorException {
            String[] testDataArr = { "@2+2", "=" };
            factorHelper.falseListTest(testDataArr);
        }
    }

    public static class ExpressionAddTest {
        IsFirstTestHelper<ExpressionAdd> expressionAddHelper = new IsFirstTestHelper<ExpressionAdd>(
                ExpressionAdd.class);

        @Test
        public void expressionAdd() throws FatalErrorException {
            String[] testDataArr = { "+13" };
            expressionAddHelper.trueListTest(testDataArr);
        }

        @Test
        public void expressionAddFail() throws FatalErrorException {
            String[] testDataArr = { "2+2", "=" };
            expressionAddHelper.falseListTest(testDataArr);
        }
    }

    public static class ExpressionTest {
        IsFirstTestHelper<Expression> expressionHelper = new IsFirstTestHelper<Expression>(Expression.class);

        @Test
        public void expression() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            expressionHelper.trueListTest(testDataArr);
        }

        @Test
        public void expressionFail() throws FatalErrorException {
            String[] testDataArr = { "+2", "=" };
            expressionHelper.falseListTest(testDataArr);
        }
    }

    public static class ProgramTest {
        IsFirstTestHelper<Program> programHelper = new IsFirstTestHelper<Program>(Program.class);

        @Test
        public void program() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            programHelper.trueListTest(testDataArr);
        }

        @Test
        public void programFail() throws FatalErrorException {
            String[] testDataArr = { "+2", "=" };
            programHelper.falseListTest(testDataArr);
        }
    }
}
