package com.nipra.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  /**
   *
   * @param resource
   * @return
   * @throws IOException
   */
  public static List<String> readLinesFromResourceFile(String resource) throws IOException {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream(resource);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    List<String> lines = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      lines.add(line);
    }

    return lines;
  }

  /**
   *
   * @param minLength
   * @param lines
   * @return
   */
  private static List<String> getWordsHelper(int minLength, List<String> lines) {
    List<String> words = new ArrayList<>();
    for (String line : lines) {
      String word = line.trim().toLowerCase();
      if (word.length() >= minLength) {
        words.add(word);
      }
    }

    return words;
  }

  /**
   *
   * @param resource
   * @param minLength
   * @return
   * @throws IOException
   */
  public static List<String> getWordsFromDictionaryInResource(String resource, int minLength)
      throws IOException {
    List<String> lines = readLinesFromResourceFile(resource);

    return getWordsHelper(minLength, lines);
  }

  /**
   *
   * @param dictionaryFilePath
   * @param minLength
   * @return
   * @throws IOException
   */
  public static List<String> getWords(String dictionaryFilePath, int minLength) throws IOException {
    Path dictionaryPath = Paths.get(dictionaryFilePath);
    List<String> lines = Files.readAllLines(dictionaryPath);

    return getWordsHelper(minLength, lines);

  }

  /**
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    List<String> words =
        getWords("/Users/nprabhak/Dropbox/Personal/Important/Job Opportunities/Apple/web2_a", 4);
    System.out.println(words);

    List<String> words2 = getWordsFromDictionaryInResource("web2_a", 5);
    System.out.println(words2);
  }
}
