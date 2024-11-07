import org.junit.Test;
import org.junit.Before;
import java.io.File;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class DotGraphParserTest {

    private DotGraphParser parser;

    @Before
    public void setUp() {
        parser = new DotGraphParser();
        parser.parseGraph("sampleGraph.dot");
    }

    @Test
    public void testGraphParsedSuccessfully() {
        Set<String> nodes = parser.getNodes();
        Set<String> edges = parser.getEdges();
        assertEquals("Number of nodes should be 3.", 3, nodes.size());
        assertEquals("Number of edges should be 3.", 3, edges.size());
    }


    @Test
    public void testOutputGraph() {
        String outputPath = "outputGraph.dot";
        parser.outputGraph(outputPath);
        File outputFile = new File(outputPath);
        assertTrue("DOT output file should be created.", outputFile.exists());
        outputFile.delete();
    }

    @Test
    public void testAddNodes() {
        Set<String> newNodes = new HashSet<>();
        newNodes.add("X");
        newNodes.add("Y");
        parser.addNodes(newNodes);
        Set<String> nodes = parser.getNodes();
        //System.out.print(parser.toString());
        assertTrue("Nodes should contain X.", nodes.contains("X"));
        assertTrue("Nodes should contain Y.", nodes.contains("Y"));
    }

    @Test
    public void testToString() {
        String graphDetails = parser.toString();
        assertTrue("Output should contain 'Number of nodes'.", graphDetails.contains("Number of nodes"));
    }

    @Test
    public void testAddEdge() {
        parser.addEdge("A", "D"); // Add new edge A -> D
        Set<String> edges = parser.getEdges();

        // Print current graph details for debugging (can remove after verification)
        System.out.println(parser.toString());

        // Check that the new edge was added
        assertTrue("Edges should contain A -> D.", edges.contains("A -> D"));

        // Confirm that the total number of edges has increased by 1
        assertEquals("Number of edges should be 4 after adding A -> D.", 4, edges.size());
    }

    @Test
    public void testRemoveNode() {
        // Add some nodes and edges to the graph
        //System.out.println(parser.toString());
        parser.addNode("X");
        parser.addEdge("A", "X");
        parser.addEdge("X", "B");
        //System.out.println(parser.toString());
        // Verify initial state
        Set<String> nodesBeforeRemoval = parser.getNodes();
        Set<String> edgesBeforeRemoval = parser.getEdges();
        assertTrue("Nodes should contain X before removal.", nodesBeforeRemoval.contains("X"));
        assertTrue("Edges should contain A -> X before removal.", edgesBeforeRemoval.contains("A -> X"));
        assertTrue("Edges should contain X -> B before removal.", edgesBeforeRemoval.contains("X -> B"));

        // Remove the node "X" and verify
        parser.removeNode("X");
        Set<String> nodesAfterRemoval = parser.getNodes();
        Set<String> edgesAfterRemoval = parser.getEdges();
        // Assert node "X" is removed
        //System.out.print(parser.toString());
        assertFalse("Nodes should not contain X after removal.", nodesAfterRemoval.contains("X"));

        // Assert edges connected to "X" are removed
        assertFalse("Edges should not contain A -> X after removal.", edgesAfterRemoval.contains("A -> X"));
        assertFalse("Edges should not contain X -> B after removal.", edgesAfterRemoval.contains("X -> B"));
    }

    @Test
    public void testRemoveNodes() {
        // Set up initial graph
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");
        parser.addNode("D");
        parser.addEdge("A", "B");
        parser.addEdge("B", "C");
        parser.addEdge("C", "D");
        parser.addEdge("D", "A");
        parser.addEdge("D", "B");  // Explicitly add D -> B to ensure it exists independently
        System.out.println("Initial graph:\n" + parser.toString());

        // Nodes to remove, including one non-existing node "E"
        String[] nodesToRemove = {"A", "C", "E"};
        parser.removeNodes(nodesToRemove);

        // Verify the remaining nodes and edges
        Set<String> expectedRemainingNodes = new HashSet<>();
        expectedRemainingNodes.add("B");
        expectedRemainingNodes.add("D");
        Set<String> actualNodes = parser.getNodes();

        assertEquals("Remaining nodes should only include B and D.", expectedRemainingNodes, actualNodes);

        // Verify that only the edge D -> B remains
        Set<String> expectedRemainingEdges = new HashSet<>();
        expectedRemainingEdges.add("D -> B");
        Set<String> actualEdges = parser.getEdges();

        assertEquals("Remaining edges should only include D -> B.", expectedRemainingEdges, actualEdges);

        System.out.println("Graph after removing nodes A, C, and non-existent E:\n" + parser.toString());
    }

}


