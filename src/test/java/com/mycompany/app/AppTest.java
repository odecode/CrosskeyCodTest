package com.mycompany.app;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public static App app;
    @BeforeClass
    public static void setup(){
        app = new App();

    }

    @Test
    public void testParser(){
        String[] lines = new String[5];
        lines[0] = "Juha,1000,5,2";
        lines[1] = "Karvinen,4356,1.27,6";
        lines[2] = "Claes Månsson,1300.55,8.67,2";
        lines[3] = "\"Clarencé,Andersson\",2000,6,4";
        lines[4] = "a,5,1,2";


        for (String line : lines) {
            String[] data = app.parseLine(line);
            for (String dataEntry : data) {
                assertTrue("String is null", dataEntry != null);
                assertTrue("String is empty",dataEntry.length() > 0);
            }
            
        }
    }

    @Test
    public void testNumConverter(){
        assertTrue("payment per month is negative with parameters: 1.72   50   1", app.calculateMontlyPayment(1.72, 50, 1) >= 0.0);
        assertTrue("payment per month is negative with parameters: 0.0    9999    10", app.calculateMontlyPayment(0.1, 9999, 10) >= 0.0);
        assertTrue("payment per month is negative with parameters: 999    3    2.8", app.calculateMontlyPayment(999, 3, 2.8) >= 0.0);
    }

    @Test
    public void testPrinter(){
        String[] row1 = {"","3000","1.2","3"};
        String[] row2 = {"James","3000","1.2","3"};

        
        assertTrue("Test case with empty string failed", app.printThis(row1, app)=="");
        assertTrue("Regular test case failed", app.printThis(row2, app).length()>1);
    }


}
