# MLP -> MultiLayeredPerceptron

## Default settings object
```java
double[] settings = {
    1, // activate weights with random value  0
    0, // minimum random weight activation    1
    1, // maximum random weight activation    2
    1, // activate biases with random value   3
    0, // minimum random bias activation      4
    1, // maximum random bias activation      5
    1, // activation function 1 = sigmoid     6
    1, // error function 1 = quadratic        7
    0.005, // learning rate                   8
    0, // softmax output                      9
    0, // output results to file output.json  10
    1, // Graph data                          11
    10, // graph every n epochs             12
};
```

## Structures

### neurons
An array the size of layer count, wich consists of neuron vectors

```java
neurons[ layer ][ this_layer ]
```

### synapses
An array the size of layer count -1. Here there are stored synapses matrices that connect from this layer, to the next one.

```java
neurons[ layer ][ this_layer ][ next_layer ]
```

## Example usage

create a network with 4 layers, sizes: 3,2,2,3
```java
Mlp testMlp = new Mlp(new int[]{3,2,2,3});
```

##### disable random bias activation
```java
testMlp.settings[3] = 0;
```

##### initialize the weights
```java
testMlp.randomlySetWeights();
```

##### forwardPropagate an input ( output is stored in the last layer )
```java
testMlp.forward(new double[]{1,0,1});
double[] out = testMlp.neurons[testMlp.neurons.length-1];
```

##### forwardPropagate an input ( output is returned from method )
```java
double[] out = testMlp.eval(new double[]{1,0,1});
```

##### backpropagate ( forward propagation is required before )
```java
double[] targetOutput = new double[]{1,1,0};
testMlp.backpropagate(targetOutput);
```

##### backpropagate ( forward propagation isn't required before )
```java
double[] exampleInput = new double[]{1,0,1};
double[] targetOutput = new double[]{1,1,0};
testMlp.backpropagate(exampleInput,targetOutput);
```

##### learn a dataset   
lets make it learn XOR  
inputs   outputs  
[[0,0], -> [[0],  
 [0,1], ->  [1],  
 [1,0], ->  [1],  
 [1,1]] ->  [0]]  
1000 epochs, 100 iterations per training example
```java
testMlp1.learn(new double[][]{{0,0}, {0,1}, {1,0}, {1,1}}, new double[][]{{0}, {1}, {1}, {0}}, 1000, 100);
```

# Graph -> GraphingTool

This is a graphing library which is kinda bad.

## Example usage

##### Create new graph object
you can create the Graph object empty or with data

```java
Graph testGraph = new Graph();
```
or
```java
Graph testGraph = new Graph(new double[]{5,5,4,4,4,3,3,3,3,2,2,2,2,2,1,1,1,1,1});
```

##### Show the graph
```java
testGraph.showGraph("graph title");
```
##### Add data
You can add data and the graph will update  
You can add data as a `Point` object:
```java
testGraph.addData(new Point(19,10));
```
or as a value
```java
testGraph.addData(10);
```
or as an array of any of the two
