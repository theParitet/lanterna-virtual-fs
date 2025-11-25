package io.github.theparitet;

public class FileNode extends AbstractNode {
    protected String content;

    public FileNode(String name) {
        super(name);
        this.content = "";
    }
    public FileNode(String name, Node parent) {
        super(name, parent);
        this.content = "";
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
