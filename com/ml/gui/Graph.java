package com.ml.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import java.util.*;

public class Graph{

    private ArrayList<Point> data = new ArrayList<Point>();
    private JPanel panel;

    public Graph(double[] yCoords){
        for(int i = 0; i < yCoords.length; i++){
            data.add(new Point(i,yCoords[i]));
        }
    }

    public void showGraph(String title){
        JFrame frame = new JFrame(title);
        panel = new GraphPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void showGraph(){
        showGraph("graph");
    }
}

class Point{
    public double y;
    public double x;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }    
}

class GraphPanel extends JPanel{
    public void paint( Graphics g ) {
        g.drawLine(100,100,0,0);
    }
}