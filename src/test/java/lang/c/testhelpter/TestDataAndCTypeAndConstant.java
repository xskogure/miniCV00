package lang.c.testhelpter;

public class TestDataAndCTypeAndConstant {
    private String testData;
    private int type;
    private boolean isConstant;

    public TestDataAndCTypeAndConstant(String testData, int type, boolean isConstant) {
        this.testData = testData;
        this.type = type;
        this.isConstant = isConstant;
    }

    public String getTestData() {
        return testData;
    }

    public int getType() {
        return type;
    }

    public boolean getIsConstant() {
        return isConstant;
    }
}
