import junit.jupiter.api.Assertions.assertEquals;

public class TestsForEvaluation  {
    protected int value1, value2;

    // assigning the values
    protected void setUp(){
        value1 = 3;
        value2 = 3;
    }

    // test method to add two values
    public void testAdd(){
        double result = value1 + value2;
        //assertEquals(result == 6);
    }
}