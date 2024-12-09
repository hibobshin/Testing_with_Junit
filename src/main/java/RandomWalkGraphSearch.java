import java.util.*;

public class RandomWalkGraphSearch extends AbstractGraphSearch {
    private final Random random;

    public RandomWalkGraphSearch() {
        this.random = new Random();
    }

    @Override
    public Path search(Object sourceNode, Object destinationNode, DotGraphParser parser) {
        if (sourceNode == null || destinationNode == null) {
            System.err.println("Source or destination node is null.");
            return null;
        }

        Set<Object> visited = new HashSet<>();
        Path path = new Path();
        Object currentNode = sourceNode;

        path.addNode(parser.extractTargetName(currentNode));
        visited.add(currentNode);

        while (!currentNode.equals(destinationNode)) {
            List<Object> neighbors = new ArrayList<>();
            if (currentNode instanceof guru.nidi.graphviz.model.MutableNode) {
                for (guru.nidi.graphviz.model.Link link : ((guru.nidi.graphviz.model.MutableNode) currentNode).links()) {
                    Object neighbor = link.to();
                    String neighborLabel = parser.extractTargetName(neighbor);
                    Object neighborNode = parser.findNodeByName(neighborLabel);

                    if (neighborNode != null && !visited.contains(neighborNode)) {
                        neighbors.add(neighborNode);
                    }
                }
            }

            if (neighbors.isEmpty()) {
                System.out.println("No unvisited neighbors available. Random walk terminates.");
                break;
            }

            // Randomly select the next node
            currentNode = neighbors.get(random.nextInt(neighbors.size()));
            visited.add(currentNode);
            path.addNode(parser.extractTargetName(currentNode));
        }

        return path;
    }

    @Override
    protected Object extractNode(Collection<Object> dataStructure) {
        // Random walk doesn't use a collection, so no-op
        return null;
    }

    @Override
    protected void insertNode(Collection<Object> dataStructure, Object node) {
        // Random walk doesn't use a collection, so no-op
    }
}
