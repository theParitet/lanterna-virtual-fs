package io.github.theparitet;

public interface Node {
    String getName();
    void setName(String newName);
    Node getParent();
    void setParent(Node parent);
    int depth();
    String getPath();
    String getPath(int max);
}
