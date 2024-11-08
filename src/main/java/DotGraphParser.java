import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.Format;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

// Enum to select search algorithm
enum Algorithm {
    BFS, DFS
}

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
    public void removeNode(String label) {
        if (graph == null) {
            System.err.println("Graph is not initialized.");
            return;
        }

        // Check if the node exists in the graph before proceeding
        boolean nodeExists = graph.nodes().stream()
                .anyMatch(node -> node.name().toString().equals(label));

        if (!nodeExists) {
            throw new IllegalArgumentException("Node " + label + " does not exist in the graph.");
        }

        // Copy nodes and edges to a new structure
        Set<String> nodesToKeep = new HashSet<>();
        List<String[]> edgesToKeep = new ArrayList<>();

        for (MutableNode node : graph.nodes()) {
            if (!node.name().toString().equals(label)) {
                nodesToKeep.add(node.name().toString());
                for (Link link : node.links()) {
                    if (!link.to().name().toString().equals(label)) {
                        edgesToKeep.add(new String[]{node.name().toString(), link.to().name().toString()});
                    }
                }
            }
        }

        // Clear the graph completely and recreate it without the target node
        graph = Factory.mutGraph().setDirected(true);

        // Re-add nodes
        for (String nodeName : nodesToKeep) {
            graph.add(Factory.mutNode(nodeName));
        }

        // Re-add edges
        for (String[] edge : edgesToKeep) {
            MutableNode sourceNode = getOrCreateNode(edge[0]);
            MutableNode targetNode = getOrCreateNode(edge[1]);
            sourceNode.addLink(targetNode);
        }

        System.out.println("Node " + label + " removed successfully.");
    }

    //Method to remove nodes
    public void removeNodes(String[] labels) {
        if (graph == null) {
            System.err.println("Graph is not initialized.");
            return;
        }

        // Iterate over each label and call removeNode for each one
        for (String label : labels) {
            try {
                removeNode(label);  // Reuse removeNode method to handle each node
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping removal for non-existing node: " + label);
            }
        }

        System.out.println("Nodes " + String.join(", ", labels) + " removed successfully.");
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

    public void removeEdge(String srcLabel, String dstLabel) {
        if (graph == null) {
            System.err.println("Graph is not initialized.");
            return;
        }

        // Locate the source node
        MutableNode sourceNode = graph.nodes().stream()
                .filter(node -> node.name().toString().equals(srcLabel))
                .findFirst()
                .orElse(null);

        // Locate the destination node
        MutableNode targetNode = graph.nodes().stream()
                .filter(node -> node.name().toString().equals(dstLabel))
                .findFirst()
                .orElse(null);

        // Check if source and destination nodes exist
        if (sourceNode == null) {
            throw new IllegalArgumentException("Source node " + srcLabel + " does not exist in the graph.");
        }
        if (targetNode == null) {
            throw new IllegalArgumentException("Destination node " + dstLabel + " does not exist in the graph.");
        }

        // Check if the edge exists and remove it
        boolean edgeRemoved = sourceNode.links().removeIf(link -> link.to().name().toString().equals(dstLabel));

        if (!edgeRemoved) {
            throw new IllegalArgumentException("Edge from " + srcLabel + " to " + dstLabel + " does not exist in the graph.");
        }

        System.out.println("Edge from " + srcLabel + " to " + dstLabel + " removed successfully.");
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
        System.out.println("Current nodes in graph: " + nodes);
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
    private String extractTargetName(Object target) {
        String name = target.toString();
        name = name.replaceAll("->.*", ""); // Remove arrow symbols and connections
        name = name.replaceAll("\\{.*?\\}", ""); // Remove curly braces
        name = name.replace("::", "").trim(); // Remove "::" specifically
        return name;
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

    public Path GraphSearch(String srcLabel, String dstLabel, Algorithm algo) {
        if (graph == null) {
            System.err.println("Graph is not initialized.");
            return null;
        }

        Object sourceNode = findNodeByName(srcLabel);
        Object destinationNode = findNodeByName(dstLabel);
        if (sourceNode == null || destinationNode == null) {
            System.err.println("Source or destination node not found in the graph.");
            return null;
        }

        return algo == Algorithm.BFS ? bfsSearch(sourceNode, destinationNode) : dfsSearch(sourceNode, destinationNode);
    }

    private Path bfsSearch(Object sourceNode, Object destinationNode) {
        Queue<Object> queue = new LinkedList<>();
        Map<Object, Object> parentMap = new HashMap<>();
        Set<Object> visited = new HashSet<>();

        queue.add(sourceNode);
        visited.add(sourceNode);

        while (!queue.isEmpty()) {
            Object currentNode = queue.poll();

            if (currentNode.equals(destinationNode)) {
                return reconstructPath(sourceNode, destinationNode, parentMap);
            }

            for (Link link : getLinks(currentNode)) {
                Object neighbor = link.to();
                String neighborLabel = extractTargetName(neighbor);
                Object neighborNode = findNodeByName(neighborLabel);

                if (neighborNode != null && !visited.contains(neighborNode)) {
                    visited.add(neighborNode);
                    parentMap.put(neighborNode, currentNode);
                    queue.add(neighborNode);
                }
            }
        }
        return null; // No path found
    }

    private Path dfsSearch(Object sourceNode, Object destinationNode) {
        Stack<Object> stack = new Stack<>();
        Map<Object, Object> parentMap = new HashMap<>();
        Set<Object> visited = new HashSet<>();

        stack.push(sourceNode);
        visited.add(sourceNode);

        while (!stack.isEmpty()) {
            Object currentNode = stack.pop();

            if (currentNode.equals(destinationNode)) {
                return reconstructPath(sourceNode, destinationNode, parentMap);
            }

            for (Link link : getLinks(currentNode)) {
                Object neighbor = link.to();
                String neighborLabel = extractTargetName(neighbor);
                Object neighborNode = findNodeByName(neighborLabel);

                if (neighborNode != null && !visited.contains(neighborNode)) {
                    visited.add(neighborNode);
                    parentMap.put(neighborNode, currentNode);
                    stack.push(neighborNode);
                }
            }
        }
        return null; // No path found
    }

    private Path reconstructPath(Object source, Object destination, Map<Object, Object> parentMap) {
        Path path = new Path();
        Object current = destination;

        while (current != null) {
            path.addNode(extractTargetName(current));  // Only add clean label
            current = parentMap.get(current);
            if (current != null && current.equals(source)) {
                path.addNode(extractTargetName(source));  // Ensure source is added correctly
                break;
            }
        }

        Collections.reverse(path.getNodes());  // Reverse the path for correct order from source to destination
        return path;
    }

    private Object findNodeByName(String label) {
        for (Object node : graph.nodes()) {
            String nodeLabel = node.toString().replaceAll("\\{.*?\\}|->.*", "").trim(); // Remove metadata
            if (nodeLabel.equals(label)) {
                return node;
            }
        }
        return null;
    }

    private Collection<Link> getLinks(Object node) {
        if (node instanceof MutableNode) {
            return ((MutableNode) node).links();
        }
        return Collections.emptyList();
    }
}
