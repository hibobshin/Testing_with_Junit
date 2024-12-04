import guru.nidi.graphviz.model.Link;
import java.util.Collection;

public abstract class AbstractGraphSearch {

    //Implemented

    public abstract Path search(Object sourceNode, Object destinationNode, DotGraphParser graphParser);

    protected Collection<Link> getLinks(Object node) {
        if (node instanceof guru.nidi.graphviz.model.MutableNode) {
            return ((guru.nidi.graphviz.model.MutableNode) node).links();
        }
        return java.util.Collections.emptyList();
    }
}
