import java.util.*;

public class Tester{
    public static void main(String[] args){
        Tester mainT = new Tester("Overall");

            Tester mathT = new Tester("MathV");

            mathT.assertEqual(
                MathV.add(new double[]{1d,3d}, new double[]{3d,2d}),
                new double[]{4d, 5d},
                "vector addition"
            );
            mathT.assertEqual(
                MathV.sub(new double[]{6d,10d}, new double[]{3d,2d}),
                new double[]{3d, 8d}, 
                "vector substractiob"
            );
            mathT.assertEqual(
                MathV.multiply(new double[]{1d,3d}, new double[]{3d,2d}),
                new double[]{3d, 6d}, 
                "vector multiplication"
            );
            mathT.assertEqual(
                MathV.div(new double[]{6d,10d}, new double[]{3d,2d}),
                new double[]{2d, 5d}, 
                "vector division"
            );

            mathT.assertEqual(
                MathV.dot(new double[][]{{1,2,3},{1,2,3}}, new double[][]{{1,2,3,1,3,3},{3,1,3,2,2,2},{3,2,3,2,1,4}}),
                new double[][]{{16,10,18,11,10,19},{16,10,18,11,10,19}},
                "matrix by matrix multiplication"
            );

            mathT.assertEqual(
                MathV.dot(new double[]{1,2,3},
                new double[][]{{1,2,3,1,3,3},{3,1,3,2,2,2},{3,2,3,2,1,4}}),
                new double[]{16,10,18,11,10,19},
                "vector by matrix multiplication");


            mathT.printResult();
            mainT.assertTrue(mathT.result(), "MathV tests");

        mainT.printResult();
    }

    int testCount;
    int testSuccess;
    String name;

    ArrayList<Boolean> testResults;
    ArrayList<String> testNames;


    public Tester(String name){
        this.name = name;

        testResults = new ArrayList<Boolean>();
        testNames = new ArrayList<String>();
    }

    public Tester(){
        this.name = "";

        testResults = new ArrayList<Boolean>();
        testNames = new ArrayList<String>();
    }

    public void assertEqual(double[] a, double[] b, String testName){
        boolean pass = true;
        if(a.length == b.length){
            for(int i = 0; i < a.length; i++){
                if(a[i] != b[i]){
                    pass = false;
                    break;
                }
            }
        }else{
            pass = false;
        }

        assertTrue(pass, testName);
    }

    public void assertEqual(double[][] a, double[][] b, String testName){
        boolean pass = true;
        if(a.length == b.length && a[0].length == b[0].length){
            for(int i = 0; i < a.length; i++){
                for(int j = 0; j < a[i].length; j++){
                    if(a[i][j] != b[i][j]){
                        pass = false;
                        break;
                    }
                }
            }
        }else{
            pass = false;
        }

        assertTrue(pass, testName);
    }

    public void assertTrue(boolean bool, String testName){
        this.testCount++;
        if(bool){
            this.testSuccess++;
        }

        testResults.add(bool);
        testNames.add(testName);
    }

    public boolean result(){
        return (this.testCount == this.testSuccess);
    }

    public void printResult(){
        System.out.println();
        System.out.print(this.name + " ");

        if(this.testCount == this.testSuccess){
            System.out.println(this.testSuccess + "/" + this.testCount + " ✔️");
        }else{
            System.out.println(this.testSuccess + "/" + this.testCount + " ❌");
        }
        
        for(int i = 0; i < testCount; i++){
            if(testResults.get(i)){
                System.out.println("    " + testNames.get(i) + " ✔️");
            }else{
                System.out.println("    " + testNames.get(i) + " ❌");
            }
        }
    }

}

/* example 
Tester mainTester = new Tester("overall");
    Tester vectorT = new Tester("Vectors");
    vectorT.assertTrue(true, "test1");
    vectorT.assertTrue(false, "test2");
    vectorT.assertTrue(true, "test3");
    vectorT.assertTrue(true, "test4");
    vectorT.printResult();
    mainTester.assertTrue(vectorT.result(), "Vector tests");

    Tester cnnT = new Tester("Cnn");
    cnnT.assertTrue(true, "test1");
    cnnT.assertTrue(true, "test2");
    cnnT.assertTrue(true, "test3");
    cnnT.assertTrue(true, "test4");
    cnnT.printResult();
    mainTester.assertTrue(cnnT.result(), "Cnn tests");
mainTester.printResult();
*/