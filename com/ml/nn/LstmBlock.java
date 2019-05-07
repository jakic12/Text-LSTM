package com.ml.nn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.ml.data.DataManager;
import com.ml.math.*;
import com.ml.other.ProgressHandler;

/**
 * abstaction for binding an lstmchain with its dataset, for easier usage (no
 * manual data transformation needed)
 */
public class LstmBlock implements Serializable{
    public Object trainingData;
    public Object expTrainingData;
    public char[] vocabulary;
    public LstmChain chain;
    public String name;
    public String delimiter;

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

    public LstmBlock(String training_sentences, String delimiter, double learningRate) {
        this.type = "text";
        this.delimiter = delimiter;

        // get only valid training sentences ( those that are not empty)
        String[] training_sentences_arr = training_sentences.split(delimiter);
        ArrayList<String> valid_sentences = new ArrayList<String>();
        for(int i = 0; i < training_sentences_arr.length; i++){
            if(training_sentences_arr[i].length() > 1){
                valid_sentences.add(training_sentences_arr[i]);
            }
        }
        Object[] valid_sentences_arr = valid_sentences.toArray();
        char[][][] data = DataManager.stringToInCharExpChar(Arrays.copyOf(valid_sentences_arr, valid_sentences_arr.length, String[].class));
        this.vocabulary = DataManager.buildCharVocab(training_sentences);

        double[][][] testData = DataManager.vectorifyChar(vocabulary, data[0]);
        double[][][] expData = DataManager.vectorifyChar(vocabulary, data[1]);

        this.trainingData = testData;
        this.expTrainingData = expData;

        this.chain = new LstmChain(new LstmCell(this.vocabulary.length, this.vocabulary.length, learningRate));
        // this.chain.printProgresss = true;
    }

    public void onProgress(ProgressHandler handler) {
        this.chain.onProgress(handler);
    }

    /**
     * trains the cell on its data
     * 
     * @param epochs
     * @param iterations
     */
    public void train(int epochs, int iterations){
        try{
            this.chain.learn((double[][][]) this.trainingData, (double[][][]) this.expTrainingData, epochs, iterations);
        }catch(ClassCastException e){
            System.out.println(e);
            try {
                this.chain.learn(new double[][][]{(double[][]) trainingData}, new double[][][]{(double[][]) expTrainingData}, epochs, iterations);
            } catch (ClassCastException e1) {
                System.out.println(e1);
                e1.printStackTrace();
            }
        }
        //System.out.println("training complete!");
    }

    public String getStringData(){
        String out = "";
        try {
            char[][] charArr = DataManager.vectorToChar((double[][][])this.trainingData, this.vocabulary);
            char[][] expArr = DataManager.vectorToChar((double[][][]) this.expTrainingData, this.vocabulary);

            for(int i = 0; i < charArr.length; i++){
                out += new String(charArr[i]);
                out += expArr[i][expArr[i].length - 1];
                out += this.delimiter;
            }
            
        } catch (ClassCastException e) {
            System.out.println(e);
            try {
                out = new String(DataManager.vectorToChar((double[][]) this.trainingData, this.vocabulary));
                char[] expArr = DataManager.vectorToChar((double[][]) this.expTrainingData, this.vocabulary);

                out += expArr[expArr.length-1];

            } catch (ClassCastException e1) {
                System.out.println(e1);
                e1.printStackTrace();
            }
        } finally {
            return out;
        }

    }

    /**
     * forward n times trought time with the starting letter <code>startChar</code>
     * 
     * @param startChar the first char(must be in vocabulary)
     * @param count how many times to forward
     * @return a string of the sepparate characters
     */
    public String forward(char startChar, int count){
        if(this.type.equals("text")){
            double[][] rawData = this.chain.forward(DataManager.vectorifyChar(this.vocabulary, startChar), count);
            char[] outData = DataManager.vectorToChar(rawData, vocabulary);
            return startChar + new String(outData);
        }else{
            throw new RuntimeException("can not forward a char if the network isnt a text network");
        }
    }


    public String forward(double[] arr, int count){
        if (this.type.equals("text")) {
            double[][] rawData = this.chain.forward(arr, count);
            char[] outData = DataManager.vectorToChar(rawData, vocabulary);
            return this.vocabulary[MathV.maxIndex(arr)] + new String(outData);
        } else {
            throw new RuntimeException("can not forward a char if the network isnt a text network");
        }
    }

    public void stopLearning(){
        this.chain.stopLearning = true;
    }
}