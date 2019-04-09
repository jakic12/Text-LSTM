package com.ml.other;
import java.util.*;
import com.ml.math.*;
import com.ml.nn.Mlp;
import com.ml.nn.DataManager;
import com.ml.nn.LstmCell;
import com.ml.nn.LstmChain;
import com.ml.gui.*;

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
                    "vector substraction"
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
                    "vector by matrix multiplication"
                );

                mathT.assertEqual(
                    MathV.sigm(0d),
                    0.5d,
                    "sigmoid function"
                );

                mathT.assertEqual(
                    MathV.dsigm(0d),
                    0.25d, 
                    "derivated sigmoid function"
                );

                mathT.assertEqual(
                    MathV.sigmArray(new double[]{0d,0d,0d}),
                    new double[]{0.5d, 0.5d, 0.5d},
                    "sigmoid array function"
                );

                mathT.assertEqual(
                    MathV.randomArray(2,3,0,0),
                    new double[][]{{0,0},{0,0},{0,0}},
                    "random array dimensions"
                );

                mathT.assertEqual(
                    MathV.sum(new double[]{10,20,2}),
                    32,
                    "sum of 1D array"
                );

                mathT.assertEqual(
                    MathV.sum(new double[][]{{1,2,3},{4,21,2}}),
                    33,
                    "sum of 2D array"
                );

                mathT.assertEqual(
                    MathV.round(1000d,17),
                    1000d,
                    "rounding"
                );

                mathT.assertEqual(
                    MathV.round(new double[]{1000,1000},17),
                    new double[]{1000,1000},
                    "rounding an array"
                );
                mathT.assertEqual(
                    MathV.pow(new double[]{3,4},3),
                    new double[]{27,64},
                    "elementwise exponentiations"
                );

                mathT.assertEqual(
                    MathV.emptyLike(new double[][]{{1,2,3},{1,1},{1,2,3,4,5}}),
                    new double[][]{{0,0,0},{0,0},{0,0,0,0,0}},
                    "empty array like"
                );

                mathT.assertTrue(
                    MathV.limit(10,0,5) == 5 &&
                    MathV.limit(-10,0,5) == 0 &&
                    MathV.limit(3,0,5) == 3,
                    "limit between 2 numbers"
                );

                mathT.assertTrue(
                    MathV.map(110,0,100, -20, -10) == -9.0 &&
                    MathV.map(20,0,40,20,100) == 60,
                    "map number"
                );

                mathT.assertEqual(
                    MathV.concat(new double[]{1,2,3}, new double[]{4,5,6}),
                    new double[]{1,2,3,4,5,6},
                    "concat vector"
                );

                mathT.assertEqual(
                    MathV.tanhArray(new double[]{0,3,-3}),
                    new double[]{0, 0.9950547536867305,-0.9950547536867305},
                    "tanhArray"
                );

                mathT.assertEqual(
                    MathV.indexOf(new char[]{'a','b','c'}, 'b'),
                    1,
                    "index of char array"
                );

                mathT.assertEqual(
                    MathV.vectorifyIndex(2, 4),
                    new double[]{0,0,1,0},
                    "vectorfy"
                );

                mathT.assertEqual(
                    MathV.sortArray(new char[]{'c', 'b', 'a'}),
                    new char[]{'a', 'b', 'c'},
                    "sort char array"
                );

            mathT.printResult();
            mainT.assertTrue(mathT.result(), "MathV tests");
            
            /*
            Tester MlpT = new Tester("Mlp");
                
                //create testing Mlp object
                Mlp testMlp = new Mlp(new int[]{3,2,2,3});

                MlpT.assertTrue( 
                    testMlp.getNeurons(0).length == 3 &&
                    testMlp.getNeurons(1).length == 2 &&  
                    testMlp.getNeurons(2).length == 2 && 
                    testMlp.getNeurons(3).length == 3
                    , "neuron dimensions"
                );

                MlpT.assertTrue( 
                    testMlp.getSynapses(0).length == 3 &&
                    testMlp.getSynapses(1).length == 2 &&  
                    testMlp.getSynapses(2).length == 2 && 
                    testMlp.getSynapses(0)[0].length == 2 &&
                    testMlp.getSynapses(1)[0].length == 2 &&  
                    testMlp.getSynapses(2)[0].length == 3
                    , "synapse dimensions"
                );

                testMlp.setSetting(1,1);
                testMlp.setSetting(2,1);
                testMlp.setSetting(3,0);
                testMlp.randomlySetWeights();

                MlpT.assertEqual(
                    testMlp.getSynapses(0),
                    new double[][]{{1,1},{1,1},{1,1}},
                    "weights initialized properly according to settings"
                );

                testMlp.forward(new double[]{1,0,1});
                MlpT.assertEqual(
                    (int)(testMlp.getNeurons(3)[1]*1000000)/1000000d,
                    0.846423,
                    "forward propagation"
                );

                MlpT.assertEqual(
                    (int)(testMlp.eval(new double[]{1,0,1})[1]*1000000)/1000000d,
                    0.846423,
                    "forward propagation ( eval )"
                );

                MlpT.assertTrue(
                    MathV.round(MathV.sum(Mlp.softmax(MathV.randomArray(3))),10) == 1,
                    "softmax function"
                );

                testMlp.setSetting(9, 1);

                MlpT.assertTrue(
                    MathV.round(MathV.sum(testMlp.eval(new double[]{1,2,3})),10) == 1,
                    "softmax at forwardpropagation"
                );

                testMlp.setSetting(9, 0);
                testMlp.setSetting(8, 1);

                testMlp.forward(new double[]{1,0,1},new double[]{0.846423, 0.846423, 0.846423});//not sure how to test error

                testMlp.forward(new double[]{1,0,0}, new double[]{1,0,0});
                double oldError = testMlp.error;
                testMlp.backpropagate(new double[]{1,0,0});
                testMlp.forward(new double[]{1,0,0},new double[]{1,0,0});

                MlpT.assertTrue( oldError > testMlp.error ,
                    "backpropagation lowers error (lowered by " + (int)((oldError-testMlp.error)/oldError*100) + "%)"
                );

                testMlp.setSetting(6,2);

                testMlp.forward(new double[]{1,0,0}, new double[]{1,0,0});
                oldError = testMlp.error;
                testMlp.backpropagate(new double[]{1,0,0});
                testMlp.forward(new double[]{1,0,0},new double[]{1,0,0});

                MlpT.assertTrue( oldError > testMlp.error ,
                    "backpropagation with tanh lowers error (lowered by " + (int)((oldError-testMlp.error)/oldError*100) + "%)"
                );
                
                testMlp.setSetting(8, 0.001);

                Mlp testMlp1 = new Mlp(new int[]{2,2,1});
                testMlp1.setSetting(3, 0);
                testMlp1.setSetting(8, 0.01);
                testMlp1.setSetting(11,1);
                testMlp1.randomlySetWeights();
                testMlp1.learn(new double[][]{{0,0}, {0,1}, {1,0}, {1,1}}, new double[][]{{0}, {1}, {1}, {0}}, 100000, 10);

                MlpT.debugString = testMlp1.error + "";

                MlpT.assertTrue(
                    Math.round(testMlp1.eval(new double[]{0,0})[0]) == 0 &&
                    Math.round(testMlp1.eval(new double[]{0,1})[0]) == 1 &&
                    Math.round(testMlp1.eval(new double[]{1,0})[0]) == 1 &&
                    Math.round(testMlp1.eval(new double[]{1,1})[0]) == 0,
                    "sigmoid can learn XOR"
                );

                Mlp testMlp2 = new Mlp(new int[]{2,2,1});
                testMlp2.setSetting(3, 0);
                testMlp2.setSetting(8, 0.01);
                testMlp2.setSetting(6, 2);
                testMlp2.setSetting(11,1);
                testMlp2.randomlySetWeights();
                testMlp2.learn(new double[][]{{0,0}, {0,1}, {1,0}, {1,1}}, new double[][]{{0}, {1}, {1}, {0}}, 100000, 10);

                MlpT.debugString = testMlp2.error + "";

                MlpT.assertTrue(
                    Math.round(testMlp2.eval(new double[]{0,0})[0]) == 0 &&
                    Math.round(testMlp2.eval(new double[]{0,1})[0]) == 1 &&
                    Math.round(testMlp2.eval(new double[]{1,0})[0]) == 1 &&
                    Math.round(testMlp2.eval(new double[]{1,1})[0]) == 0,
                    "tanh can learn XOR"
                );

            MlpT.printResult();
            mainT.assertTrue(MlpT.result(), "MLP tests");
            */
            /*
            Tester DataManagerT = new Tester("DataManager");

                DataManagerT.assertEqual(
                    DataManager.stringToInCharExpChar("test"),
                    new char[][]{{'t','e','s'},{'e','s','t'}},
                    "stringToInCharExpChar"
                );

                DataManagerT.assertEqual(
                    DataManager.vectorifyChar(new char[]{'t', 'e', 's'}, "test"),
                    new double[][]{{1,0,0}, {0,1,0}, {0,0,1}, {1,0,0}},
                    "vectorify char ( single array )"
                );

            DataManagerT.printResult();
            mainT.assertTrue(DataManagerT.result(), "DataManager tests");
            
            
            Tester lstmT = new Tester("LSTM");
                // 0 - t, 1 - e, 2 - s
                String testData = "test";
                double[][] testTestData = new double[][]{{0,0,0}, {1,0,0}, {0,1,0}, {0,0,1}, {1,0,0}};
                double[][] testExpData = new double[][]{{1,0,0}, {0,1,0}, {0,0,1}, {1,0,0}, {0,0,0}};
                LstmCell cell1 = new LstmCell(3, 3);

                cell1.forward(testTestData[1], testExpData[1]);
                double oldError1 = cell1.error;
                        
                cell1.backpropagate(testTestData[1], new double[3], new double[3], testExpData[1]);
                cell1.forward(testTestData[1], testExpData[1]);

                double relativeError = ((oldError1-cell1.error)/cell1.error*100);
                String out = ((relativeError + "").length() > 3)? (relativeError + "").substring(0,4) : relativeError + "";

                lstmT.assertTrue(
                    cell1.error - oldError1 < 0,
                    "lstm cell lowers error " + out + "%"
                );

                char[] vocab = new char[]{'\n','t','e','s'};

                testTestData = new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1},{0,1,0,0}};
                testExpData = new double[][]{{0,1,0,0},{0,0,1,0},{0,0,0,1},{0,1,0,0},{1,0,0,0}};
                LstmCell cell2 = new LstmCell(4, 4);
                LstmChain chain = new LstmChain(cell2);
                chain.learn(testTestData, testExpData, 1000);

                // TODO assert all of these
                System.out.println(vocab[MathV.maxIndex(chain.cell.eval(new double[]{1,0,0,0}))]);
                System.out.println(vocab[MathV.maxIndex(chain.cell.eval(new double[]{0,1,0,0}, chain.cell.h, chain.cell.c))]);
                System.out.println(vocab[MathV.maxIndex(chain.cell.eval(new double[]{0,0,1,0}, chain.cell.h, chain.cell.c))]);
                System.out.println(vocab[MathV.maxIndex(chain.cell.eval(new double[]{0,0,0,1}, chain.cell.h, chain.cell.c))]);
                System.out.println(vocab[MathV.maxIndex(chain.cell.eval(new double[]{0,1,0,0}, chain.cell.h, chain.cell.c))]);

            lstmT.printResult();
            mainT.assertTrue(lstmT.result(), "LSTM tests");
            */

        mainT.printResult();
        //System.exit(0);
    }

    int testCount;
    int testSuccess;
    String name;
    String debugString;

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

    public void assertEqual(double a, double b, String testName){
        this.debugString = a + "";
        assertTrue(a == b, testName);
    }

    public void assertEqual(double[] a, double[] b, String testName){
        this.debugString = Arrays.toString(a);
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

    public void assertEqual(char[] a, char[] b, String testName) {
        this.debugString = Arrays.toString(a);
        boolean pass = true;
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    pass = false;
                    break;
                }
            }
        } else {
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

    public void assertEqual(char[][] a, char[][] b, String testName) {
        boolean pass = true;
        if (a.length == b.length && a[0].length == b[0].length) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    if (a[i][j] != b[i][j]) {
                        pass = false;
                        break;
                    }
                }
            }
        } else {
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
                System.out.print("    " + testNames.get(i) + " ✔️\n");
                //System.out.println("    " + testNames.get(i) + " ✔️"); random newlines get added ( probably just vscode )
            }else{
                System.out.println("    " + testNames.get(i) + " ❌" + ((this.debugString != null)? "  " + this.debugString : ""));
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

    Tester MlpT = new Tester("Mlp");
    MlpT.assertTrue(true, "test1");
    MlpT.assertTrue(true, "test2");
    MlpT.assertTrue(true, "test3");
    MlpT.assertTrue(true, "test4");
    MlpT.printResult();
    mainTester.assertTrue(MlpT.result(), "Mlp tests");
mainTester.printResult();
*/