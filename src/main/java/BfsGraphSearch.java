import java.util.*;

public class BfsGraphSearch extends AbstractGraphSearch {
    @Override
    public Path search(Object sourceNode, Object destinationNode, DotGraphParser graphParser) {
        Queue<Object> queue = new LinkedList<>();
        return executeSearch(sourceNode, destinationNode, graphParser, queue);
    }

    @Override
    protected Object extractNode(Collection<Object> dataStructure) {
        return ((Queue<Object>) dataStructure).poll();
    }

    @Override
    protected void insertNode(Collection<Object> dataStructure, Object node) {
        ((Queue<Object>) dataStructure).offer(node);
    }
}
