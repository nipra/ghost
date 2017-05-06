package com.nipra.ghost;

import java.io.IOException;
import java.util.List;

/**
 * Ghost Game
 *
 */

public class App {
  public static void main(String[] args) throws IOException {
    String dictionaryFile =
        "/Users/nprabhak/Dropbox/Personal/Important/Job Opportunities/Apple/web2";
    Trie trie = new Trie();
    // List<String> dictionary = Utils.getWords(dictionaryFile, 2);
    List<String> dictionary = Utils.getWordsFromDictionaryInResource("web2", 2);
    Game game = new Game(trie, dictionary, 3);

    game.start();
  }
}
