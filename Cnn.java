import java.util.*;

public class Cnn{

    double[] settings = {
        1, // activate weights with random value  0
        0, // minimum random weight activation    1
        1, // maximum random weight activation    2

        1, // activate biases with random value   3
        0, // minimum random bias activation      4
        1, // maximum random bias activation      5

        1, // activate weights with random value  6
        0, // minimum random weight activation    7
        1  // maximum random weight activation    8
    };

    double[][] neurons; 
    double[][] biases;
    double[][][] synapses;

    public Cnn(int[] dimensions){

        this.neurons = new double[dimensions.length][];
        this.biases = new double[dimensions.length][];
        this.synapses = new double[dimensions.length-1][][];

        for(int i = 0; i < dimensions.length; i++){
            this.neurons[i] = (this.settings[0] == 1)? MathV.randomArray(dimensions[i], this.settings[1], this.settings[2]) : MathV.randomArray(dimensions[i]);
            this.biases[i] = (this.settings[3] == 1) ? MathV.randomArray(dimensions[i], this.settings[4], this.settings[5]) : MathV.randomArray(dimensions[i]);

            if(i < dimensions.length-1){
                synapses[i] = (this.settings[6] == 1)
                        ? MathV.randomArray(dimensions[i+1], dimensions[i], this.settings[7], this.settings[8])
                        : MathV.randomArray(dimensions[i+1], dimensions[i]);
            }
        }

    }
}