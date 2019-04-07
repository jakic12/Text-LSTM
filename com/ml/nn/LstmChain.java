package com.ml.nn;

import java.util.Arrays;

import com.ml.gui.Graph;
import com.ml.nn.LstmCell;

public class LstmChain{

    double[][] c;
    double[][] h;

    double[][] dc;
    double[][] dh;

    public LstmCell cell;

    public boolean graphProgress = true;
    
    public static void main(String[] args) {
        // 0 - end/start,  1 - t,  2 - e, 3 - s

        double[][] testTestData = new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1},{0,1,0,0}};
        double[][] testExpData = new double[][]{{0,1,0,0},{0,0,1,0},{0,0,0,1},{0,1,0,0},{1,0,0,0}};
        LstmCell cell1 = new LstmCell(4, 4);
        LstmChain chain = new LstmChain(cell1);
        chain.learn(testTestData, testExpData, 1000000);

        System.out.println(Arrays.toString( chain.cell.eval(new double[]{1,0,0,0}) ));
        System.out.println(Arrays.toString( chain.cell.eval(new double[]{0,1,0,0}, chain.cell.h, chain.cell.c) ));
        System.out.println(Arrays.toString(chain.cell.eval(new double[]{0,0,1,0}, chain.cell.h, chain.cell.c)));
        System.out.println(Arrays.toString( chain.cell.eval(new double[]{0,0,0,1}, chain.cell.h, chain.cell.c) ));
        System.out.println(Arrays.toString(chain.cell.eval(new double[]{0,1,0,0}, chain.cell.h, chain.cell.c)));
    }

    public LstmChain(LstmCell cell){
        this.cell = cell;
    }

    public void learn(double[][] testData, double[][] expData, int epochs){
        this.c = new double[testData.length][expData[0].length];
        this.h = new double[testData.length][expData[0].length];
        this.dc = new double[testData.length][expData[0].length - 1];
        this.dh = new double[testData.length][expData[0].length - 1];

        Graph g = null;
        if (this.graphProgress) {
            g = new Graph();
            g.showGraph("lstm chain error");
        }

        for(int epoch = 0; epoch < epochs; epoch++){
            double error = forward(testData, expData);
            g.addData(error);
            backpropagate(testData, expData);
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
            this.cell.forward(testData[t], expData[t]);
            this.c[t] = this.cell.c;
            this.h[t] = this.cell.h;
            sumError += this.cell.error;
        }

        return sumError/testData.length;
    }

    public void backpropagate(double[][] testData, double[][] expData){
        for(int t = testData.length - 1; t > 0; t--){
            if(t == 0)
                this.cell.forward(testData[0], expData[0]);
            else
                this.cell.forward(testData[t], this.h[t-1], this.c[t-1]);
            
            if(t == testData.length - 1)
                this.cell.backpropagate(testData[t], this.h[t-1], this.c[t-1], expData[t]);
            else
                this.cell.backpropagate(testData[t], this.h[t-1], this.c[t-1], expData[t], this.dh[t], this.dc[t]);

            if (t != 0){
                this.dc[t-1] = this.cell.dCt_1;
                this.dh[t-1] = this.cell.dht_1;
            }
        }
    }
}