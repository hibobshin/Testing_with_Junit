import java.util.*;

public class DfsGraphSearch extends AbstractGraphSearch {
    @Override
    public Path search(Object sourceNode, Object destinationNode, DotGraphParser graphParser) {
        Stack<Object> stack = new Stack<>();
        return executeSearch(sourceNode, destinationNode, graphParser, stack);
    }

    @Override
    protected Object extractNode(Collection<Object> dataStructure) {
        return ((Stack<Object>) dataStructure).pop();
    }

    @Override
    protected void insertNode(Collection<Object> dataStructure, Object node) {
        ((Stack<Object>) dataStructure).push(node);
    }
}
