package com.nipra.ghost;

import java.util.ArrayList;
import java.util.List;

public class Trie {

  private Node root;

  public Trie() {
    this.root = new Node("");
  }

  /**
   *
   * @param key
   */
  public void insert(String key) {

    Node tempNode = root;

    for (int i = 0; i < key.length(); ++i) {

      char c = key.charAt(i);
      int asciiIndex = c - 'a';

      if (tempNode.getChild(asciiIndex) == null) {
        Node node = new Node(String.valueOf(c));
        tempNode.setChild(asciiIndex, node);
        tempNode = node;
      } else {
        tempNode = tempNode.getChild(asciiIndex);
      }
    }

    tempNode.setLeaf(true);
  }

  /**
   *
   * @param node
   * @param prefix
   * @return
   */
  private String getFirstWordWithPrefixHelper(Node node, String prefix) {
    if (node.isLeaf()) {
      return prefix;
    } else {
      Node firstChildNode = node.getFirstChild();
      String character = firstChildNode.getCharacter();
      return getFirstWordWithPrefixHelper(firstChildNode, prefix + character);
    }
  }


  /**
   *
   * @param prefix
   * @return
   */
  public String getFirstWordWithPrefix(String prefix) {
    Node trieNode = root;

    for (int i = 0; i < prefix.length(); ++i) {
      char c = prefix.charAt(i);
      int asciiIndex = c - 'a';
      trieNode = trieNode.getChild(asciiIndex);
      if (trieNode == null) {
        return null;
      }
    }

    return getFirstWordWithPrefixHelper(trieNode, prefix);

  }


  /**
   *
   * @param node
   * @param prefix
   * @param allWords
   */
  private void collect(Node node, String prefix, List<String> allWords) {

    if (node == null)
      return;

    if (node.isLeaf()) {
      allWords.add(prefix);
    }

    for (Node childNode : node.getChildren()) {
      if (childNode == null)
        continue;
      String childCharacter = childNode.getCharacter();
      collect(childNode, prefix + childCharacter, allWords);
    }
  }

  /**
   *
   * @param prefix
   * @return
   */
  public List<String> allWordsWithPrefix(String prefix) {

    Node trieNode = root;
    List<String> allWords = new ArrayList<>();

    for (int i = 0; i < prefix.length(); ++i) {

      char c = prefix.charAt(i);
      int asciiIndex = c - 'a';
      trieNode = trieNode.getChild(asciiIndex);
      if (trieNode == null) {
        return allWords;
      }
    }

    collect(trieNode, prefix, allWords);

    return allWords;
  }


}
