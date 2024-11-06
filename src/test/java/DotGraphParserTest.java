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
}

