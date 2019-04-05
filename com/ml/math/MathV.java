package com.ml.math;

public class MathV{
    public static double[] add(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] += b[i];
        }
        return a;
    }

    public static double[] sub(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] -= b[i];
        }
        return a;
    }

    public static double[] multiply(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] *= b[i];
        }
        return a;
    }

    public static double[] div(double[] a, double[] b){
        if(a.length != b.length){
            throw new RuntimeException("vector length mismatch");
        }
        for(int i = 0; i < b.length; i++){
            a[i] /= b[i];
        }
        return a;
    }

    public static double[] div(double[] a, double b) {
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
                }
            }
        }

        return out;
    }

    public static double sigm(double x){
        return 1 / (1 + Math.exp(-x));
    }

    public static double[] sigmArray(double[] x){
        for(int i = 0; i < x.length; i++){
            x[i] = sigm(x[i]);
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
        double[] out = MathV.emptyLike(x);
        for(int i = 0; i < out.length; i++){
            out[i] = sigm(x[i]) * (1 - sigm(x[i]));   
        }
        return out;
    }

    public static double[] multiplyByDsigmArray(double[] x) {
        double[] out = MathV.emptyLike(x);
        for (int i = 0; i < out.length; i++) {
            out[i] = x[i] * sigm(x[i]) * (1 - sigm(x[i]));
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

}