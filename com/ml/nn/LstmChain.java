package com.ml.nn;

import java.util.Arrays;

import com.ml.gui.Graph;
import com.ml.math.MathV;
import com.ml.nn.LstmCell;

public class LstmChain{

    double[][] c;
    double[][] h;

    double[][] dc;
    double[][] dh;

    double[][][][][][][] cellNeurons = null;

    public LstmCell cell;

    public boolean graphProgress = true;
    public boolean graphAll = false;

    public double lowerLearningRateRate = 0.001-0.0000001; // how much to lower the learning rate every numOfEpochsToLower epochs
    public int numOfEpochsToLower = 1000000/2;

    Graph[] gT;

    public LstmChain(LstmCell cell){
        this.cell = cell;
    }

    /**
     * gets the cell neurons
     * @param cell the LstmCell of which the neurons have to be saved
     * @see #setCellNeurons(double[][][][], LstmCell)
     */
    // public double[][][][] getCellNeurons(LstmCell cell){
    //     double[][][][] out = new double[4][][][];
    //     out[0] = new double[cell.mlpGates.length][][];
    //     for(int i = 0; i < cell.mlpGates.length; i++){
    //         out[0][i] = cell.mlpGates[i].neurons.clone();
    //     }
    // 
    //     out[1] = new double[cell.doubleGates.length][][];
    //     for (int i = 0; i < cell.doubleGates.length; i++) {
    //         out[1][i] = cell.doubleGates[i].neurons.clone();
    //     }
    // 
    //     out[2][0][0] = cell.ct_1;
    //     out[3][0][0] = cell.ht_1;
    // 
    //     return out;
    // }

    public double[][][][][][] getCellNeurons() {
        double[][][][][][] out = new double[4][][][][][];
        out[0] = new double[this.cell.mlpGates.length][][][][];
        for (int i = 0; i < this.cell.mlpGates.length; i++) {
            out[0][i] = this.cell.mlpGates[i].toArray().clone();
        }

        out[1] = new double[this.cell.doubleGates.length][][][][];
        for (int i = 0; i < this.cell.doubleGates.length; i++) {
            out[1][i] = this.cell.doubleGates[i].toArray().clone();
        }

        out[2] = new double[1][1][1][1][];
        out[2][0][0][0][0] = this.cell.ct_1.clone();
        out[3] = new double[1][1][1][1][];
        out[3][0][0][0][0] = this.cell.ht_1.clone();

        return out;
    }
    

    /**
     * function that cooperates with {@link #getCellNeurons(LstmCell)}, so you can save neuron states and recover them
     * by first getting the cell neurons and saving them for further use
     * when you want the neurons to be recovered, you can call this function to set them back, <br>
     * data structure:<br>
     * <br>
     * <pre>
     * 2 types of gates + celloutputs [
     *      mlpGates[
     *          mlpGates layers[
     *              mlpGates layer1 neurons[x,x ... x],
     *              mlpGates layer2 neurons[x,x ... x],
     *              ...
     *              mlpGates layerN neurons[x,x ... x],
     *          ]
     *      ],
     *      doubleGates[
     *          doubleGates layers[
     *              doubleGates layer1 neurons[x,x ... x],
     *              doubleGates layer2 neurons[x,x ... x],
     *              ...
     *              doubleGates layerN neurons[x,x ... x],
     *          ]
     *      ],
     *      ct-1[
     *          [[[x,x, ... x]]]
     *      ],
     *      ht-1[
     *          [[[x,x, ... x]]]
     *      ],
     * ]
     * 
     * </pre>
     * 
     * @param neurons the neurons of all gates
     * @param cell the LstmCell of which the neurons have to be recovered
     */
    public void setCellNeurons(double[][][][] neurons, LstmCell cell){
        for (int i = 0; i < cell.mlpGates.length; i++) {
            cell.mlpGates[i].neurons = neurons[0][i];
        }

        for (int i = 0; i < cell.doubleGates.length; i++) {
            cell.doubleGates[i].neurons = neurons[1][i];
        }

        cell.ct_1 = neurons[2][0][0];
        cell.ct_1 = neurons[2][0][0];
        cell.calcNeurons();
    }

    public void learn(double[][] testData, double[][] expData, int epochs,/* test */char[] vocab){
        this.c = new double[testData.length][expData[0].length];
        this.h = new double[testData.length][expData[0].length];
        this.dc = new double[testData.length][expData[0].length - 1];
        this.dh = new double[testData.length][expData[0].length - 1];
        this.cellNeurons = new double[testData.length][][][][][][];

        Graph g = null;
        if (this.graphProgress) {
            g = new Graph();
            g.showGraph("lstm chain error");
            if(this.graphAll){
                gT = new Graph[testData.length];
                for (int i = 0; i < testData.length; i++) {
                    gT[i] = new Graph();
                    gT[i].showGraph("test " + i);
                }    
            }
        }

        for(int epoch = 0; epoch < epochs; epoch++){
            double startTime = System.currentTimeMillis();

            double error = forward(testData, expData);
            System.out.println(error);
            g.addData(error);
            backpropagate(testData, expData);

            if(epoch % numOfEpochsToLower == 0){
                this.cell.incrementAllSetting(8, -lowerLearningRateRate);
            }

            // test
            boolean testPrev = true;
            String testOutput = "";
            testOutput += (vocab[MathV.maxIndex(testData[0])]);
            testOutput += (vocab[MathV.maxIndex(this.cell.eval(testData[0]))]);
            for(int i = 1; i < testData.length; i++){
                if(testPrev)
                    testOutput += (vocab[MathV.maxIndex(this.cell.eval(MathV.vectorifyIndex(MathV.maxIndex(this.cell.h), this.cell.outSize), this.cell.h, this.cell.c))]);
                else
                    testOutput += (vocab[MathV.maxIndex(this.cell.eval(testData[i], this.cell.h, this.cell.c))]);
            }

            double endTime = System.currentTimeMillis();
            double duration = (endTime - startTime)/1000.0;

            System.out.println("'" + testOutput + "'");
            System.out.println("remaining: " + (duration*(epochs-epoch)) + "seconds");
            // test
        }

        System.out.println("end");
    }


    /**
     * Function that accepts input and expected output, so it can calculate the error
     * 
     * @param testData array of inputs
     * @param expData array of expected outputs
     * @return average error over all the test cases
     */
    public double forward(double[][] testData, double[][] expData){
        double sumError = 0;
        for(int t = 0; t < testData.length; t++){
            if(t == 0)
                this.cell.forward(testData[t], expData[t]);
            else
                this.cell.forward(testData[t], this.h[t-1], this.c[t-1], expData[t]);
            this.c[t] = this.cell.c;
            this.h[t] = this.cell.h;
            sumError += this.cell.error;

            if(this.cellNeurons != null){
                this.cellNeurons[t] = getCellNeurons();
            }

            if(this.gT != null)
                this.gT[t].addData(this.cell.error);
        }

        return sumError/testData.length;
    }

    public void backpropagate(double[][] testData, double[][] expData){
        for(int t = testData.length - 1; t >= 0; t--){
            /*if(t == 0)
                this.cell.forward(testData[0], expData[0]);
            else
                this.cell.forward(testData[t], this.h[t-1], this.c[t-1]);*/

            //setCellNeurons(this.cellNeurons[t], this.cell);
            LstmCell timeCell = new LstmCell(this.cell.inSize, this.cell.outSize, this.cellNeurons[t]);
            
            if(t == testData.length - 1)
                timeCell.backpropagate(testData[t], this.h[t-1], this.c[t-1], expData[t]);
            else if(t != 0){
                timeCell.backpropagate(testData[t], this.h[t-1], this.c[t-1], expData[t], this.dh[t], this.dc[t], this.cell);
            }else{
                timeCell.backpropagate(testData[t], new double[this.cell.outSize], new double[this.cell.outSize], expData[t], this.dh[t], this.dc[t], this.cell);
            }

            if (t != 0){
                this.dc[t-1] = timeCell.dCt_1;
                this.dh[t-1] = timeCell.dht_1;
            }
        }
    }
}