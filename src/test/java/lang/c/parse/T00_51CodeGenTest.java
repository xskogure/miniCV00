package lang.c.parse;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import lang.FatalErrorException;
import lang.c.testhelpter.CodeGenTestHelper;

@RunWith(Enclosed.class)
public class T00_51CodeGenTest {

    // Test for "cv00"
    public static class NumberTest {
        CodeGenTestHelper<Number> numberHelper = new CodeGenTestHelper<Number>(Number.class);

        @Test
        public void number0() throws FatalErrorException {
            String testData = "1";
            // ここに miniCompiler の実行結果を貼り付けるのはNG行為
            String expected = """
                        ;;; 予測の方に自分が予測コードを書く際に必要となる情報等は記入して良い
                        MOV #1, (R6)+   ;; Number で出力
                    """;
            // ↑ """の横位置が先頭と考える．コード・擬似コードは先頭に空白やタブが必要なことを思い出そう
            /* 以下はMOV命令の先頭に空白タブがないと判定される！ 
            String expected = """
                        ;;; 予測の方に自分が予測コードを書く際に必要となる情報等は記入して良い
                    MOV #1, (R6)+   ;; Number で出力
                    """;
                    */
            numberHelper.checkCodeGen(testData, expected);
        }
    }

    public static class FactorTest {
        CodeGenTestHelper<Factor> factorHelper = new CodeGenTestHelper<Factor>(Factor.class);

        @Test
        public void factor0() throws FatalErrorException {
            String testData = "1";
            // ここに miniCompiler の実行結果を貼り付けるのはNG行為
            String expected = """
                        ;;; 予測の方に自分が予測コードを書く際に必要となる情報等は記入して良い
                        MOV #1, (R6)+   ;; Number で出力
                    """;
            // ↑ """の横位置が先頭と考える．コード・擬似コードは先頭に空白やタブが必要なことを思い出そう
            factorHelper.checkCodeGen(testData, expected);
        }
    }

    public static class TermTest {
        CodeGenTestHelper<Term> termHelper = new CodeGenTestHelper<Term>(Term.class);

        @Test
        public void term0() throws FatalErrorException {
            String testData = "1";
            // ここに miniCompiler の実行結果を貼り付けるのはNG行為
            String expected = """
                        ;;; 予測の方に自分が予測コードを書く際に必要となる情報等は記入して良い
                        MOV #1, (R6)+
                    """;
            // ↑ """の横位置が先頭と考える．コード・擬似コードは先頭に空白やタブが必要なことを思い出そう
            termHelper.checkCodeGen(testData, expected);
        }

        // これは失敗するはず・Ignoreアノテーションをコメントにして，エラーを確認したら Ignoreアノテーションを有効に戻しておいてください
        @Ignore
        @Test
        public void term1() throws FatalErrorException {
            String testData = "1";
            // ここに miniCompiler の実行結果を貼り付けるのはNG行為
            String expected = """
                        ;;; 予測の方に自分が予測コードを書く際に必要となる情報等は記入して良い
                    MOV #1, (R6)+
                    """;
            // ↑ """の横位置が先頭と考える．この場合 MOV の前に空白やタブがない判定になるためこのテストは失敗する
            termHelper.checkCodeGen(testData, expected);
        }
    }

    public static class ExpressionTest {
        CodeGenTestHelper<Expression> expressionHelper = new CodeGenTestHelper<Expression>(Expression.class);

        @Test
        public void expression0() throws FatalErrorException {
            String testData = "7 + 2";
            // ここに miniCompiler の実行結果を貼り付けるのはNG行為
            String expected = """
                        MOV	#7, (R6)+  ;; Number が出力
                        MOV	#2, (R6)+  ;; Number が出力
                        MOV	-(R6), R0  ;; ここから ExpressionAdd
                        MOV	-(R6), R1
                        ADD	R1, R0
                        MOV	R0, (R6)+  ;; ここまで ExpressionAdd
                    """;
            // ↑ """の横位置が先頭と考える．この場合 ADD や MOV が """" と同じか前にあると，その命令が先頭で先頭に空白やタブがない判定になる
            expressionHelper.checkCodeGen(testData, expected);
        }
    }

    public static class ProgramTest {

        CodeGenTestHelper<Program> programHelper = new CodeGenTestHelper<Program>(Program.class);

        @Test
        public void program0() throws FatalErrorException {
            String testData = "13 + 7 + 2";
            // ここに miniCompiler の実行結果を貼り付けるのはテストの実施方法としてNG
            // この期待コードを提示してもらって説明してもらうのでコメントを沢山記載しておこう
            String expected = """
                        .=0x0100
                        JMP __START
                    __START:
                        MOV #0x1000,R6
                    ;; Number; ここで13を積む（このような解説コメントは入れていい）
                        MOV	#13, (R6)+  ;; push 13
                    ;; Number; 7を積む
                        MOV	#7, (R6)+   ;; push 7
                    ;; ここから，130+7 の演算  ExpressionAdd
                        MOV	-(R6), R0   ;; pop R0 (多分7)
                        MOV	-(R6), R1   ;; pop R1 (多分13)
                        ADD	R1, R0      ;; R0 += R1 (20)
                        MOV	R0, (R6)+   ;; push R0  (20)
                    ;; ここまでで，13+7 まで終わる
                    ;; ここから，20+2 の演算  ExpressionAdd
                    ;; 2を積む
                        MOV	#2, (R6)+   ;; push 2
                        MOV	-(R6), R0   ;; pop R0 (多分2)
                        MOV	-(R6), R1   ;; pop R1 (多分20)
                        ADD	R1, R0      ;; R0 += R1;  (22)
                        MOV	R0, (R6)+   ;; push R0 (22)
                    ;; これで 13+7+2 が終了
                        MOV -(R6), R0   ;; pop R0 (22)
                        HLT
                        .end
                    """;
            // ↑ """の横位置が先頭と考える．つまりこの行の""" と __START: の横位置が一致していないとテストが失敗する
            // ラベルは行先頭から書かないといけないこと．コード・擬似コードは先頭に空白やタブが必要なことを思い出そう

            // Check only code portion, not validate comments
            programHelper.checkCodeGen(testData, expected);
        }
    }
}
