package com.nipra.ghost;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of Game. An instance of Game class has states which keep on getting updated as
 * game progresses. start method starts the game.
 */
public class Game {
  // Input string used by a player to indicate that it wants to challenge opponent
  private static final String CHALLENGE = "challenge";
  // List of players
  private static final Player players[] = new Player[] {Player.HUMAN, Player.COMPUTER};
  // List of alphabets valid as input
  private static final char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();
  // Valid moves by computer
  private static final ComputerMoveType computerMoveTypes[] = new ComputerMoveType[] {
      ComputerMoveType.CHALLENGE_HUMAN, ComputerMoveType.NORMAL};

  private Trie trie;
  private List<String> dictionary;

  // Player going to make a move currently
  private Player playerToMakeAMove;
  // Whether human has been challenged now
  private boolean humanHasBeenChallenged = false;
  // Whether computer has been challenged now
  private boolean computerHasBeenChallenged = false;
  // Prefix grows starting with empty string
  private String currentPrefix = "";
  // Indicates whether game has just started i.e. first valid move is not yet complete
  private boolean startOfGame = true;

  /**
   * Game constructor
   *
   * @param trie
   * @param dictionary
   * @param minWordLengthInDict
   */
  public Game(Trie trie, List<String> dictionary) {
    this.trie = trie;
    this.dictionary = dictionary;
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
   * Get all words with prefix in dictionary.
   *
   * @param prefix
   * @return
   */
  public List<String> searchWords(String prefix) {
    return trie.allWordsWithPrefix(prefix);
  }

  /**
   * Method to decide which player should make the first move.
   *
   * @return Player
   */
  public Player toss() {
    Random randomNumberGenerator = new Random();
    Player player = players[randomNumberGenerator.nextInt(players.length)];
    return player;
  }


  /**
   * Check whether player is a human.
   *
   * @param player
   * @return boolean
   */
  public boolean isHuman(Player player) {
    return player.equals(Player.HUMAN);
  }


  /**
   * Check whether player is a computer.
   *
   * @param player
   * @return boolean
   */
  public boolean isComputer(Player player) {
    return player.equals(Player.COMPUTER);
  }


  /**
   * Check whether input provided in a move is a challenge to opponent.
   *
   * @param input
   * @return boolean
   */
  public boolean isAChallenge(String input) {
    return input.equals(CHALLENGE);
  }


  /**
   * After each valid move toggle player to make the next move.
   */
  public void togglePlayerToMakeAMove() {
    if (isHuman(playerToMakeAMove)) {
      this.playerToMakeAMove = Player.COMPUTER;
    } else {
      this.playerToMakeAMove = Player.HUMAN;
    }
  }


  /**
   * Check whether human is bluffing.
   *
   * @param input
   * @return boolean
   */
  public boolean isHumanBluffing(String input) {
    return input.isEmpty();
  }


  /**
   * Check whether input provided by player is valid.
   *
   * @param input
   * @return boolean
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
   * Sanitize/massage input. Trim it. Convert to lowercase.
   *
   * @param input
   * @return
   */
  public String sanitize(String input) {
    return input.trim().toLowerCase();
  }


  /**
   * Check whether player has challenged the opponent.
   *
   * @param input
   * @return boolean
   */
  public boolean playerHasChallenged(String input) {
    if (input != null) {
      return input.equals(CHALLENGE);
    }

    return false;
  }


  /**
   * Get a random alphabet
   *
   * @return String
   */
  public String getAlphabetRandomly() {
    Random randomNumberGenerator = new Random();
    char alphabet = alphabets[randomNumberGenerator.nextInt(alphabets.length)];
    return String.valueOf(alphabet);
  }

  /**
   * Get a random computer move type.
   *
   * @return ComputerMoveType
   */
  public ComputerMoveType getComputerMoveTypeRandomly() {
    Random randomNumberGenerator = new Random();
    return computerMoveTypes[randomNumberGenerator.nextInt(computerMoveTypes.length)];
  }


  /**
   * Method to get input from console.
   *
   * @param scanner
   * @return String
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
   * Start the game!
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
