package com.ml.tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.ml.math.*;
import com.ml.nn.Mlp;
import com.ml.other.ProgressHandler;
import com.ml.data.DataManager;
import com.ml.nn.LstmBlock;
import com.ml.nn.LstmCell;
import com.ml.nn.LstmChain;

public class Tester{
    public static void main(String[] args){
        Tester mainT = new Tester("Overall");
            /*
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
                testMlp.setSetting(11, 0);

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
                testMlp1.setSetting(11,0);
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
                testMlp2.setSetting(11,0);
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
            
            
            Tester DataManagerT = new Tester("DataManager");

                DataManagerT.assertEqual(
                    DataManager.stringToInCharExpChar("test"),
                    new char[][]{{'t','e','s'},{'e','s','t'}},
                    "stringToInCharExpChar"
                );

                DataManagerT.assertEqual(
                    DataManager.stringToInCharExpChar(new String[]{"test", "test2"}),
                    new char[][][]{{{'t','e','s'},{'t','e','s','t'}}, {{'e','s','t'},{'e','s','t','2'}}},
                    "stringToInCharExpChar array"
                );

                DataManagerT.assertEqual(
                    DataManager.vectorifyChar(new char[]{'t', 'e', 's'}, "test"),
                    new double[][]{{1,0,0}, {0,1,0}, {0,0,1}, {1,0,0}},
                    "vectorify char ( single array )"
                );

                DataManagerT.assertEqual(
                    DataManager.buildCharVocab("test vocab"),
                    new char[]{' ','a','b','c','e','o','s','t','v'},
                    "build vocab"
                );

            DataManagerT.printResult();
            mainT.assertTrue(DataManagerT.result(), "DataManager tests");
            
            
            Tester lstmT = new Tester("LSTM");

                /*LstmCell msWordTestCell = new LstmCell(2,1);

                msWordTestCell.mlpGates[3].synapses[0][0][0] = 0.45;
                msWordTestCell.mlpGates[3].synapses[0][1][0] = 0.25;
                msWordTestCell.doubleGates[3].synapses[0][0][0] = 0.15;
                msWordTestCell.doubleGates[3].biases[1][0] = 0.2;
                msWordTestCell.mlpGates[3].biases[1][0] = 0;

                msWordTestCell.mlpGates[1].synapses[0][0][0] = 0.95;
                msWordTestCell.mlpGates[1].synapses[0][1][0] = 0.8;
                msWordTestCell.doubleGates[1].synapses[0][0][0] = 0.8;
                msWordTestCell.doubleGates[1].biases[1][0] = 0.65;
                msWordTestCell.mlpGates[1].biases[1][0] = 0;

                msWordTestCell.mlpGates[0].synapses[0][0][0] = 0.7;
                msWordTestCell.mlpGates[0].synapses[0][1][0] = 0.45;
                msWordTestCell.doubleGates[0].synapses[0][0][0] = 0.1;
                msWordTestCell.doubleGates[0].biases[1][0] = 0.15;
                msWordTestCell.mlpGates[0].biases[1][0] = 0;
                
                msWordTestCell.mlpGates[2].synapses[0][0][0] = 0.6;
                msWordTestCell.mlpGates[2].synapses[0][1][0] = 0.4;
                msWordTestCell.doubleGates[2].synapses[0][0][0] = 0.25;
                msWordTestCell.doubleGates[2].biases[1][0] = 0.1;
                msWordTestCell.mlpGates[2].biases[1][0] = 0;

                LstmChain testChain2 = new LstmChain(msWordTestCell);

                testChain2.learn(new double[][]{{1,2},{0.5,3}},new double[][]{{0.5},{1.25}}, 1);/////////////////

                // the test sentences - unsplit
                String testSentences = "test1,,reee";
                testSentences.replace(",,", "");
                String[] testData = testSentences.split(",,");
                /*for(int i = 0; i < testData.length-1; i++){
                    testData[i] += " ";
                }/////////////////

                //build vocabulary and change the chars to input output pairs
                char[][][] data = DataManager.stringToInCharExpChar(testData);
                char[] vocabulary = DataManager.buildCharVocab(testSentences);

                //converts pairs to data - vectorifies all the chars
                double[][][] testTestData = DataManager.vectorifyChar(vocabulary, data[0]);
                double[][][] testExpData = DataManager.vectorifyChar(vocabulary, data[1]);

                //create lstm cell and chain
                LstmCell cell1 = new LstmCell(vocabulary.length, vocabulary.length, 0.001);
                LstmChain chain = new LstmChain(cell1);

                chain.onProgress(new ProgressHandler(){
                    @Override
                    public void progress(int epoch, double error){
                        //System.out.println(progress);
                    }

                    @Override
                    public void end(double error) {
                    
                    }
                });

                //learn the data
                chain.learn(testTestData, testExpData, 1000, 50);
                
                chain.printProgresss = false;

                //generate sentences
                for(int i = 0; i < testData.length; i++){
                    double[][] out = chain.forwardWithVectorify(DataManager.vectorifyChar(vocabulary, testData[i].charAt(0)), testData[i].length()-1);
                    char[] outChar = DataManager.vectorToChar(out, vocabulary);
                    String outStr = testData[i].charAt(0) + new String(outChar);
                    //System.out.println(outStr);
                    lstmT.assertEqual(
                        outStr, 
                        testData[i], 
                        "learned sentence:'" + testData[i] + "'"
                    );
                }
            lstmT.printResult();
            mainT.assertTrue(lstmT.result(), "LSTM tests");*/
            

            Tester lstmBlockT = new Tester("LSTM block");
                String sentence = "test";
                LstmBlock block = new LstmBlock(sentence, 0.001);
                block.train(1000, 50);
                String testForward1 = block.forward('t', sentence.length()-1);

                lstmBlockT.assertEqual(
                    sentence, 
                    testForward1, 
                    "lstm block can learn '" + sentence + "'"
                );

                String afterSave = "";

                try {
                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("testFile.net"));
                    os.writeObject(block);
                    os.close();


                    ObjectInputStream is = new ObjectInputStream(new FileInputStream("testFile.net"));
                    LstmBlock block1 = (LstmBlock)is.readObject();
                    afterSave = block1.forward('t', sentence.length()-1);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                lstmBlockT.assertEqual(
                    testForward1, 
                    afterSave, 
                    "lstm block forwards the same after saving and oppening it again"
                );



            lstmBlockT.printResult();
            mainT.assertTrue(lstmBlockT.result(), "LSTM block tests");

            

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

    public void assertEqual(String a, String b, String testName){
        assertTrue(a.equals(b), testName);
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

    public void assertEqual(char[][][] a, char[][][] b, String testName) {
        boolean pass = true;
        if (a.length == b.length && a[0].length == b[0].length) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    for(int x = 0; x < a[i][j].length; x++){
                        if (a[i][j][x] != b[i][j][x]) {
                            pass = false;
                            break;
                        }
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