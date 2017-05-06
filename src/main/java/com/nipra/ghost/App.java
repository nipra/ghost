package com.nipra.ghost;

import java.io.IOException;
import java.util.List;

/**
 * Ghost Game
 *
 */

public class App {
  public static void main(String[] args) throws IOException {
    // Trie data structure to store all dictionary words
    Trie trie = new Trie();
    // Get words for the dictionary. Min length of each word is 2.
    List<String> dictionary = Utils.getWordsFromDictionaryInResource("web2", 2);
    Game game = new Game(trie, dictionary);

    // Start the game
    game.start();
  }
}
