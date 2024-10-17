import guru.nidi.graphviz.parse.Parser;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
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

        Set<String> nodes = new HashSet<>();
        Set<String> edges = new HashSet<>();

        // Iterate through the nodes in the graph
        for (MutableNode node : graph.nodes()) {
            nodes.add(node.name().toString());

            // Iterate through links from each node to find edges
            for (Link link : node.links()) {
                LinkTarget target = link.to();

                // If the target is a MutableNode or a general Node, extract the name directly
                if (target instanceof MutableNode) {
                    MutableNode targetNode = (MutableNode) target;
                    edges.add(node.name() + " -> " + targetNode.name());
                } else if (target instanceof guru.nidi.graphviz.model.Node) {
                    // If it's a generic Node, add its name
                    guru.nidi.graphviz.model.Node targetNode = (guru.nidi.graphviz.model.Node) target;
                    edges.add(node.name() + " -> " + targetNode.name().toString());
                } else {
                    // Extract the name cleanly without extra symbols
                    String targetName = extractTargetName(target);
                    edges.add(node.name() + " -> " + targetName);
                }
            }
        }

        // Output graph details
        System.out.println("Number of nodes: " + nodes.size());
        System.out.println("Nodes: " + nodes);
        System.out.println("Number of edges: " + edges.size());
        System.out.println("Edges: " + edges);
    }

    // Method to extract a clean target name from a LinkTarget object
    private String extractTargetName(LinkTarget target) {
        String name = target.toString();
        // Remove any unnecessary "::" or additional info
        int index = name.indexOf("::");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name.trim();
    }



    // Main method for testing Feature 1
    public static void main(String[] args) {
        DotGraphParser parser = new DotGraphParser();
        parser.parseGraph("sampleGraph.dot");
    }
}
