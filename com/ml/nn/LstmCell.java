package com.ml.nn;

import com.ml.gui.*;
import com.ml.math.MathV;
import com.ml.nn.Mlp;

public class LstmCell {

    // settings
    public double learningRate = 0.01;
    public String errorFunction = "meanSquared";
    
    Mlp[] gates; 
    public double[] c;
    public double[] h;
    public double error;

    private int inSize;
    private int outSize;

    public LstmCell(int inputSize, int outputSize){
        this.inSize = inputSize;
        this.outSize = outputSize;
        int combinedSize = inputSize + outputSize;
        
        gates = new Mlp[4];
        // forget gate
        gates[0] = new Mlp(new int[]{combinedSize, outputSize});
        gates[0].randomlySetWeights();

        // add sigm gate
        gates[1] = new Mlp(new int[]{combinedSize, outputSize});
        gates[1].randomlySetWeights();

        // add candidates
        gates[2] = new Mlp(new int[]{combinedSize, outputSize});
        gates[2].setSetting(6,2);
        gates[2].randomlySetWeights();

        // output gate
        gates[3] = new Mlp(new int[]{combinedSize, outputSize});
        gates[3].randomlySetWeights();
    }

    public void forward(double[] x, double[] h, double[] c){

        if(x.length != this.inSize || h.length != this.outSize || c.length != this.outSize){
            throw new RuntimeException("vector size error");
        }

        c = c.clone();
        double[] combined = MathV.concat(x,h);
        c = MathV.multiply(c, gates[0].eval(combined));
        c = MathV.add(c, MathV.multiply(gates[1].eval(combined), gates[2].eval(combined)));
        this.c = c.clone();
        c = MathV.tanhArray(c);
        this.h = MathV.multiply(c,gates[3].eval(combined));
    }

    public void forward(double[] x, double[] h, double[] c, double[] expected){
        forward(x,h,c);
        this.error = errorFunction(this.h,expected);
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

    public double[] eval(double[] x, double[] h, double[] c) {
        forward(x,h,c);
        return this.h;
    }
}