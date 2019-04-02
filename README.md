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
    0.01 // learning rate                     8  
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

disable random bias activation
```java
testMlp.settings[3] = 0;
```

initialize the weights
```java
testMlp.randomlySetWeights();
```

forwardPropagate an input ( output is stored in the last layer )
```java
testMlp.forward(new double[]{1,0,1});
double[] out = testMlp.neurons[testMlp.neurons.length-1];
```

forwardPropagate an input ( output is returned from method )
```java
double[] out = testMlp.eval(new double[]{1,0,1});
```

backpropagate ( forward propagation is required before )
```java
double[] targetOutput = new double[]{1,1,0};
testMlp.backpropagate(targetOutput);
```

backpropagate ( forward propagation isn't required before )
```java
double[] exampleInput = new double[]{1,0,1};
double[] targetOutput = new double[]{1,1,0};
testMlp.backpropagate(exampleInput,targetOutput);
```
