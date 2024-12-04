import java.util.*;

public class DfsGraphSearch extends AbstractGraphSearch {
    @Override
    public Path search(Object sourceNode, Object destinationNode, DotGraphParser graphParser) {
        Stack<Object> stack = new Stack<>();
        Map<Object, Object> parentMap = new HashMap<>();
        Set<Object> visited = new HashSet<>();

        stack.push(sourceNode);
        visited.add(sourceNode);

        while (!stack.isEmpty()) {
            Object currentNode = stack.pop();

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
                    stack.push(neighborNode);
                }
            }
        }

        return null; // No path found
    }
}
