import org.junit.Test;
import org.junit.Before;
import java.io.File;
import static org.junit.Assert.*;

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
        assertNotNull("Graph should be parsed successfully and should not be null.", parser);
    }

    @Test
    public void testNumberOfNodes() {
        Set<String> nodes = parser.getNodes();
        assertEquals("Number of nodes should be 4.", 4, nodes.size());
    }

    @Test
    public void testNodesContent() {
        Set<String> nodes = parser.getNodes();
        assertTrue("Nodes should contain A.", nodes.contains("A"));
        assertTrue("Nodes should contain B.", nodes.contains("B"));
        assertTrue("Nodes should contain C.", nodes.contains("C"));
        assertTrue("Nodes should contain D.", nodes.contains("D"));
    }

    @Test
    public void testNumberOfEdges() {
        Set<String> edges = parser.getEdges();
        assertEquals("Number of edges should be 3.", 3, edges.size());
    }

    @Test
    public void testEdgesContent() {
        Set<String> edges = parser.getEdges();
        assertTrue("Edges should contain A -> B.", edges.contains("A -> B"));
        assertTrue("Edges should contain B -> C.", edges.contains("B -> C"));
        assertTrue("Edges should contain C -> A.", edges.contains("C -> A"));
    }

    @Test
    public void testAddNode() {
        DotGraphParser parser = new DotGraphParser();
        parser.addNode("F");
        Set<String> nodes = parser.getNodes();
        assertTrue("Nodes should contain F.", nodes.contains("F"));
    }

    @Test
    public void testAddDuplicateNode() {
        DotGraphParser parser = new DotGraphParser();
        parser.addNode("G");
        parser.addNode("G"); // Adding the same node again
        Set<String> nodes = parser.getNodes();
        assertEquals("Number of nodes should be 1 after adding a duplicate.", 1, nodes.size());
    }

    @Test
    public void testAddEdge() {
        DotGraphParser parser = new DotGraphParser();
        parser.addEdge("H", "I");
        Set<String> edges = parser.getEdges();
        assertTrue("Edges should contain H -> I.", edges.contains("H -> I"));
    }

    @Test
    public void testOutputDOTGraph() {
        String outputPath = "testOutput.dot";
        parser.outputDOTGraph(outputPath);
        File outputFile = new File(outputPath);
        assertTrue("DOT output file should be created.", outputFile.exists());

        // Clean up
        outputFile.delete();
    }

    @Test
    public void testOutputGraphics() {
        String outputPath = "testOutput.png";
        parser.outputGraphics(outputPath, "png");
        File outputFile = new File(outputPath);
        assertTrue("PNG output file should be created.", outputFile.exists());

        // Clean up
        outputFile.delete();
    }

}

