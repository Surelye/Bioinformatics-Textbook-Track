package auxil;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<PathNode> path;
    private PathNode.NodeType lnt;
    private int lni;
    private final StringBuilder strPath = new StringBuilder();

    public Path() {
        this.path = new ArrayList<>(List.of(new PathNode(PathNode.NodeType.s, 0)));
        lnt = PathNode.NodeType.s;
        strPath.append('s');
    }

    public int size() {
        return path.size();
    }
    
    public void addNode(PathNode node) {
        if (lnt == PathNode.NodeType.e) {
            throw new RuntimeException("given path has already been ended");
        }
        PathNode.NodeType nt = node.nodeType();
        int ni = node.index();
        if (nt == PathNode.NodeType.s) {
            throw new RuntimeException("given path has already been started");
        }
        if (nt != PathNode.NodeType.e && ni < lni) {
            throw new RuntimeException("node index is too small");
        } else if (lnt == PathNode.NodeType.s && !((nt == PathNode.NodeType.I && ni == 0) ||
                ((nt == PathNode.NodeType.M || nt == PathNode.NodeType.D) && ni == 1))) {
            throw new RuntimeException("wrong indexing after start node");
        } else if ((nt == PathNode.NodeType.M || nt == PathNode.NodeType.D) && ni != lni + 1) {
            throw new RuntimeException("incorrect node index");
        } else if (ni > lni + 1) {
            throw new RuntimeException("node index is too large");
        }
        path.add(node);
        strPath.append("%s%s".formatted(nt, (nt == PathNode.NodeType.e) ? "" : ni));
        lnt = nt;
        lni = ni;
    }

    public PathNode getNthNode(int n) {
        return path.get(n);
    }

    @Override
    public String toString() {
        return strPath.toString();
    }
}
