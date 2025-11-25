package io.github.theparitet;

public abstract class AbstractNode implements Node {
    protected String name;
    protected Node parent;

    protected AbstractNode(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must be non-empty.");
        }
        this.name = name.trim();
        this.parent = null;
    }
    protected AbstractNode(String name, Node parent) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must be non-empty.");
        }
        this.name = name.trim();
        this.parent = parent;
    }

    @Override
    public String getName() { return name; }
    @Override
    public void setName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name must be non-empty.");
        }
        this.name = newName.trim();
    }

    @Override
    public Node getParent() {return parent; }
    @Override
    public void setParent(Node parent) { this.parent = parent; }

    @Override
    public int depth() {
        int d = 0;
        Node p = getParent();
        while (p != null) {
            d++;
            p = p.getParent();
        }
        return d;
    }

    @Override
    public String getPath() {
        StringBuilder sb = new StringBuilder(this.name);
        Node parent = this.parent;
        while (parent != null) {
            sb.insert(0, parent.getName() + "/");
            parent = parent.getParent();
        }
        return sb.toString();
    }

    public final String getPathFinal() {
        StringBuilder sb = new StringBuilder(this.name);
        Node parent = this.parent;
        while (parent != null) {
            sb.insert(0, parent.getName() + "/");
            parent = parent.getParent();
        }
        return sb.toString();
    }

    @Override
    public String getPath(int max) {
        if (this.depth() < max) return getPathFinal();
        StringBuilder sb = new StringBuilder(this.name);

        Node parent = this.parent;
        for (int i = 0; i < max-2; i++) {
            sb.insert(0, parent.getName() + "/");
            parent = parent.getParent();
        }

        parent = this.parent;
        while (parent.getParent() != null) {
            parent = parent.getParent();
        }

//        sb.insert(0, " ... /");
        sb.insert(0, " ...(" + (this.depth()-max+1) + "x) /");
        sb.insert(0, parent.getName()+"/");
        return sb.toString();
    }

}
