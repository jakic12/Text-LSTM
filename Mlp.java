import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Mlp{

    double[] settings = {
        1, // activate weights with random value  0
        0, // minimum random weight activation    1
        1, // maximum random weight activation    2

        1, // activate biases with random value   3
        0, // minimum random bias activation      4
        1, // maximum random bias activation      5

        1, // activation function 1 = sigmoid     6
        1, // error function 1 = quadratic        7
        0.01, // learning rate                    8
        0, // softmax output                      9
        1, // output results to file output.json  10
    };

    double[][] neurons; 
    double[][] biases;
    double[][][] synapses;
    int[] dimensions;
    double error;

    public Mlp(int[] dimensions){
        // constructor, that initializes all the weights

        this.dimensions = dimensions;

        this.neurons = new double[this.dimensions.length][];
        this.biases = new double[this.dimensions.length][];
        this.synapses = new double[this.dimensions.length - 1][][];

        for (int i = 0; i < this.dimensions.length; i++) {
            this.neurons[i] = new double[this.dimensions[i]];

            if (i != 0)
                this.biases[i] = new double[this.dimensions[i]];

            if (i < this.dimensions.length - 1) {
                this.synapses[i] = new double[this.dimensions[i]][this.dimensions[i + 1]];
            }
        }
    }

    public Mlp(int[] dimensions, boolean randomlyAssign) {
        // constructor, so you can randomly assign weights if you can

        this(dimensions);
        if(randomlyAssign){
            randomlySetWeights();
        }
    }

    public void errorFunction(double[] x, double[] t){
        x = x.clone();
        t = t.clone();
        if (this.settings[7] == 1) {
            this.error = MathV.sum(MathV.pow(MathV.sub(x, t), 2));
        }else{
            throw new RuntimeException("unknown error function setting");
        }
    }

    public double[] dErrorFunction(double[] x, double[] t) {
        if(this.settings[7] == 1){
            return MathV.multiplyByDsigmArray(MathV.sub(x, t));
        }
        throw new RuntimeException("unknown error function setting");
    }

    public void randomlySetWeights(){
        // randomly sets weights

        for (int i = 0; i < this.dimensions.length; i++) {
            if (i != 0)
                this.biases[i] = (this.settings[3] == 1)
                        ? MathV.randomArray(this.dimensions[i], this.settings[4], this.settings[5])
                        : new double[this.dimensions[i]];

            if (i < this.dimensions.length - 1) {
                this.synapses[i] = (this.settings[0] == 1)
                        ? MathV.randomArray(this.dimensions[i + 1], this.dimensions[i], this.settings[1], this.settings[2])
                        : new double[this.dimensions[i]][this.dimensions[i + 1]];
            }
        }
    }

    public void forward(double[] input){
        // standard forwardpropagation

        if(this.neurons[0].length != input.length){
            throw new RuntimeException("input and first layer of the network are not the same size");
        }else{
            this.neurons[0] = input;
            
            for(int i = 1; i < this.neurons.length; i++){
                this.neurons[i] = MathV.dot(neurons[i-1],synapses[i-1]);
                this.neurons[i] = MathV.add(neurons[i], biases[i]);

                if(settings[6] == 1)
                    this.neurons[i] = MathV.sigmArray(this.neurons[i]);
            }
        }

        if(this.settings[9] == 1){
            this.neurons[this.neurons.length - 1] = softmax( this.neurons[this.neurons.length - 1] );
        }
    }

    public void forward(double[] input, double[] expOut){
        // forward propagation with expected out, for error calculation

        if(this.neurons[neurons.length-1].length != expOut.length){
            throw new RuntimeException("expected output and last layer of the network are not the same size");
        }else{
            forward(input);
            errorFunction(this.neurons[neurons.length - 1], expOut);
        }
    }

    public double[] eval(double[] input){
        // forwardpropagation that returns last layer

        forward(input);
        return this.neurons[this.neurons.length - 1];
    }

    public static double[] softmax(double[] input){
        // softmax of an array

        double[] out = input.clone();
        for(int i = 0; i < input.length; i++){
            out[i] = Math.exp(out[i]);
        }
        double sumExp = MathV.sum(out);
        for(int i = 0; i < input.length; i++){
            out[i] = out[i] / sumExp;
        }
        return out;
    }

    public void backpropagate(double[] input, double[] expOut){
        // backpropagates the network

        this.forward(input, expOut);
        this.backpropagate(expOut);
        this.forward(input, expOut);
    }
    
    public void backpropagate(double[] expOut) {
        // backpropagates the network
        // needs forward propagation first

        backpropagateDarray(dErrorFunction(this.neurons[this.neurons.length - 1], expOut));
    }

    public double[] backpropagateDarray(double[] out_derivative) {
        // backpropagates the network with a given derivatives of the last layer,
        // and returns the derivative of the input layer

        double[][] dneurons = MathV.emptyLike(this.neurons);

        dneurons[dneurons.length -1] = out_derivative;

        for (int layer = this.neurons.length - 2; layer >= 0; layer--) {
            // calculate 𝛿
            for (int i = 0; i < this.dimensions[layer]; i++) {
                for (int j = 0; j < this.dimensions[layer + 1]; j++) {
                    dneurons[layer][i] += this.synapses[layer][i][j] * dneurons[layer + 1][j];
                }
                dneurons[layer][i] *= MathV.dsigmNoSigm(this.neurons[layer][i]);
                //this.neurons[layer][i] -= dneurons[layer][i];
            }

            for(int i = 0; i < this.dimensions[layer]; i++){
                if(layer != 0)
                    this.biases[layer][i] += -this.settings[8] * dneurons[layer][i];
            }

            // update weights
            for (int i = 0; i < this.dimensions[layer]; i++) {
                for (int j = 0; j < this.dimensions[layer + 1]; j++) {
                    this.synapses[layer][i][j] += -this.settings[8] * dneurons[layer + 1][j] * this.neurons[layer][i];
                }
            }
        }

        return dneurons[0];
    }

    public void learn(double[][] training_in, double[][] training_out, int epochs, int iterations){
        PrintWriter out = null;
        if(this.settings[10] == 1){
            try{
                out = new PrintWriter(new FileWriter("output.json"));
                out.println("{\"errors\":[");
            }catch(IOException e){
                System.out.println("can not write to file output.json");
            }
        }
        for(int epoch = 0; epoch < epochs; epoch++){
            double errSum = 0;
            for (int i = 0; i < training_in.length; i++) {
                for(int iter = 0; iter < iterations; iter++){
                    this.backpropagate(training_in[i], training_out[i]);
                    errSum += this.error;
                }
            }
            errSum = errSum/(training_in.length*iterations);
            if(this.settings[10] == 1 && out != null){
                out.println(this.error + ((epoch != epochs-1)? ", " : ""));
            }
            System.out.println(errSum);
        }
        if (this.settings[10] == 1 && out != null) {
            out.println("]}");
            out.close();
        }

        System.out.println("");
    }
}