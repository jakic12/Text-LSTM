package com.ml.math;

public class MathV{
    public static double[] add(double[] a, double[] b){
        a = a.clone();
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] += b[i];
        }
        return a;
    }

    public static double[] sub(double[] a, double[] b){
        a = a.clone();
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] -= b[i];
        }
        return a;
    }

    public static double[] multiply(double[] a, double[] b){
        a = a.clone();
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] *= b[i];
        }
        return a;
    }

    public static double[] div(double[] a, double[] b){
        a = a.clone();
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] /= b[i];
        }
        return a;
    }

    public static double[] div(double[] a, double b) {
        a = a.clone();
        for (int i = 0; i < a.length; i++) {
            a[i] /= b;
        }
        return a;
    }

    public static double[][] dot(double[][] a, double[][] b){
        double[][] out;
        if(a[0].length != b.length){
            throw new RuntimeException("vector and matrix dimension mismatch");
        }else{
            out = new double[a.length][b[0].length];

            for(int v = 0; v < a.length; v++){

                for(int i = 0; i < b[0].length; i++){
                    for(int j = 0; j < a[0].length; j++){
                        out[v][i] += a[v][j] * b[j][i];
                        if (out[v][i] == Double.NaN)
                            throw new RuntimeException("nan at dot");
                    }
                }

            }
        }

        return out;
    }

    public static double[] dot(double[] a, double[][] b) {
        double[] out;
        if (a.length != b.length) {
            throw new RuntimeException("vector and matrix dimension mismatch");
        } else {
            out = new double[b[0].length];
            for (int i = 0; i < b[0].length; i++) {
                for (int j = 0; j < a.length; j++) {
                    out[i] += a[j] * b[j][i];
                    if(out[i] == Double.NaN)
                        throw new RuntimeException("nan at dot");
                }
            }
        }

        return out;
    }

    public static double sigm(double x){
        return 1 / (1 + Math.exp(-x));
    }

    public static double[] sigmArray(double[] x){
        x = x.clone();
        for(int i = 0; i < x.length; i++){
            x[i] = sigm(x[i]);
        }
        return x;
    }

    public static double[] tanhArray(double[] x){
        double[] out = new double[x.length];
        for(int i = 0; i < out.length ; i++){
            out[i] = Math.tanh(x[i]);
        }
        return out;
    }

    public static double dtanhNoTanh(double x){
        // tanh derivative with input allready tanh'd
        return 1 - Math.pow(x,2);
    }

    public static double dtanh(double x) {
        // tanh derivative with input allready tanh'd
        return 1 - Math.pow(Math.tanh(x), 2);
    }

    public static double[] dtanhArray(double[] x) {
        // tanh derivative with input allready tanh'd
        x = x.clone();
        for (int i = 0; i < x.length; i++) {
            x[i] = dtanh(x[i]);
        }
        return x;
    }

    public static double dsigm(double x){
        return sigm(x) * ( 1 - sigm(x));
    }

    public static double dsigmNoSigm(double x) {
        // dsigmoid with input allready sigmoided
        return x * (1 - x);
    }

    public static double[] dsigmArray(double[] x) {
        x = x.clone();
        double[] out = MathV.emptyLike(x);
        for(int i = 0; i < out.length; i++){
            out[i] = sigm(x[i]) * (1 - sigm(x[i]));   
        }
        return out;
    }

    public static double[] randomArray(int size){
        return randomArray(size, 0d, 1d);
    }

    public static double[] randomArray(int size, double min, double max) {
        double[] out = new double[size];

        for(int i = 0; i < size; i++){
            out[i] = Math.random()*(max-min) + min;
        }

        return out;
    }

    public static double[][] randomArray(int sizeX, int sizeY) {
        return randomArray(sizeX, sizeY, 0, 1);
    }

    public static double[][] randomArray(int sizeX, int sizeY, double min, double max) {
        double[][] out = new double[sizeY][sizeX];

        for(int i = 0; i < out.length; i++){
            for(int j = 0; j < out[i].length; j++){
                out[i][j] = Math.random()*(max-min) + min;
            }
        }

        return out;
    }

    public static double[] emptyLike(double[] x){
        return new double[x.length];
    }

    public static double[][] emptyLike(double[][] x){
        double[][] out = new double[x.length][];
        for(int i = 0; i < out.length; i++){
            out[i] = new double[x[i].length];
        }
        return out;
    }

    public static double sum(double[] x){
        double out = 0;
        for(double i : x){
            out += i;
        }
        return out;
    }

    public static double sum(double[][] x) {
        double out = 0;
        for (double[] i : x) {
            for(double j : i){
                out += j;
            }
        }
        return out;
    }

    public static double round(double x, int n){
        double epsilon = 0.00000001d;
        double multiplied = Math.pow(10,n) * x;
        double remainder = multiplied - Math.floor(multiplied);
        multiplied = Math.floor(multiplied);

        if(Double.compare(1 - remainder, remainder) == 0){
            multiplied += 1;
        }else if(Math.abs((1 - remainder) - remainder) < epsilon){
            multiplied += 1;
        }

        if(1 - remainder < remainder){
            multiplied += 1;
        }

        return multiplied/Math.pow(10,n);
    }

    public static double[] round(double[] x, int n){
        double[] out = x.clone();
        for(int i = 0; i < x.length; i++){
            x[i] = MathV.round(x[i], n);
        }
        return x;
    }

    public static double[][] round(double[][] x, int n) {
        double[][] out = x.clone();
        for (int i = 0; i < x.length; i++) {
            for(int j = 0; j < x[i].length; j++){
                x[i][j] = MathV.round(x[i][j], n);
            }
        }
        return x;
    }

    public static double[] pow(double[] x, int n){
        double[] out = x.clone();
        for(int i = 0; i < x.length; i++){
            out[i] = Math.pow(out[i],n);
        }
        return out;
    }

    public static double limit(double x, double min, double max){
        if(x < min){
            x = min;   
            return x;
        }
        if(x > max){
            x = max;
            return x;
        }
        return x;
    }

    public static double map(double x, double start, double end, double startScaled, double endScaled){
        double maxRel = end - start;
        double xRel = x - start;
        double xFactor = xRel/maxRel;

        double outMaxRel = endScaled - startScaled;
        double outXRel = xFactor * outMaxRel;
        double out = outXRel + startScaled;
        return out;
    }

    public static double[] concat(double[] a, double[] b){
        double[] out = new double[a.length + b.length];
        for(int i = 0; i < out.length; i++){
            out[i] = (i < a.length)? a[i] : b[i-a.length];
        }
        return out;
    }

    public static int maxIndex(double[] x){
        double max = x[0];
        int index = 0;
        for(int i = 1; i < x.length; i++){
            if(x[i] > max){
                max = x[i];
                index = i;
            }
        }
        return index;
    }


    /**
     * Takes in number to represent as vector
     * example:
     *  <code>
     *      vectorifyIndex(2, 5) -> [0,0,1,0,0]
     *  </code>
     * 
     * @param x the idex
     * @param size the size of the output array
     * @return
     */
    public static double[] vectorifyIndex(int x, int size){
        double[] out = new double[size];
        out[x] = 1;
        return out;
    }
    
    /**
     * index of char element in a char array
     * 
     * @param x the array
     * @param a the char to be found
     * @return index of the char in the array
     */
    public static int indexOf(char[] x, char a){
        for(int i = 0; i < x.length; i++){
            if(x[i] == a)
                return i;
        }
        return -1;
    }

    /**
     * Sorts a char array
     * the method clones the object!
     * 
     * @param arr the array to get sorted
     * @return the sorted array
     */
    public static char[] sortArray(char[] arr){
        arr = arr.clone();
        boolean again = false;
        for(int i = 0; i < arr.length-1; i++){
            if(arr[i] > arr[i+1]){
                char temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1] = temp;
                again = true;
            }
        }
        if(again)
            return sortArray(arr);
        return arr;
    }

}