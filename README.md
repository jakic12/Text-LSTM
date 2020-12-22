# Math docs
All of the math i did is written here
[LSTM1.docx](LSTM1.docx)

# Running the gui
run the file `LstmGui/dist/LstmGui.jar`

# The library
##### MLP -> MultiLayeredPerceptron
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
    0, // Graph data                          11
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

# Graph

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

# DataManager
DataManager is made, to convert from meaningfull data, to Lstm trainable data

## Example usage
### text
##### build a vocabulary
```java
char[] vocabulary = DataManager.buildCharVocab("test sentence1. Test sentence2");
```

##### convert a sentence to lstm data
```java
//TODO check if posible
```

##### convert multiple sentences to lstm data
```java
String[] testData = new String[]{"test sentence1", "Test sentence2"};
char[][][] data = DataManager.stringToInCharExpChar(testData);
char[] vocabulary = DataManager.buildCharVocab("test sentence1. Test sentence2");

double[][][] testTestData = DataManager.vectorifyChar(vocabulary, data[0]);
double[][][] testExpData = DataManager.vectorifyChar(vocabulary, data[1]);
```

##### convert vectorized sentence to chars
```java
char[] outChar = DataManager.vectorToChar(out, vocabulary);
```

# LstmCell
This object is an abstract lstm cell with 4 gates

## Example usage

##### Create new cell
```java
int inputSize = 2;
int outputSize = 3;
LstmCell cell1 = new LstmCell(inputSize, outputSize);
```

# LstmChain
Made for orchestraiting and training an lstmcell

## Example usage
##### create new chain
```java
LstmChain chain = new LstmChain(cell1);
```

##### Make the cell learn on some data on 10000 epochs and 100 iterations
```java
// example data [0,0,1] -> [0,1,0]
//              [0,1,0] -> [1,0,0]
double[][] testTestData = new double[][]{{0,0,1},{0,1,0}};
double[][] testExpData = new double[][]{{0,1,0},{1,0,0}};

chain.learn(testTestData, testExpData, 10000, 100);
```

##### Generate 1000 sets of data, with the first data being [0,0,1]
```java
double[][] out = chain.forwardWithVectorify([0,0,1], 1000);
```
