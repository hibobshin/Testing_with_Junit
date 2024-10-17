import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class DotGraphParser {
    private MutableGraph graph;

    // Method to parse a DOT file and create a graph object
    public void parseGraph(String filepath) {
        try {
            // Use the class loader to locate the file in the resources folder
            URL resource = getClass().getClassLoader().getResource(filepath);
            if (resource == null) {
                throw new IOException("File not found: " + filepath);
            }
            File file = new File(resource.getFile());

            // Parse the file directly into a MutableGraph object
            this.graph = new Parser().read(file);
            System.out.println("Graph parsed successfully!");

            // Output graph details
            outputGraphDetails();

        } catch (IOException e) {
            System.err.println("Failed to read DOT file: " + e.getMessage());
        }
    }

    // Method to output graph details
    private void outputGraphDetails() {
        if (graph == null) {
            System.out.println("No graph available.");
            return;
        }

        Set<String> nodes = getNodes();
        Set<String> edges = getEdges();

        System.out.println("Number of nodes: " + nodes.size());
        System.out.println("Nodes: " + nodes);
        System.out.println("Number of edges: " + edges.size());
        System.out.println("Edges: " + edges);
    }

    // Method to add a node to the graph
    public void addNode(String nodeName) {
        if (graph == null) {
            graph = Factory.mutGraph(); // Initialize the graph if it wasn't already
        }

        // Check if the node already exists
        if (graph.nodes().stream().noneMatch(node -> node.name().toString().equals(nodeName))) {
            graph.add(Factory.mutNode(nodeName));
            System.out.println("Node added: " + nodeName);
        } else {
            System.out.println("Node " + nodeName + " already exists.");
        }
    }

    // Method to add an edge between two nodes
    public void addEdge(String sourceName, String targetName) {
        if (graph == null) {
            graph = Factory.mutGraph(); // Initialize the graph if it wasn't already
        }

        MutableNode sourceNode = getOrCreateNode(sourceName);
        MutableNode targetNode = getOrCreateNode(targetName);

        // Add an edge from source to target
        sourceNode.addLink(targetNode);
        System.out.println("Edge added: " + sourceName + " -> " + targetName);
    }

    // Helper method to get or create a node
    private MutableNode getOrCreateNode(String nodeName) {
        // Look for the node, return it if found
        for (MutableNode node : graph.nodes()) {
            if (node.name().toString().equals(nodeName)) {
                return node;
            }
        }
        // Create a new node if it doesn't exist
        MutableNode newNode = Factory.mutNode(nodeName);
        graph.add(newNode);
        return newNode;
    }

    // Getter for nodes
    public Set<String> getNodes() {
        Set<String> nodes = new HashSet<>();
        if (graph != null) {
            for (MutableNode node : graph.nodes()) {
                nodes.add(node.name().toString());
            }
        }
        return nodes;
    }

    // Getter for edges
    public Set<String> getEdges() {
        Set<String> edges = new HashSet<>();
        if (graph != null) {
            for (MutableNode node : graph.nodes()) {
                for (Link link : node.links()) {
                    LinkTarget target = link.to();
                    if (target instanceof MutableNode) {
                        MutableNode targetNode = (MutableNode) target;
                        edges.add(node.name() + " -> " + targetNode.name());
                    } else if (target instanceof guru.nidi.graphviz.model.Node) {
                        guru.nidi.graphviz.model.Node targetNode = (guru.nidi.graphviz.model.Node) target;
                        edges.add(node.name() + " -> " + targetNode.name().toString());
                    } else {
                        String targetName = extractTargetName(target);
                        edges.add(node.name() + " -> " + targetName);
                    }
                }
            }
        }
        return edges;
    }

    // Method to extract a clean target name from a LinkTarget object
    private String extractTargetName(LinkTarget target) {
        String name = target.toString();
        int index = name.indexOf("::");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name.trim();
    }

    // Main method for testing the new functionality
    public static void main(String[] args) {
        DotGraphParser parser = new DotGraphParser();
        parser.parseGraph("sampleGraph.dot");

        // Test adding nodes and edges
        System.out.println("\nAdding nodes and edges...");
        parser.addNode("E");
        parser.addEdge("A", "E");
        parser.addEdge("E", "D");

        // Print out graph details after additions
        System.out.println("\nGraph details after adding nodes and edges:");
        parser.outputGraphDetails();
    }
}
