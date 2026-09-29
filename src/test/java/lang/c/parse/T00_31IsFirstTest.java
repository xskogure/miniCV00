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

        IsFirstTestHelper<Number> helper = new IsFirstTestHelper<>(Number.class);

        // 個別正当例
        @Test
        public void accept() throws FatalErrorException {
            helper.trueTest("1");
            helper.trueTest("12");
        }

        // リスト正当例
        @Test
        public void acceptList() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            helper.trueListTest(testDataArr);
        }

        // 個別不当例
        @Test
        public void reject() throws FatalErrorException {
            helper.falseTest("@2");
            helper.falseTest("+");
        }

        // リスト不当例
        @Test
        public void rejectList() throws FatalErrorException {
            String[] testDataArr = { "@2", "+" };
            helper.falseListTest(testDataArr);
        }

        // 個別正当不当混在例
        @Test
        public void numberFail() throws FatalErrorException {
            helper.test("1", true);
            helper.test("32", true);
            helper.test("+", false);
            helper.test("@", false);
        }
    }

    public static class TermTest {
        IsFirstTestHelper<Term> helper = new IsFirstTestHelper<>(Term.class);

        @Test
        public void term() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            helper.trueListTest(testDataArr);
        }

        @Test
        public void termFail() throws FatalErrorException {
            String[] testDataArr = { "@2+2", "=" };
            helper.falseListTest(testDataArr);
        }
    }

    public static class FactorTest {
        IsFirstTestHelper<Factor> helper = new IsFirstTestHelper<>(Factor.class);

        @Test
        public void factor() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            helper.trueListTest(testDataArr);
        }

        @Test
        public void factorFail() throws FatalErrorException {
            String[] testDataArr = { "@2+2", "=" };
            helper.falseListTest(testDataArr);
        }
    }

    public static class ExpressionAddTest {
        IsFirstTestHelper<ExpressionAdd> helper = new IsFirstTestHelper<>(
                ExpressionAdd.class);

        @Test
        public void expressionAdd() throws FatalErrorException {
            String[] testDataArr = { "+13" };
            helper.trueListTest(testDataArr);
        }

        @Test
        public void expressionAddFail() throws FatalErrorException {
            String[] testDataArr = { "2+2", "=" };
            helper.falseListTest(testDataArr);
        }
    }

    public static class ExpressionTest {
        IsFirstTestHelper<Expression> helper = new IsFirstTestHelper<>(Expression.class);

        @Test
        public void expression() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            helper.trueListTest(testDataArr);
        }

        @Test
        public void expressionFail() throws FatalErrorException {
            String[] testDataArr = { "+2", "=" };
            helper.falseListTest(testDataArr);
        }
    }

    public static class ProgramTest {
        IsFirstTestHelper<Program> helper = new IsFirstTestHelper<>(Program.class);

        @Test
        public void program() throws FatalErrorException {
            String[] testDataArr = { "13", "11+2" };
            helper.trueListTest(testDataArr);
        }

        @Test
        public void programFail() throws FatalErrorException {
            String[] testDataArr = { "+2", "=" };
            helper.falseListTest(testDataArr);
        }
    }
}
