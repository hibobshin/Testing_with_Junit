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
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");
        parser.addEdge("A", "B");
        parser.addEdge("B", "C");

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
        newNodes.add("X");
        newNodes.add("Y");
        parser.addNodes(newNodes);
        Set<String> nodes = parser.getNodes();
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
        parser.addEdge("A", "D");
        Set<String> edges = parser.getEdges();
        assertTrue("Edges should contain A -> D.", edges.contains("A -> D"));
        assertEquals("Number of edges should be 4 after adding A -> D.", 4, edges.size());
    }

    @Test
    public void testRemoveNode() {
        parser.addNode("X");
        parser.addEdge("A", "X");
        parser.addEdge("X", "B");

        Set<String> nodesBeforeRemoval = parser.getNodes();
        Set<String> edgesBeforeRemoval = parser.getEdges();
        assertTrue("Nodes should contain X before removal.", nodesBeforeRemoval.contains("X"));
        assertTrue("Edges should contain A -> X before removal.", edgesBeforeRemoval.contains("A -> X"));

        parser.removeNode("X");
        Set<String> nodesAfterRemoval = parser.getNodes();
        Set<String> edgesAfterRemoval = parser.getEdges();
        assertFalse("Nodes should not contain X after removal.", nodesAfterRemoval.contains("X"));
        assertFalse("Edges should not contain A -> X after removal.", edgesAfterRemoval.contains("A -> X"));
    }

    @Test
    public void testRemoveNodes() {
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");
        parser.addNode("D");
        parser.addEdge("A", "B");
        parser.addEdge("B", "C");
        parser.addEdge("C", "D");
        parser.addEdge("D", "A");
        parser.addEdge("D", "B");

        String[] nodesToRemove = {"A", "C", "E"};
        parser.removeNodes(nodesToRemove);

        Set<String> expectedRemainingNodes = new HashSet<>();
        expectedRemainingNodes.add("B");
        expectedRemainingNodes.add("D");
        Set<String> actualNodes = parser.getNodes();
        assertEquals("Remaining nodes should only include B and D.", expectedRemainingNodes, actualNodes);

        Set<String> expectedRemainingEdges = new HashSet<>();
        expectedRemainingEdges.add("D -> B");
        Set<String> actualEdges = parser.getEdges();
        assertEquals("Remaining edges should only include D -> B.", expectedRemainingEdges, actualEdges);
    }

    @Test
    public void testGraphSearchWithBFS() {
        Path pathAB = parser.GraphSearch("A", "B", Algorithm.BFS);
        assertNotNull("Path from A to B should exist", pathAB);

        Path pathAC = parser.GraphSearch("A", "C", Algorithm.BFS);
        assertNotNull("Path from A to C should exist", pathAC);
    }

    @Test
    public void testGraphSearchWithDFS() {
        Path pathAB = parser.GraphSearch("A", "B", Algorithm.DFS);
        assertNotNull("Path from A to B should exist", pathAB);

        Path pathAC = parser.GraphSearch("A", "C", Algorithm.DFS);
        assertNotNull("Path from A to C should exist", pathAC);
    }

    @Test
    public void testGraphSearchNoPathBFS() {
        parser.addNode("D");
        Path path = parser.GraphSearch("A", "D", Algorithm.BFS);
        assertNull("Path from A to D should not exist in BFS.", path);
    }

    @Test
    public void testGraphSearchNoPathDFS() {
        parser.addNode("D");
        Path path = parser.GraphSearch("A", "D", Algorithm.DFS);
        assertNull("Path from A to D should not exist in DFS.", path);
    }

    @Test
    public void testGraphSearchSelfPathBFS() {
        Path path = parser.GraphSearch("A", "A", Algorithm.BFS);
        assertNotNull("Path from A to A should exist as self-path in BFS.", path);
        assertEquals("Path should be A.", "A", path.toString());
    }

    @Test
    public void testGraphSearchSelfPathDFS() {
        Path path = parser.GraphSearch("A", "A", Algorithm.DFS);
        assertNotNull("Path from A to A should exist as self-path in DFS.", path);
        assertEquals("Path should be A.", "A", path.toString());
    }
}
