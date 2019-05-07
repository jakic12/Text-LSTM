package com.ml.data;

import com.ml.math.*;

/**
 * class for managing data to pass to the neural networks
 */
public class DataManager{
    /**
     * splits up String into expected input and output
     * example:
     *      input: "test"
     *      output: [
     *                  ['t','e','s'],
     *                  ['e','s','t']
     *              ]
     * 
     * @param x string to be split up
     * @return 2 char arrays that include 1) input data 2) expected output data
     */
    public static char[][] stringToInCharExpChar(String x){
        char[][] out = new char[2][x.length()-1];
        for(int i = 0; i < x.length()-1; i++){
            out[0][i] = x.charAt(i);
            out[1][i] = x.charAt(i+1);
        }
        return out;
    }

    /**
     * the same as {@link #stringToInCharExpChar(String)}, but for an array of Strings
     * @param x the sentences
     * @return 2 char arrays that include 1) input datasets 2) expected output datasets
     */
    public static char[][][] stringToInCharExpChar(String[] x) {
        char[][][] out = new char[2][x.length][];
        for(int i = 0; i < x.length; i++){
            char[][] temp = stringToInCharExpChar(x[i]);
            out[0][i] = temp[0];
            out[1][i] = temp[1];
        }
        return out;
    }

    /**
     * vectorifies the char
     * 
     * @param vocab the vocabulary
     * @param data  the chars to be represented as array
     * @return the vector that shows the position of the char in the array
     */
    public static double[] vectorifyChar(char[] vocab, char data) {
        return MathV.vectorifyIndex(MathV.indexOf(vocab, data), vocab.length);
    }

    
    public static double[][] vectorifyChar(char[] vocab, char[] data){
        double[][] out = new double[data.length][];

        for(int i = 0; i < data.length; i++){
            out[i] = vectorifyChar(vocab, data[i]);
        }
        return out;
    }
    
    public static double[][][] vectorifyChar(char[] vocab, char[][] data) {
        double[][][] out = new double[data.length][][];

        for(int i = 0; i < data.length; i++){
            out[i] = vectorifyChar(vocab, data[i]);
        }
        
        return out;
    }

    /**
     * the same as {@link #vectorifyChar(char[], char[])} but accepts strings
     */
    public static double[][] vectorifyChar(char[] vocab, String data) {
        double[][] out = new double[data.length()][];

        for (int i = 0; i < data.length(); i++) {
            out[i] = MathV.vectorifyIndex(MathV.indexOf(vocab, data.charAt(i)), vocab.length);
        }
        return out;
    }

    public static char[] getUniqueChars(String text){
        String chars = "";
        for (int i = 0; i < text.length(); i++) {
            if( chars.indexOf(text.charAt(i)) == -1){
                chars += text.charAt(i);
            }
        }
        return chars.toCharArray();
    }

    public static char[] buildCharVocab(String x){
        char[] out = getUniqueChars(x);
        return MathV.sortArray(out);
    }

    /**
     * lstm ouput vector to char
     * @param arr the output
     * @param vocab the vocabulary
     * @return the char that the vector represents
     */
    public static char vectorToChar(double[] arr, char[] vocab){
        return vocab[MathV.maxIndex(arr)];
    }

    /**
     * lstm ouputs vector to char array
     * 
     * @param arr the outputs
     * @param vocab the vocabulary
     * @return the chars that the vector represents
     */
    public static char[] vectorToChar(double[][] arr, char[] vocab) {
        char[] out = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            out[i] = vectorToChar(arr[i], vocab);
        }
        return out;
    }

    public static char[][] vectorToChar(double[][][] arr, char[] vocab){
        char[][] out = new char[arr.length][];
        for(int i = 0; i < arr.length; i++){
            out[i] = vectorToChar(arr[i], vocab);
        }
        return out;
    }

}