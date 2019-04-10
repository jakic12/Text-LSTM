package com.ml.nn;

import com.ml.gui.*;
import com.ml.math.MathV;
import com.ml.nn.Mlp;

public class LstmCell {

    // settings
    public double learningRate = 0.01;
    public String errorFunction = "meanSquared";
    public String gateMode = "weight"; // weight / mlp
    
    public Mlp[] mlpGates; 
    public double[][] weightGates;
    public double[] c;
    public double[] h;
    public double error;

    public double[] dCt_1;
    public double[] dht_1;

    private int inSize;
    private int outSize;

    public int[] mlpDimensions;

    public LstmCell(int inputSize, int outputSize){
        this.inSize = inputSize;
        this.outSize = outputSize;
        int combinedSize = inputSize + outputSize;

        this.mlpDimensions = new int[]{combinedSize,3, outputSize};

        // this.mlpGates[0] -> S0
        // this.mlpGates[1] -> S1
        // this.mlpGates[2] -> S2
        // this.mlpGates[3] -> T0
        
        mlpGates = new Mlp[4];
        // forget gate
        mlpGates[0] = new Mlp(this.mlpDimensions);
        mlpGates[0].randomlySetWeights();

        // add sigm gate
        mlpGates[1] = new Mlp(this.mlpDimensions);
        mlpGates[1].randomlySetWeights();

        // add candidates
        mlpGates[3] = new Mlp(this.mlpDimensions);
        mlpGates[3].setSetting(6,2);
        mlpGates[3].randomlySetWeights();

        // output gate
        mlpGates[2] = new Mlp(this.mlpDimensions);
        mlpGates[2].randomlySetWeights();

        for(int i = 0; i < 4; i++){ // for all mlpGates
            mlpGates[i].setSetting(1,-1);
            mlpGates[i].setSetting(8, this.learningRate);
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
        }
    }
    public void forward(double[] x, double[] h, double[] c){

        if(x.length != this.inSize || h.length != this.outSize || c.length != this.outSize){
            throw new RuntimeException("vector size error");
        }

        c = c.clone();
        double[] combined = MathV.concat(x,h);
        c = MathV.multiply(c, mlpGates[0].eval(combined));
        c = MathV.add(c, MathV.multiply(mlpGates[1].eval(combined), mlpGates[2].eval(combined)));
        this.c = c.clone();
        c = MathV.tanhArray(c);
        this.h = MathV.multiply(c,mlpGates[3].eval(combined));
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
     * function is a <code>void</code> itself, but the values {@link LstmCell.dCt_1}
     * and {@link LstmCell.dht_1} are updated for further use
     * 
     * @param x           the input for this time step (t)
     * @param ht_1        the output of the previous time step (t-1)
     * @param ct_1        the memory of the previous time step (t-1)
     * @param expectedOut the expected output of the cell at the timestep (t)
     * @param dht1        the derivative of the output of the next time step (t+1)
     * @param dCt1        the derivative of the memory of the next time step (t+1)
     */
    public void backpropagate(double[] x, double[] ht_1, double[] ct_1, double[] expectedOut, double[] dht1, double[] dCt1){
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
            dCt[n] = dht[n] * MathV.dtanh(this.c[n]) * this.mlpGates[2].getOut()[n] + dCt1[n];
        }
        
        for(int n = 0; n < this.outSize; n++){
            dS[0][n] = dCt[n] * ct_1[n];
        }
        
        for (int n = 0; n < this.outSize; n++) {
            dS[1][n] = dCt[n] * this.mlpGates[2].getOut()[n];
        }
        
        for (int n = 0; n < this.outSize; n++) {
            dS[2][n] = dht[n] * Math.tanh(this.c[n]);
        }
        
        for (int n = 0; n < this.outSize; n++) {
            dS[3][n] = dCt[n] * this.mlpGates[1].getOut()[n];
        }

        //odvodi glede na vhode 
        double[][] dMlpS = new double[4][this.outSize + this.inSize]; 
        for(int i = 0; i < dMlpS.length; i++){
            dMlpS[i] = this.mlpGates[i].backpropagateDarray(dS[i]);
        }
        
        for(int i = 0; i < 4; i++){ // loop trough all sigmoid mlpGates
            for (int n = 0; n < this.outSize; n++) {
                this.dht_1[n] += dS[i][n] * dMlpS[i][n];
            }   
        }
        
        for(int n = 0; n < this.outSize; n++){
            this.dCt_1[n] = this.c[n] * this.mlpGates[0].getOut()[n];
        }
        
    }

    public void backpropagate(double[] x, double[] ht_1, double[] ct_1, double[] expectedOut){
        // if cell is last, there is not dCt+1 and ht+1
        backpropagate(x, ht_1, ct_1, expectedOut, new double[this.outSize], new double[this.outSize]);
    }

    public double errorFunction(double[] x, double[] t) {
        x = x.clone();
        t = t.clone();
        if (this.errorFunction == "meanSquared") {
            return MathV.sum(MathV.div(MathV.pow(MathV.sub(x, t), 2), 2));
        } else {
            throw new RuntimeException("unknown error function setting");
        }
    }
}