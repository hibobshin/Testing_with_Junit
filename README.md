# CSE-464-2024-amarwah3

# DotGraphParser Project

## Overview
The DotGraphParser project provides functionality for parsing, modifying, and exporting DOT graphs. It allows you to:
- Parse DOT files to create an internal graph representation.
- Add nodes and edges to the graph, including handling duplicates.
- Export the graph as a DOT file or as an image (PNG).

## Features
1. Parsing a DOT File
   - Ability to parse DOT files and load graph data.

2. Add Nodes
   - Add new nodes to the graph.
   - Ensure no duplicate nodes are created.

3. Add Edges
   - Create new edges between nodes.

4. Export Graph
   - Export the graph to a DOT file.
   - Export the graph as a PNG image.

## Installation
To use this project, you need Java and Maven installed.

**Clone the repository:**
```sh
git clone 
```

**Navigate to the project directory:**
```sh
cd CSE-464-2024-amarwah3
```

**Build the project using Maven:**
```sh
mvn clean install
```

## Running The Code

**To Parse the DOT file use:**

```sh
DotGraphParser parser = new DotGraphParser();
parser.parseGraph("sampleGraph.dot");
```

**To add new Nodes and Edges to the graph:**

```sh
parser.addNode("NewNode");
parser.addEdge("NodeA", "NodeB");
```

**To Export the Graph:**
**1. To DOT:**
```sh
parser.outputDOTGraph("outputGraph.dot");
```
**2. To PNG:**
```sh
parser.outputGraphics("outputGraph.png", "png");
```

## To Test

**To test the code run:**
```sh 
mvn test
```


