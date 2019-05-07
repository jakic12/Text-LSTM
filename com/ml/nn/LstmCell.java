package com.ml.nn;

import java.io.Serializable;

import javax.management.RuntimeErrorException;

import com.ml.gui.*;
import com.ml.math.MathV;
import com.ml.nn.Mlp;

public class LstmCell implements Serializable{

    // settings
    public double learningRate = 0.00001;
    public String errorFunction = "meanSquared";
    public String gateMode = "double"; // double - one mlp for x, single mlp for h
    
    public Mlp[] mlpGates;
    public Mlp[] doubleGates; // the gates for the h
    public double[] c;
    public double[] h;
    public double[] ct_1; //for easier saving
    public double[] ht_1; //for easier saving
    public double error;

    public double[][] S;

    public double[] dCt_1;
    public double[] dht_1;

    public int inSize;
    public int outSize;

    public int[] mlpDimensions;
    public int[] mlpDimensionsH; // dimentions for the X network if gate mode is double
    
    public LstmCell(int inputSize, int outputSize, double learningRate) {
        this.learningRate = learningRate;
        this.inSize = inputSize;
        this.outSize = outputSize;

        this.mlpDimensions = new int[] { inputSize, outputSize };

        // this.mlpGates[0] -> S0
        // this.mlpGates[1] -> S1
        // this.mlpGates[2] -> S2
        // this.mlpGates[3] -> T0

        this.mlpDimensionsH = this.mlpDimensions.clone();
        this.mlpDimensionsH[0] = outputSize;

        this.S = new double[4][outputSize];

        // init the second networks if gatemode is double

        this.doubleGates = new Mlp[4];
        // forget gate
        this.doubleGates[0] = new Mlp(this.mlpDimensionsH);
        

        // add sigm gate
        this.doubleGates[1] = new Mlp(this.mlpDimensionsH);

        // add candidates
        this.doubleGates[3] = new Mlp(this.mlpDimensionsH);

        // output gate
        this.doubleGates[2] = new Mlp(this.mlpDimensionsH);

        this.mlpGates = new Mlp[4];
        // forget gate
        this.mlpGates[0] = new Mlp(this.mlpDimensions);

        // add sigm gate
        this.mlpGates[1] = new Mlp(this.mlpDimensions);

        // add candidates
        this.mlpGates[3] = new Mlp(this.mlpDimensions);

        // output gate
        this.mlpGates[2] = new Mlp(this.mlpDimensions);

        for (int i = 0; i < 4; i++) { // for all mlpGates
            this.mlpGates[i].setSetting(1, -1);
            this.mlpGates[i].setSetting(8, this.learningRate);
            this.mlpGates[i].setSetting(6, 0);

            this.doubleGates[i].setSetting(1, -1);
            this.doubleGates[i].setSetting(2, 1);
            this.doubleGates[i].setSetting(8, this.learningRate);
            this.doubleGates[i].setSetting(6, 0);
        }

        this.doubleGates[0].randomlySetWeights();
        this.doubleGates[1].randomlySetWeights();
        this.doubleGates[3].randomlySetWeights();
        this.doubleGates[2].randomlySetWeights();
        
        this.mlpGates[0].randomlySetWeights();
        this.mlpGates[1].randomlySetWeights();
        this.mlpGates[3].randomlySetWeights();
        this.mlpGates[2].randomlySetWeights();
    }

    public void setLearningRate(double learningRate){
        this.learningRate = learningRate;

        for (int i = 0; i < 4; i++) { // for all mlpGates
            this.mlpGates[i].setSetting(8, this.learningRate);
            this.doubleGates[i].setSetting(8, this.learningRate);
        } 
    }

    /**
     * set the setting of all the gate mlps
     * 
     * @param index the index of the setting
     * @param value the value of the setting
     */
    public void setAllSettings(int index, double value){
        for (int i = 0; i < 4; i++) { // for all mlpGates
            this.mlpGates[i].setSetting(index, value);
            this.doubleGates[i].setSetting(index, value);
        }
    }
    
    /**
     * increment the setting by the ammount of all the gate mlps
     * 
     * @param index   the index of the setting
     * @param ammount the ammount for the setting to be incremented
     */
    public void incrementAllSetting(int index, double ammount){
        for (int i = 0; i < 4; i++) { // for all mlpGates
            this.mlpGates[i].incrementSetting(index, ammount);
            this.doubleGates[i].incrementSetting(index, ammount);
        }
    }

    public void forward(double[] x, double[] h, double[] c){

        if(x.length != this.inSize || h.length != this.outSize || c.length != this.outSize){
            throw new RuntimeException("vector size error");
        }

        this.ct_1 = c.clone();
        this.ht_1 = h.clone();

        this.S[0] = MathV.sigmArray(evalGate(0, x, h));
        this.S[1] = MathV.sigmArray(evalGate(1, x, h));
        this.S[2] = MathV.sigmArray(evalGate(2, x, h));
        this.S[3] = MathV.tanhArray(evalGate(3, x, h));

        c = c.clone();
        c = MathV.multiply(c, this.S[0]);
        c = MathV.add(c, MathV.multiply(this.S[1], this.S[3]));
        this.c = c.clone();
        this.h = MathV.multiply(MathV.tanhArray(c), this.S[2]);
    }

    public void calcNeurons(){
        this.S[0] = MathV.sigmArray(evalGateWithoutForward(0));
        this.S[1] = MathV.sigmArray(evalGateWithoutForward(1));
        this.S[2] = MathV.sigmArray(evalGateWithoutForward(2));
        this.S[3] = MathV.tanhArray(evalGateWithoutForward(3));

        c = this.ct_1;
        c = MathV.multiply(c, this.S[0]);
        c = MathV.add(c, MathV.multiply(this.S[1], this.S[3]));
        this.c = c.clone();
        this.h = MathV.multiply(MathV.tanhArray(c), this.S[2]);
    }
    
    /**
     * this is adding a level of abstraction, so it controlls the gatemode
     * note gate 1,2 still count as sepparate gates
     * 
     * @param index index of the gate
     * @param x input
     * @param h hidden state
     */
    public double[] evalGate(int index, double[] x, double[] h){
        return MathV.add(this.mlpGates[index].eval(x), this.doubleGates[index].eval(h));
    }

    public double[] evalGateWithoutForward(int index){
        return MathV.add(this.mlpGates[index].getOut(), this.doubleGates[index].getOut());
    }

    public void forward(double[] x, double[] h, double[] c, double[] expected){
        forward(x,h,c);
        this.error = errorFunction(this.h,expected);
    }

    public void forward(double[] x){
        forward(x, new double[this.outSize], new double[this.outSize]);
    }
    
    public void forward(double[] x, double[] expected) {
        forward(x, new double[this.outSize], new double[this.outSize], expected);
    }

    public double[] eval(double[] x, double[] h, double[] c) {
        forward(x, h, c);
        return this.h;
    }

    public double[] eval(double[] x) {
        forward(x);
        return this.h;
    }


    /**
     * This is a function that updates all the sub mlps to lower the error the
     * function is a <code>void</code> itself, but the values {@link #LstmCell.dCt_1}
     * and {@link #LstmCell.dht_1} are updated for further use
     * 
     * @param x           the input for this time step (t)
     * @param ht_1        the output of the previous time step (t-1)
     * @param ct_1        the memory of the previous time step (t-1)
     * @param expectedOut the expected output of the cell at the timestep (t)
     * @param dht1        the derivative of the output of the next time step (t+1)
     * @param dCt1        the derivative of the memory of the next time step (t+1)
     */
    public void backpropagate(double[] x, double[] ht_1, double[] ct_1, double[] expectedOut, double[] dht1, double[] dCt1, LstmCell cell){
        // needs forward propagation first 
        // if the cell isnt the last, it recieves derivatives of h and c from the next timestep ( t+1 )

        if(expectedOut.length != this.outSize){
            throw new RuntimeException("expectedOut vector size error");
        }
        if (x.length != this.inSize) {
            throw new RuntimeException("x vector size error");
        }
        if (ht_1.length != this.outSize) {
            throw new RuntimeException("ht_1 vector size error");
        }
        if (ct_1.length != this.outSize) {
            throw new RuntimeException("ct_1 vector size error");
        }
        if (dht1.length != this.outSize) {
            throw new RuntimeException("dht1 vector size error");
        }
        if (dCt1.length != this.outSize) {
            throw new RuntimeException("dCt1 vector size error");
        }

        double[] dht = new double[this.outSize];
        double[] dCt = new double[dht.length];
        double[][] dS = new double[4][dCt.length];
        this.dht_1 = new double[this.outSize];
        this.dCt_1 = new double[dht.length];
        
        for(int n = 0; n < this.outSize; n++){
            dht[n] = (this.h[n] - expectedOut[n]) + dht1[n];
        }
        
        for(int n = 0; n < this.outSize; n++){
            dCt[n] = dht[n] * MathV.dtanh(this.c[n]) * this.S[2][n] + dCt1[n];
        }
        
        for(int n = 0; n < this.outSize; n++){
            dS[0][n] = dCt[n] * ct_1[n] * MathV.dsigmNoSigm(this.S[0][n]);
        }
        
        for (int n = 0; n < this.outSize; n++) {
            dS[1][n] = dCt[n] * this.S[3][n] * MathV.dsigmNoSigm(this.S[1][n]);
        }
        
        for (int n = 0; n < this.outSize; n++) {
            dS[2][n] = dht[n] * Math.tanh(this.c[n]) * MathV.dsigmNoSigm(this.S[2][n]);
        }
        
        for (int n = 0; n < this.outSize; n++) {
            dS[3][n] = dCt[n] * this.S[1][n] * MathV.dtanhNoTanh(this.S[3][n]);
        }

        //odvodi glede na vhode 
        double[][] dMlpS; 
        double[][] dMlpSout;

        dMlpS = new double[4][this.inSize];
        dMlpSout = new double[4][this.outSize];

        for (int i = 0; i < dMlpS.length; i++) {
            dMlpS[i] = this.mlpGates[i].backpropagateDarray(dS[i], cell.mlpGates[i]);
            dMlpSout[i] = this.doubleGates[i].backpropagateDarray(dS[i], cell.doubleGates[i]);
        }

        for (int i = 0; i < 4; i++) { // loop trough all mlpGates
            /*for (int n = 0; n < this.inSize; n++) {
                this.dht_1[n] +=  /*dS[i][n] * dMlpS[i][n];
            } <--- dX */
            for (int n = 0; n < this.outSize; n++) {
                this.dht_1[n] +=  dMlpSout[i][n];
            }
        }

        for(int n = 0; n < this.outSize; n++){
            this.dCt_1[n] = dCt[n] * this.S[0][n];
        }
        
    }

    public void backpropagate(double[] x, double[] ht_1, double[] ct_1, double[] expectedOut, LstmCell cell){
        // if cell is last, there is no dCt+1 and dht+1
        backpropagate(x, ht_1, ct_1, expectedOut, new double[this.outSize], new double[this.outSize], cell);
    }

    public void backpropagate(double[] x, double[] ht_1, double[] ct_1, double[] expectedOut) {
        // if cell is last, there is no dCt+1 and dht+1
        backpropagate(x, ht_1, ct_1, expectedOut, this);
    }

    public double errorFunction(double[] x, double[] t) {
        double error;
        x = x.clone();
        t = t.clone();
        if (this.errorFunction.equals("meanSquared")) {
            error = MathV.sum(MathV.div(MathV.pow(MathV.sub(x, t), 2), 2));
        } else {
            throw new RuntimeException("unknown error function setting \"" + this.errorFunction + "\"");
        }

        if(Double.isNaN(error)){
            // throw new RuntimeException("the error is NaN!");
        }
        return error;
    }

    public double[][][][][][] toArray() {
        double[][][][][][] out = new double[4][][][][][];
        out[0] = new double[this.mlpGates.length][][][][];
        for (int i = 0; i < this.mlpGates.length; i++) {
            out[0][i] = this.mlpGates[i].toArray().clone();
        }

        out[1] = new double[this.doubleGates.length][][][][];
        for (int i = 0; i < this.doubleGates.length; i++) {
            out[1][i] = this.doubleGates[i].toArray().clone();
        }

        out[2] = new double[1][1][1][1][];
        out[2][0][0][0][0] = this.ct_1.clone();
        out[3] = new double[1][1][1][1][];
        out[3][0][0][0][0] = this.ht_1.clone();

        return out;
    }

    public LstmCell(int inputSize, int outputSize, double[][][][][][] arr){
        this.inSize = inputSize;
        this.outSize = outputSize;

        this.S = new double[4][outputSize];
        this.doubleGates = new Mlp[4];
        this.mlpGates = new Mlp[4];

        for (int i = 0; i < arr[0].length; i++) {
            this.mlpGates[i] = new Mlp(arr[0][i]);
        }

        for (int i = 0; i < arr[0].length; i++) {
            this.doubleGates[i] = new Mlp(arr[1][i]);
        }

        this.ct_1 = arr[2][0][0][0][0];
        this.ht_1 = arr[3][0][0][0][0];
        this.calcNeurons();
    }

    // 
    // out[0] -> 5D array
    // out[1] -> array of 4D array
    // out[2][0] -> array of 3D arrays
    // out[3][0][0] -> array of 2D arrays
    // out[4][0][0][0] -> array of 1D arrays
    // 
    // out[1] = gates
    //  out[1][0] = gate 0
    //  out[1][1] = gate 1
    //  out[1][2] = gate 2
    //  out[1][3] = gate 3
    // out[2] = 
    // 
    // @return
    // 
    // public double[][][][][][] toArray(){
    // 
    // }
}
