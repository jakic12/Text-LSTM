package com.ml.other;

public interface ProgressHandler{
    public void progress(int epoch, double error);
    public void end(double error);
}