package io.github.theparitet;

import java.util.ArrayList;
import java.util.List;

public class FolderNode extends AbstractNode {
    protected ArrayList<Node> children = new ArrayList<>();

    public FolderNode(String name) {
        super(name);
    }
    public FolderNode(String name, Node parent) {
        super(name, parent);
    }

    public List<Node> getChildren() {
        return children;
    }
    public Node get(int index) {
        return children.get(index);
    }

    public boolean addChild(Node child) {
        if (containsChildName(child.getName())) {
            return false;
        }
        child.setParent(this);
        return children.add(child);
    }
    public boolean containsChildName(String name) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public boolean removeChild(Node child) {
        return children.remove(child);
    }

    @Override
    public String getPath() {
        return super.getPath() + "/";
    }
    @Override
    public String getPath(int max) {
        return super.getPath(max) + "/";
    }


    public Node getNodeByName(String name) {
        for (Node node : children) {
            if (node.getName().equals(name)) return node;
        }
        return null;
    }
}
