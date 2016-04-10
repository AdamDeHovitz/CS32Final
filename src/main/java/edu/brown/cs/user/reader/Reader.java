package edu.brown.cs.adehovit.reader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Reads in words from text files.
 * Splits on none letter characters and drops case.
 *
 * @author Adam DeHovitz
 */


public class Reader {

  private static final String CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

  /**
   * Reads in a file and returns a list of all words, lowercase.
   *
   * @param file The File to be read in
   * @return a List of Strings
   * @throws IOException in case of file reader error
   */
  public List<String> read(File file) throws IOException {
    BufferedReader br = null;
    String line = "";
    List<String> retList = new ArrayList<String>();
    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader in = new InputStreamReader(fis, "UTF-8");
      br = new BufferedReader(in);

      while ((line = br.readLine()) != null) {
        parseLine(retList, line);
      }
    } catch (FileNotFoundException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          throw e;
        }
      }
    }
    return retList;
  }

  /**
   * Parses a String that represents a line and adds all words (lowercase).
   * Words are all alphabetical characters seperated by anyamount of none
   * alphabetical characters
   *
   * @param wordList list of words to add words to
   * @param line     the String to be parsed
   */
  public static void parseLine(List<String> wordList, String line) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < line.length(); i++) {
      String letter = line.substring(i, i + 1);
      if (LETTERS.contains(letter)) {
        buf.append(letter);
      } else if (CAPS.contains(letter)) {
        buf.append(LETTERS.charAt(CAPS.indexOf(letter)));
      } else if (buf.length() != 0) {
        wordList.add(buf.toString());
        buf.setLength(0);
      }
    }
    if (buf.length() != 0) {
      wordList.add(buf.toString());
    }
  }

  /**
   * returns 52 letters representing the 26 letters, caps and uncaps.
   *
   * @return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
   */
  public static String getCapsAndLetters() {
    return LETTERS + CAPS;
  }
}
