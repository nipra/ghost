package com.nipra.ghost;

/**
 * Node class to represent each node of Trie data structure.
 */
public class Node {

  private String character;
  private Node[] children;
  private boolean leaf;
  private boolean visited;

  /**
   * Constructor for Node class. Each node has a character and starts with an empty array of length
   * the size of number of alphabets in English language.
   *
   * @param character
   */
  public Node(String character) {
    this.character = character;
    this.children = new Node[Constant.ALPHABET_SIZE];
  }

  public void setChild(int index, Node node) {
    this.children[index] = node;
  }

  public String getCharacter() {
    return character;
  }

  public void setCharacter(String character) {
    this.character = character;
  }

  public Node[] getChildren() {
    return children;
  }

  public void setChildren(Node[] children) {
    this.children = children;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public void setLeaf(boolean leaf) {
    this.leaf = leaf;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  public Node getChild(int index) {
    return children[index];
  }

  /**
   * Get first child of the node which is not null. Children are sorted by natural order of
   * characters.
   *
   * @return Node
   */
  public Node getFirstChild() {
    for (int i = 0; i < Constant.ALPHABET_SIZE; i++) {
      Node node = children[i];
      if (node != null) {
        return node;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return this.character;
  }

}
