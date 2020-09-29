<img width="600" alt="graphview" src="https://user-images.githubusercontent.com/53358116/94511906-49c9b980-0223-11eb-8bb7-1508dd8dc44a.png">

A library for drawing Unlinear Graphs

## Gradle Setup

```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

```gradle
dependencies {
    implementation 'com.github.vk59:GraphView-Library:2.0.1'
}
```

## Quick start
#### 1. Firstly, you have to declare a variable of GraphData type. It's necessary to show your data correctly.
The first parameter is ArrayList of Moments (every Moment contain X and Y coordinate of the point on Graph).
But it isn't necessary to fill the ArrayList right now (but you can)
```java
private graphData = new GraphData(new ArrayList<Moment>, Color.rgb(61, 244, 165), "Graph №1");
```

#### 2. OK, it's pretty easy. Then we have to fill our GraphData with Moments. It's easy too:
```java
graphData.addData(X, Y);
```

#### 3. There is float variable  X and Y. Or you can add several data using this method:
```java
graphData.setData(arrayListOfMoments);
```

#### 4. Now we are going to draw our data in the GraphView.
Of course, before that you have to add GraphView to your layout and find that in the code.
```java
private GraphView graphView = findViewById(R.id.graphView);
graphView.addGraphData(graphData);
graphView.drawGraph();
```
That's all! Your graph is on the display.

#### 5. If you want to clear your graph, write 
```java
graphView.clear;
```
Now graphView forget about all the GraphData objects. Of course, your Data is still saved (in graphData variable). You can add new Moment and draw that again.

#### 6. If you want to draw several graphs, you should declare several GraphData variables and add that to GraphView:
```java
graphView.addGraphData(graphData);
graphView.addGraphData(graphData2);
graphView.addGraphData(graphData3);
```

## Examples of using

This library generally was used in the 'ISC' application. You can see the code [here](https://github.com/infolab-club/isc)

### Typical example of unlinear chart
![isc1](https://user-images.githubusercontent.com/53358116/94512558-fbb5b580-0224-11eb-9813-f0790d1d64f0.jpg)

### Real time drawing
![isc2](https://user-images.githubusercontent.com/53358116/94512564-fce6e280-0224-11eb-967e-a1475db4d78f.jpg)

### Drawing several graphs on one chart
![isc3](https://user-images.githubusercontent.com/53358116/94512566-fe180f80-0224-11eb-8cfc-72e38dfc012d.jpg)

##### Created by Ivan Kostylev
