package com.nipra.ghost;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Game {
  private static final String CHALLENGE = "challenge";
  private static Player players[] = new Player[] {Player.HUMAN, Player.COMPUTER};
  private static final char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  private static ComputerMoveType computerMoveTypes[] = new ComputerMoveType[] {
      ComputerMoveType.CHALLENGE_HUMAN, ComputerMoveType.NORMAL};

  private Trie trie;
  private List<String> dictionary;

  private Player playerToMakeAMove;
  private boolean humanHasBeenChallenged = false;
  private boolean computerHasBeenChallenged = false;
  private String currentPrefix = "";
  private boolean startOfGame = true;

  /**
   *
   * @param trie
   * @param dictionary
   * @param minWordLengthInDict
   */
  public Game(Trie trie, List<String> dictionary, int minWordLengthInDict) {
    this.trie = trie;
    this.dictionary = dictionary;
  }


  /**
   *
   * @return
   * @throws IOException
   */
  public Set<Character> getAllCharsInDict() throws IOException {
    Set<Character> chars = new TreeSet<>();
    for (String word : dictionary) {
      for (char c : word.toCharArray()) {
        chars.add(c);
      }
    }

    return chars;
  }

  /**
   *
   * @throws IOException
   */
  public void loadDictionary() throws IOException {
    for (String word : dictionary) {
      trie.insert(word);
    }
  }

  /**
   *
   * @param prefix
   * @return
   */
  public List<String> searchWords(String prefix) {
    return trie.allWordsWithPrefix(prefix);
  }

  /**
   *
   * @return
   */
  public Player toss() {
    Random randomNumberGenerator = new Random();
    Player player = players[randomNumberGenerator.nextInt(players.length)];
    return player;
  }

  /**
   *
   * @param player
   * @return
   */
  public boolean isHuman(Player player) {
    return player.equals(Player.HUMAN);
  }

  /**
   *
   * @param player
   * @return
   */
  public boolean isComputer(Player player) {
    return player.equals(Player.COMPUTER);
  }

  /**
   *
   * @param input
   * @return
   */
  public boolean isAChallenge(String input) {
    return input.equals(CHALLENGE);
  }

  /**
   *
   */
  public void togglePlayerToMakeAMove() {
    if (isHuman(playerToMakeAMove)) {
      this.playerToMakeAMove = Player.COMPUTER;
    } else {
      this.playerToMakeAMove = Player.HUMAN;
    }
  }

  /**
   *
   * @param input
   * @return
   */
  public boolean isHumanBluffing(String input) {
    return input.isEmpty();
  }

  /**
   *
   * @param input
   * @return
   */
  public boolean inputIsValid(String input) {
    String trimmed = input.trim();
    String regex = "^[A-Za-z]+$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(trimmed);

    if (humanHasBeenChallenged) {
      if (matcher.matches()) {
        return true;
      }
    }

    if (matcher.matches() && ((trimmed.length() == 1) || trimmed.equals(CHALLENGE))) {
      return true;
    } else {
      return false;
    }

  }

  /**
   *
   * @param input
   * @return
   */
  public String sanitize(String input) {
    return input.trim().toLowerCase();
  }

  /**
   *
   * @param input
   * @return
   */
  public boolean playerHasChallenged(String input) {
    if (input != null) {
      return input.equals(CHALLENGE);
    }

    return false;
  }

  /**
   *
   * @return
   */
  public String getAlphabetRandomly() {
    Random randomNumberGenerator = new Random();
    char alphabet = alphabets[randomNumberGenerator.nextInt(alphabets.length)];
    return String.valueOf(alphabet);
  }

  /**
   *
   * @return
   */
  public ComputerMoveType getComputerMoveTypeRandomly() {
    Random randomNumberGenerator = new Random();
    return computerMoveTypes[randomNumberGenerator.nextInt(computerMoveTypes.length)];
  }

  /**
   *
   * @param scanner
   * @return
   */
  public String getInputFromConsole(Scanner scanner) {
    if (startOfGame) {
      System.out.println("Let's start the game...");
    } else {
      System.out.printf("Current word: %s%n", currentPrefix);
    }

    if (humanHasBeenChallenged) {
      System.out.println("You have been challenged by computer...");
      System.out.print("Please enter your response: ");
    } else {
      System.out.print("Enter a letter or challenge: ");
    }

    return scanner.nextLine();

  }


  /**
   *
   */
  public void start() {
    try {
      loadDictionary();
    } catch (Exception e) {
      System.out.println("Error loading dictionary... ");
      e.printStackTrace();
    }

    this.playerToMakeAMove = toss();
    this.startOfGame = true;

    Scanner scanner = new Scanner(System.in);

    while (true) {

      // If human is the next player to make a move
      if (isHuman(playerToMakeAMove)) {

        String input = getInputFromConsole(scanner);
        if (!humanHasBeenChallenged && !inputIsValid(input)) {
          System.out.println("Invalid input.");
          continue;
        }
        input = sanitize(input);

        if (humanHasBeenChallenged) {
          if (isHumanBluffing(input)) {
            System.out.println("Human lost! You bluffed.");
          } else {
            System.out.println("Computer lost!!!");
          }
          break;
        } else if (isAChallenge(input) && startOfGame) {
          System.out.println("A challenge can not be made at the start of the game...");
          continue;
          // challenge computer
        } else if (isAChallenge(input)) {
          computerHasBeenChallenged = true;
        } else {
          this.currentPrefix = currentPrefix + input;
        }

        // If Computer is the next player to make a move
      } else {

        if (startOfGame) {
          String alphabet = getAlphabetRandomly();
          System.out.println("Computer chose: " + alphabet);
          this.currentPrefix = currentPrefix + alphabet;
        } else if (computerHasBeenChallenged) {
          // Computer tries to find a word.
          // Either human or computer wins.
          List<String> wordsFound = searchWords(currentPrefix);
          if (!wordsFound.isEmpty()) {
            System.out.println("Computer found the word: " + wordsFound.get(0));
            System.out.println("Human lost!!");
          } else {
            System.out.println("Computer couldn't find the word. Human won!!");
          }
          break;
        } else {
          // Before starting to play next move, check whether human has completed a word
          String wordFound = trie.getFirstWordWithPrefix(currentPrefix);
          if ((wordFound != null) && wordFound.equals(currentPrefix)) {
            System.out.println("Hello human! I caught you completing a word. You lose!");
            System.out.println("Word found: " + currentPrefix);
            break;
          }

          // Choose to challenge human or continue to play normally
          if (getComputerMoveTypeRandomly().equals(ComputerMoveType.NORMAL)) {
            String alphabet = getAlphabetRandomly();
            System.out.println("Computer chose: " + alphabet);
            this.currentPrefix = currentPrefix + alphabet;
          } else {
            humanHasBeenChallenged = true;
          }
        }
      }

      startOfGame = false;
      togglePlayerToMakeAMove();

    }
  }

}
