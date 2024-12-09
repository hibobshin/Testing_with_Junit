import java.util.*;
import java.util.Collections;
import guru.nidi.graphviz.model.Link;

public abstract class AbstractGraphSearch {

    public abstract Path search(Object sourceNode, Object destinationNode, DotGraphParser graphParser);

    protected Collection<Link> getLinks(Object node) {
        if (node instanceof guru.nidi.graphviz.model.MutableNode) {
            return ((guru.nidi.graphviz.model.MutableNode) node).links();
        }
        return Collections.emptyList();
    }

    // A utility method to process nodes for BFS/DFS
    protected Path executeSearch(
            Object sourceNode, Object destinationNode, DotGraphParser graphParser,
            Collection<Object> dataStructure
    ) {
        Map<Object, Object> parentMap = new HashMap<>();
        Set<Object> visited = new HashSet<>();

        dataStructure.add(sourceNode);
        visited.add(sourceNode);

        while (!dataStructure.isEmpty()) {
            Object currentNode = extractNode(dataStructure);

            if (currentNode.equals(destinationNode)) {
                return graphParser.reconstructPath(sourceNode, destinationNode, parentMap);
            }

            for (var link : getLinks(currentNode)) {
                Object neighbor = link.to();
                String neighborLabel = graphParser.extractTargetName(neighbor);
                Object neighborNode = graphParser.findNodeByName(neighborLabel);

                if (neighborNode != null && !visited.contains(neighborNode)) {
                    visited.add(neighborNode);
                    parentMap.put(neighborNode, currentNode);
                    insertNode(dataStructure, neighborNode);
                }
            }
        }

        return null; // No path found
    }

    // Abstract methods for specific behavior
    protected abstract Object extractNode(Collection<Object> dataStructure);
    protected abstract void insertNode(Collection<Object> dataStructure, Object node);
}
