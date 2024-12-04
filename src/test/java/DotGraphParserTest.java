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
        parser.parseGraph("input.dot"); // The provided DOT file
    }

    @Test
    public void testGraphParsedSuccessfully() {
        Set<String> nodes = parser.getNodes();
        Set<String> edges = parser.getEdges();
        assertEquals("Number of nodes should be 8.", 8, nodes.size()); // Nodes: a, b, c, d, e, f, g, h
        assertEquals("Number of edges should be 9.", 9, edges.size()); // Edges as per the graph structure
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
        String outputPath = "testGraphOutput.png";
        parser.outputGraphics(outputPath, "png");
        File outputFile = new File(outputPath);
        assertTrue("PNG output file should be created.", outputFile.exists());
        assertTrue("PNG file should not be empty.", outputFile.length() > 0);
        outputFile.delete();
    }

    @Test
    public void testAddNodes() {
        Set<String> newNodes = new HashSet<>();
        newNodes.add("x");
        newNodes.add("y");
        parser.addNodes(newNodes);
        Set<String> nodes = parser.getNodes();
        assertTrue("Nodes should contain x.", nodes.contains("x"));
        assertTrue("Nodes should contain y.", nodes.contains("y"));
    }

    @Test
    public void testToString() {
        String graphDetails = parser.toString();
        assertTrue("Output should contain 'Number of nodes'.", graphDetails.contains("Number of nodes"));
    }

    @Test
    public void testAddEdge() {
        parser.addEdge("a", "x");
        Set<String> edges = parser.getEdges();
        assertTrue("Edges should contain a -> x.", edges.contains("a -> x"));
        assertEquals("Number of edges should be 10 after adding a -> x.", 10, edges.size());
    }

    @Test
    public void testRemoveNode() {
        parser.addNode("x");
        parser.addEdge("a", "x");
        parser.addEdge("x", "b");

        Set<String> nodesBeforeRemoval = parser.getNodes();
        Set<String> edgesBeforeRemoval = parser.getEdges();
        assertTrue("Nodes should contain x before removal.", nodesBeforeRemoval.contains("x"));
        assertTrue("Edges should contain a -> x before removal.", edgesBeforeRemoval.contains("a -> x"));

        parser.removeNode("x");
        Set<String> nodesAfterRemoval = parser.getNodes();
        Set<String> edgesAfterRemoval = parser.getEdges();
        assertFalse("Nodes should not contain x after removal.", nodesAfterRemoval.contains("x"));
        assertFalse("Edges should not contain a -> x after removal.", edgesAfterRemoval.contains("a -> x"));
    }

    @Test
    public void testRemoveNodes() {
        String[] nodesToRemove = {"a", "c", "e"};
        parser.removeNodes(nodesToRemove);

        Set<String> expectedRemainingNodes = new HashSet<>();
        expectedRemainingNodes.add("b");
        expectedRemainingNodes.add("d");
        expectedRemainingNodes.add("f");
        expectedRemainingNodes.add("g");
        expectedRemainingNodes.add("h");
        Set<String> actualNodes = parser.getNodes();
        assertEquals("Remaining nodes should include b, d, f, g, and h.", expectedRemainingNodes, actualNodes);

        Set<String> actualEdges = parser.getEdges();
        assertTrue("Edges should not contain edges starting or ending with removed nodes.",
                actualEdges.stream().noneMatch(edge -> edge.startsWith("a") || edge.startsWith("c") || edge.startsWith("e")));
    }

    @Test
    public void testGraphSearchWithBFS() {
        Path pathAB = parser.GraphSearch("a", "b", Algorithm.BFS);
        assertNotNull("Path from a to b should exist", pathAB);

        Path pathAC = parser.GraphSearch("a", "c", Algorithm.BFS);
        assertNotNull("Path from a to c should exist", pathAC);
    }

    @Test
    public void testGraphSearchWithDFS() {
        Path pathAB = parser.GraphSearch("a", "b", Algorithm.DFS);
        assertNotNull("Path from a to b should exist", pathAB);

        Path pathAC = parser.GraphSearch("a", "c", Algorithm.DFS);
        assertNotNull("Path from a to c should exist", pathAC);
    }

    @Test
    public void testRandomWalkSearch() {
        System.out.println("Random Walk Testing");

        // Run the random walk multiple times to demonstrate randomness
        for (int i = 0; i < 5; i++) {
            Path path = parser.GraphSearch("a", "c", Algorithm.RANDOM_WALK);

            // Ensure the Random Walk Search returns a path
            assertNotNull("Random walk should return a path.", path);

            // Print the random walk path for inspection
            System.out.println("Random Walk Path [" + (i + 1) + "]: " + path);

            // Verify the path starts at the source node
            assertEquals("Path should start at the source node.", "a", path.getNodes().get(0));

            // Verify that the path ends at the destination node if reached
            if (path.getNodes().contains("c")) {
                assertEquals("Path should end at the destination node if reached.", "c", path.getNodes().get(path.getNodes().size() - 1));
            } else {
                System.out.println("Random walk did not reach the destination node 'c' in this run.");
            }
        }
    }

}
