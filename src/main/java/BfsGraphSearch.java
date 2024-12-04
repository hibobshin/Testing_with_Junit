import java.util.*;

public class BfsGraphSearch extends AbstractGraphSearch {

    //Implemented

    @Override
    public Path search(Object sourceNode, Object destinationNode, DotGraphParser graphParser) {
        Queue<Object> queue = new LinkedList<>();
        Map<Object, Object> parentMap = new HashMap<>();
        Set<Object> visited = new HashSet<>();

        queue.add(sourceNode);
        visited.add(sourceNode);

        while (!queue.isEmpty()) {
            Object currentNode = queue.poll();

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
                    queue.add(neighborNode);
                }
            }
        }

        return null; // No path found
    }
}
