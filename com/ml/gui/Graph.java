package com.ml.gui;

import javax.swing.*;

import com.ml.math.MathV;

import java.awt.*;
import java.awt.geom.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.*;

public class Graph{

    private ArrayList<Point> data = new ArrayList<Point>();
    private ArrayList<Point> scaledData = new ArrayList<Point>();
    private JPanel panel = null;
    private JFrame frame;

    public double maxX = Double.MIN_VALUE;
    private double minX = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;
    private double minY = Double.MAX_VALUE;

    public boolean lockX = true;
    public boolean lockY = false;

    public boolean lockXCamera = false;
    public int padding = 30;

    public Graph(double[] yCoords){
        addData(yCoords);
    }

    public Graph(){

    }

    public void showGraph(String title){
        frame = new JFrame(title);
        panel = new GraphPanel(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500,500));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.repaint();
    }

    public Point scaleCoordinates(Point p){
        Point p1 = p.clone();

        int frameXsize = this.getWidth();
        int frameYsize = this.getHeight();

        if(!this.lockX)
            p1.x = MathV.map(p1.x, this.minX, this.maxX, 0, frameXsize);
        if(!this.lockY)
            p1.y = MathV.map(p1.y, this.minY, this.maxY, frameYsize, 0);

        return p1;
    }

    public Point reverseScaleCoordinates(Point p){
        //pixel to value conversion

        Point p1 = p.clone();

        int frameXsize = this.getWidth();
        int frameYsize = this.getHeight();

        if (!this.lockX)
            p1.x = MathV.map(p1.x, 0, frameXsize, this.minX, this.maxX);
        if (!this.lockY)
            p1.y = MathV.map(p1.y, 0, frameYsize, this.maxY,this.minY);

        return p1;
    }

    public ArrayList<Point> scaleCoordinates(ArrayList<Point> p) {
        ArrayList<Point> out = new ArrayList<Point>();
        for(int i = 0; i < p.size(); i++){
            if(p != null)
                out.add(this.scaleCoordinates(p.get(i)));
        }
        return out;
    }

    public void scaleCoordinates() {
        this.scaledData = this.scaleCoordinates(this.data);
    }

    public void showGraph(){
        showGraph("graph");
    }

    public ArrayList<Point> getData(){
        return this.data;
    }

    public ArrayList<Point> getScaledData(){
        this.scaleCoordinates();
        return this.scaledData;
    }

    public void addData(double[] yCoords) {
        for (int i = 0; i < yCoords.length; i++) {
            addData(new Point(this.data.size(), yCoords[i]));
        }
    }

    public void addData(double yCoord){
        this.addData(new double[]{yCoord});
    }

    public void addData(Point[] p){
        for (int i = 0; i < p.length; i++) {
            data.add(p[i]);
            this.calcBorder(p[i]);
            
            if(this.getPanel() != null)
                this.getPanel().repaint();
        }
    }

    public void addData(Point p){
        this.addData(new Point[]{p});
    }

    public JFrame getFrame(){
        return this.frame;
    }

    public int getHeight(){
        return this.frame.getHeight() - 30;
    }

    public int getWidth(){
        return this.frame.getWidth();
    }

    public JPanel getPanel(){
        return this.panel;
    }

    private void calcBorder(Point p){
        if(p.x > maxX){
            if(maxX < 0){
                maxX = 0;
            }
            maxX = p.x;
        }
        if(p.x < minX && p.x < 0){
            if(minX > 0){
                minX = 0;
            }
            minX = p.x;
        }
        if (p.y > maxY) {
            if (maxY < 0) {
                maxY = 0;
            }
            maxY = p.y;
        }
        if (p.y < minY) {
            if (minY > 0) {
                minY = 0;
            }
            minY = p.y;
        }
    }
}

class Point{
    public double y;
    public double x;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }    

    public Point clone(){
        return new Point(this.x, this.y);
    }

    public Point add(Point p){
        this.x += p.x;
        this.y += p.y;
        return this;
    }

    public Point sub(Point p) {
        this.x -= p.x;
        this.y -= p.y;
        return this;
    }

    public double distance(Point p){
        Point relative = Point.sub(this,p);
        return Math.sqrt( Math.pow(relative.x, 2) + Math.pow(relative.y, 2));
    }

    public static Point sub(Point p, Point p1){
        return p.clone().sub(p1);
    }

    public static Point add(Point p, Point p1){
        return p.clone().add(p1);
    }
}

class GraphPanel extends JPanel{
    Graph graphObject = null;
    Point start = null;

    public Point screenPosition;
    private double zoom;

    public GraphPanel(Graph x){
        this.graphObject = x;
        this.screenPosition = new Point(0,0);
        this.zoom = 1;

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                if(start != null){
                    Point end = new Point(evt.getX(), evt.getY());
                    Point difference = Point.sub(end,start);
                    screenPosition.add(difference);
                    start.x = end.x;
                    start.y = end.y;
                    System.out.println(screenPosition.x + " " + screenPosition.y);
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt){
                start = new Point(evt.getX(), evt.getY());
            }

            public void mouseReleased(MouseEvent evt) {
                start = null;
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            public void mouseWheelMoved(MouseWheelEvent evt){
                zoom += evt.getWheelRotation()*-0.05;
                if(zoom < 0){
                    zoom = 0;
                }
                repaint();
            }
        });

    }

    public Point transformToView(Point p){
        Point p1 = p.clone();
        p1.x *= this.zoom;
        p1.y *= this.zoom;

        p1.x += screenPosition.x;
        p1.y += screenPosition.y;

        return p1;
    }

    public Point transformFromView(Point p){
        Point p1 = p.clone();

        p1 = Point.sub(p1, screenPosition);

        p1.x /= this.zoom;
        p1.y /= this.zoom;
        return p1;
    }

    public ArrayList<Point> transformToView(ArrayList<Point> p) {
        ArrayList<Point> out = new ArrayList<Point>();
        for(Point p1 : p){
            out.add(this.transformToView(p1));
        }
        return out;
    }

    public void paint( Graphics g ) {
        super.paint(g);
        if(graphObject != null){
            if(graphObject.lockXCamera)
                if(graphObject.maxX  > screenPosition.x + graphObject.getFrame().getWidth())
                    screenPosition.x -= graphObject.maxX - (screenPosition.x + graphObject.getFrame().getWidth());

            ArrayList<Point> scaledData = this.transformToView(graphObject.getScaledData());
            drawCoordinateSystem(g);
            for(int i = 0; i < scaledData.size() - 1; i++){
                //check if point is out of padding bounds
                if(!(scaledData.get(i).x < graphObject.padding || scaledData.get(i).x > graphObject.getFrame().getWidth() - (graphObject.padding*2)
                || scaledData.get(i).y < graphObject.padding || scaledData.get(i).y > graphObject.getHeight() -(graphObject.padding*2)))
                    g.drawLine((int)scaledData.get(i).x, (int)scaledData.get(i).y, (int)scaledData.get(i+1).x, (int)scaledData.get(i+1).y);
            }
        }
    }

    private void drawCoordinateSystem( Graphics g ){
        Point zero = this.transformToView(graphObject.scaleCoordinates(new Point(0,0)));
        if (!(zero.x < graphObject.padding || zero.x > graphObject.getFrame().getWidth() - (graphObject.padding*2)))
            g.drawLine((int)zero.x, graphObject.padding, (int)zero.x, graphObject.getFrame().getSize().height-(graphObject.padding*2)-30);
        if (!(zero.y < graphObject.padding || zero.y > graphObject.getHeight() - (graphObject.padding*2)))
            g.drawLine(graphObject.padding,(int)zero.y, graphObject.getFrame().getSize().width-(graphObject.padding*2),(int) zero.y);

        if(graphObject.lockX){
            int segmentCount = 10;
            for(int i = graphObject.padding; i < graphObject.getWidth() - (graphObject.padding*2) ; i += ((graphObject.getWidth() - (graphObject.padding * 2)) - graphObject.padding)/segmentCount){
                Point xCoord = new Point(i,graphObject.getHeight()-(graphObject.padding*2)+10);

                String value = (graphObject.reverseScaleCoordinates(transformFromView(xCoord)).x + "");
                int maxLength = (value.length() < 5) ? value.length() : 5;
                value = value.substring(0, maxLength);

                g.drawLine((int)xCoord.x, (int)xCoord.y, (int)xCoord.x, (int)xCoord.y+10);
                g.drawString(value, (int)xCoord.x - 10, (int)xCoord.y+25);
            }

            for(int i = graphObject.padding; i < graphObject.getHeight() - (graphObject.padding*2) ; i += ((graphObject.getHeight() - (graphObject.padding * 2)) - graphObject.padding)/segmentCount){
                Point xCoord = new Point(10, i);

                String value = (graphObject.reverseScaleCoordinates(transformFromView(xCoord)).y + "");
                int maxLength = (value.length() < 5) ? value.length() : 5;
                value = value.substring(0, maxLength);

                g.drawLine((int)xCoord.x, (int)xCoord.y, (int)xCoord.x+10, (int)xCoord.y);
                g.drawString(value, (int)xCoord.x-10, (int)xCoord.y+15);
            }
        }
    }
}