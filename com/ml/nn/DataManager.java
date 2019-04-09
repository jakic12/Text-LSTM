package com.ml.nn;

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
     * vectorifies the char
     * 
     * @param vocab the vocabulary
     * @param data the chars to be represented as array
     * @return the vector that shows the position of the char in the array
     */
    public static double[][] vectorifyChar(char[] vocab, char[] data){
        double[][] out = new double[data.length][];

        for(int i = 0; i < data.length; i++){
            out[i] = MathV.vectorifyIndex(MathV.indexOf(vocab, data[i]), vocab.length);
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
    }

    
}