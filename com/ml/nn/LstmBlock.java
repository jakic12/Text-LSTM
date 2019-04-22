package com.ml.nn;

import com.ml.data.DataManager;
import com.ml.math.*;

/**
 * abstaction for binding an lstmchain with its dataset, for easier usage (no
 * manual data transformation needed)
 */
public class LstmBlock{
    public Object trainingData;
    public Object expTrainingData;
    public char[] vocabulary;
    public LstmChain chain;

    private String type;

    public LstmBlock(String training_sentence, double learningRate){
        this.type = "text";

        char[][] data = DataManager.stringToInCharExpChar(training_sentence);
        this.vocabulary = DataManager.buildCharVocab(training_sentence);

        double[][] testData = DataManager.vectorifyChar(vocabulary, data[0]);
        double[][] expData = DataManager.vectorifyChar(vocabulary, data[1]);

        this.trainingData = testData;
        this.expTrainingData = expData;

        this.chain = new LstmChain(new LstmCell(this.vocabulary.length, this.vocabulary.length, learningRate));
        //this.chain.printProgresss = true;
    }

    /**
     * trains the cell on its data
     * 
     * @param epochs
     * @param iterations
     */
    public void train(int epochs, int iterations){
        try{
            this.chain.learn((double[][][])trainingData, (double[][][])expTrainingData, epochs, iterations);
        }catch(Exception e){
            System.out.println(e);
            try {
                this.chain.learn(new double[][][]{(double[][]) trainingData}, new double[][][]{(double[][]) expTrainingData}, epochs, iterations);
            } catch (Exception e1) {
                System.out.println(e1);
            }
        }
        //System.out.println("training complete!");
    }

    public String forward(char startChar, int count){
        if(this.type.equals("text")){
            double[][] rawData = this.chain.forward(DataManager.vectorifyChar(this.vocabulary, startChar), count);
            char[] outData = DataManager.vectorToChar(rawData, vocabulary);
            return startChar + new String(outData);
        }else{
            throw new RuntimeException("can not forward a char if the network isnt a text network");
        }
    }
}