import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Format;

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
            URL resource = getClass().getClassLoader().getResource(filepath);
            if (resource == null) {
                throw new IOException("File not found: " + filepath);
            }
            File file = new File(resource.getFile());

            // Parse the file directly into a MutableGraph object
            this.graph = new Parser().read(file);

        } catch (IOException e) {
            System.err.println("Failed to read DOT file: " + e.getMessage());
        }
    }

    // Method to output graph details
    @Override
    public String toString() {
        if (graph == null) {
            return "No graph available.";
        }

        Set<String> nodes = getNodes();
        Set<String> edges = getEdges();

        StringBuilder details = new StringBuilder();
        details.append("Number of nodes: ").append(nodes.size()).append("\n");
        details.append("Nodes: ").append(nodes).append("\n");
        details.append("Number of edges: ").append(edges.size()).append("\n");
        details.append("Edges: ").append(edges);

        return details.toString();
    }

    // Method to add a single node to the graph
    public void addNode(String nodeName) {
        if (graph == null) {
            graph = Factory.mutGraph();  // Initialize the graph if it wasn't already
        }

        if (graph.nodes().stream().noneMatch(node -> node.name().toString().equals(nodeName))) {
            graph.add(Factory.mutNode(nodeName));
        }
    }

    // Method to remove nodes
    public void removeNode(String label){
        if(graph == null){
            System.out.print("Graph is empty");
            return;
        }
    }

    // Method to add multiple nodes
    public void addNodes(Set<String> nodeNames) {
        if (graph == null) {
            graph = Factory.mutGraph();
        }
        for (String nodeName : nodeNames) {
            addNode(nodeName);
        }
    }

    // Method to add an edge between two nodes
    public void addEdge(String sourceName, String targetName) {
        if (graph == null) {
            graph = Factory.mutGraph();  // Initialize the graph if it’s not already
        }

        // Get or create the source and target nodes
        MutableNode sourceNode = getOrCreateNode(sourceName);
        MutableNode targetNode = getOrCreateNode(targetName);

        // Check if the edge already exists (optional, depending on requirements)
        boolean edgeExists = sourceNode.links().stream()
                .anyMatch(link -> link.to().equals(targetNode));

        if (!edgeExists) {
            // Add a directed link if it doesn't already exist
            sourceNode.addLink(targetNode);
        }
    }


    // Helper method to get or create a node
    private MutableNode getOrCreateNode(String nodeName) {
        for (MutableNode node : graph.nodes()) {
            if (node.name().toString().equals(nodeName)) {
                return node;
            }
        }
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
                    String targetName = extractTargetName(target);
                    edges.add(node.name() + " -> " + targetName);
                }
            }
        }
        return edges;
    }

    // Helper method to clean up target name
    private String extractTargetName(LinkTarget target) {
        String name = target.toString();
        int index = name.indexOf("::");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name.trim();
    }

    // Method to output the graph to a specified DOT file
    public void outputGraph(String filepath) {
        if (graph == null) {
            System.err.println("No graph available to output.");
            return;
        }
        try {
            Graphviz.fromGraph(graph).render(Format.DOT).toFile(new File(filepath));
        } catch (IOException e) {
            System.err.println("Failed to write DOT file: " + e.getMessage());
        }
    }

    // Method to output the graph as a graphic (e.g., PNG)
    public void outputGraphics(String path, String format) {
        if (graph == null) {
            System.err.println("No graph available to output.");
            return;
        }
        try {
            Format outputFormat = format.equalsIgnoreCase("png") ? Format.PNG : null;
            if (outputFormat != null) {
                Graphviz.fromGraph(graph).render(outputFormat).toFile(new File(path));
            } else {
                System.err.println("Unsupported format: " + format);
            }
        } catch (IOException e) {
            System.err.println("Failed to write graphic file: " + e.getMessage());
        }
    }
}
