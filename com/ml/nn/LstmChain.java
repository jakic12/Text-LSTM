package com.ml.nn;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.ml.gui.Graph;
import com.ml.math.MathV;
import com.ml.nn.LstmCell;

import com.ml.other.*;

public class LstmChain{

    double[][] c;
    double[][] h;

    double[][] dc;
    double[][] dh;

    double[][][][][][][] cellNeurons = null;

    public LstmCell cell;

    public boolean graphProgress = false;
    public boolean graphAll = false;
    public boolean printProgresss = false;

    public double lowerLearningRateRate = 0; // how much to lower the learning rate every numOfEpochsToLower epochs
    public int numOfEpochsToLower = 1;
    
    private ProgressHandler handler = null;

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
    /**
     * learns the data: it forward propagates all the test data and backpropagates it "epoch" times
     * 
     * @param testData the input data trough time
     * @param expData the expected output trough time
     * @param epochs the number of times to iterate trough data
     * @return average error
     */
    private double learn(double[][] testData, double[][] expData, int epochs){
        this.c = new double[testData.length][expData[0].length];
        this.h = new double[testData.length][expData[0].length];
        this.dc = new double[testData.length][expData[0].length - 1];
        this.dh = new double[testData.length][expData[0].length - 1];
        this.cellNeurons = new double[testData.length][][][][][][];
        double error = 0;

        for(int epoch = 0; epoch < epochs; epoch++){
            error += forward(testData, expData);
            backpropagate(testData, expData);
            
            if(epoch % numOfEpochsToLower == 0){
                this.cell.incrementAllSetting(8, -lowerLearningRateRate);
            }
        }
        return error/epochs;
    }

    /**
     * learns multiple datasets
     * same as {@link #learn(double[][], double[][], int)}, but for multiple datasets
     * 
     * @param testData all the datasets (testData[0] -> data trough time)
     * @param expData the expected data
     * @param epochs number of times to repeat the process
     * @param iterations number of times to go trough each dataset
     */
    public void learn(double[][][] testData, double[][][] expData, int epochs, int iterations){
        Graph g = null;
        if (this.graphProgress) {
            g = new Graph();
            g.showGraph("lstm chain error");
            if (this.graphAll) {
                gT = new Graph[testData.length];
                for (int i = 0; i < testData.length; i++) {
                    gT[i] = new Graph();
                    gT[i].showGraph("test " + i);
                }
            }
        }
        for(int epoch = 0; epoch < epochs; epoch++){
            double startTime = 0;
            if(this.printProgresss)
                startTime = System.currentTimeMillis();//timer

            //the learning part
            double error = 0;
            for (int i = 0; i < testData.length; i++) {
                error += learn(testData[i], expData[i], iterations);
            }
            error /= testData.length;

            //visualization
            if(this.printProgresss)
                System.out.println(error);
            if(g != null)
                g.addData(error);

            // timer
            if(this.printProgresss){
                double endTime = System.currentTimeMillis();
                double duration = (endTime - startTime) / 1000.0;

                System.out.println(epoch + "e - remaining: " + new SimpleDateFormat("HH:mm:ss").format(new Date(0,0,0,0,0,(int)(duration * (epochs - epoch)))));
            }

            //handler
            if(this.handler != null){
                this.handler.progress(((double)epoch/epochs)*100);
            }

        }
        if(this.printProgresss)
            System.out.println("end");
    }


    /**
     * forwards all the test data and calculate the average error
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

    /**
     * forwards the starting data first and then forwards the output of t-1,
     * n-1 times
     * @param start the first input
     * @param count how many times to forward
     * @return the array of all of the outputs trough time
     */
    public double[][] forward(double[] start, int count){
        double[][] out = new double[count][];
        out[0] = this.cell.eval(start);
        for(int i = 1; i < count; i++){
            out[i] = this.cell.eval(this.cell.h, this.cell.h, this.cell.c);
        }
        return out;
    }

    /**
     * same as {@link #forward(double[], int)}, but cleans up output before feeding it forward<br>
     * <pre>
     * example:
     *  before clean up:
     *      [0.213123,0.243234,0.92123123,0.0210239]
     *  after clean up:
     *      [0,0,1,0]
     * </pre>
     * @see #forward(double[], int)
     */
    public double[][] forwardWithVectorify(double[] start, int count) {
        double[][] out = new double[count][];
        out[0] = this.cell.eval(start);
        for (int i = 1; i < count; i++) {
            out[i] = this.cell.eval(MathV.vectorifyIndex(MathV.maxIndex(this.cell.h), this.cell.outSize), this.cell.h, this.cell.c);
        }
        return out;
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
    
    /**
     * sets up the handler that gets called every epoch<br>
     * 
     * <pre>
     * chain.onProgress(new ProgressHandler(){
     *  //code to be executed
     * });
     * </pre>
     * 
     * @param handler the handler to be called on progress
     * @see ProgressHandler
     */
    public void onProgress(ProgressHandler handler){
        this.handler = handler;
    }
}