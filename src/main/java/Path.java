import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<String> nodes;

    public Path() {
        this.nodes = new ArrayList<>();
    }

    public void addNode(String node) {
        nodes.add(node);
    }

    public List<String> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return String.join(" -> ", nodes);
    }
}
