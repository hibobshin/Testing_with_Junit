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
    public void testOutputGraphics() {
        // Set up a simple graph
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");
        parser.addEdge("A", "B");
        parser.addEdge("B", "C");

        // Define the output file path
        String outputPath = "testGraphOutput.png";

        // Call the method to generate PNG file
        parser.outputGraphics(outputPath, "png");

        // Verify that the file exists
        File outputFile = new File(outputPath);
        assertTrue("PNG output file should be created.", outputFile.exists());

        // (Optional) Check file size to ensure it's not empty
        assertTrue("PNG file should not be empty.", outputFile.length() > 0);

        // Clean up by deleting the file after the test
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

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingNode() {
        // Set up a simple graph
        parser.addNode("A");
        parser.addNode("B");
        parser.addEdge("A", "B");

        // Attempt to remove a non-existing node "C"
        parser.removeNode("F");
    }

    @Test
    public void testRemoveNonExistingNodeMessage() {
        // Given: Graph initialized from sampleGraph.dot, containing nodes A, B, C

        try {
            // Attempt to remove a non-existing node "D"
            parser.removeNode("D");
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct
            assertEquals("Node D does not exist in the graph.", e.getMessage());
        }
    }

    @Test
    public void testRemoveExistingEdge() {
        // Given: Graph initialized from sampleGraph.dot with edge A -> B
        Set<String> initialEdges = parser.getEdges();
        assertTrue("Graph should initially contain edge A -> B.", initialEdges.contains("A -> B"));

        // Remove the edge A -> B
        parser.removeEdge("A", "B");

        // Verify that the edge A -> B has been removed
        Set<String> edgesAfterRemoval = parser.getEdges();
        assertFalse("Edge A -> B should be removed from the graph.", edgesAfterRemoval.contains("A -> B"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingEdge() {
        // Given: Graph initialized from sampleGraph.dot without an edge B -> A
        parser.removeEdge("B", "A");  // Attempt to remove non-existing edge
    }

    @Test
    public void testRemoveNonExistingEdgeMessage() {
        try {
            // Attempt to remove a non-existing edge B -> A
            parser.removeEdge("B", "A");
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Verify the exception message is correct
            assertEquals("Edge from B to A does not exist in the graph.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEdgeWithNonExistingSourceNode() {
        // Attempt to remove an edge from non-existing node "D" to existing node "A"
        parser.removeEdge("D", "A");
    }

    @Test
    public void testGraphSearchPathExists() {
        // Test for direct and indirect paths within the cycle
        Path pathAB = parser.GraphSearch("A", "B");
        assertNotNull("Path from A to B should exist", pathAB);

        Path pathBC = parser.GraphSearch("B", "C");
        assertNotNull("Path from B to C should exist", pathBC);

        Path pathAC = parser.GraphSearch("A", "C");
        assertNotNull("Path from A to C should exist", pathAC);

        // Test for self-path
        Path selfPathA = parser.GraphSearch("A", "A");
        assertNotNull("Path from A to A should exist as self-path", selfPathA);

        // Test for non-existing path
        Path nonExistentPath = parser.GraphSearch("A", "D");
        assertNull("Path from A to D should not exist", nonExistentPath);
    }

    @Test
    public void testGraphSearchPathDoesNotExist() {
        // Add a disconnected node to ensure there is no path from A to this node
        parser.addNode("D");

        // Verify that no path exists from A to D
        Path path = parser.GraphSearch("A", "D");

        // Path should be null as there is no connection between A and D
        assertNull("Path from A to D should not exist.", path);
    }

    @Test
    public void testGraphSearchSelfPath() {
        // Verify that a path exists from A to A (self-path)
        Path path = parser.GraphSearch("A", "A");

        // Path should consist of A only, assuming self-paths are allowed
        assertNotNull("Path from A to A should exist as self-path.", path);
        assertEquals("Path should be A.", "A", path.toString());
    }
}


