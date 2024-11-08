<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import java.util.*;
>>>>>>> dfs

public class Path {
    private List<String> nodes;

    public Path() {
<<<<<<< HEAD
        this.nodes = new ArrayList<>();
=======
        nodes = new ArrayList<>();
>>>>>>> dfs
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
