package edu.brown.cs.adehovit;

import edu.brown.cs.adehovit.autocorrect.Command;
import edu.brown.cs.adehovit.autocorrect.WordData;
import edu.brown.cs.adehovit.reader.Reader;
import org.junit.Test;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamdeho on 2/28/16.
 */
public class CommandTest {

  @Test
  public void prefixMatch(){
    List<String> words = new ArrayList<String>();
    words.add("the");
    words.add("then");
    words.add("there");
    words.add("thers");
    words.add("therst");
    words.add("hat");
    words.add("th");
    List<List<String>> corpuses = new ArrayList<>();
    corpuses.add(words);
    WordData main = new WordData(corpuses);
    Command command = new Command(main, new ArrayList<String>(),
            "the", "", false);
    command.prefix();
    List<String> answers = command.getAnswers();
    List<String> compare = new ArrayList<String>();

    compare.add("then");
    compare.add("there");
    compare.add("thers");
    compare.add("therst");

    Boolean equals = compare.size() == answers.size();
    for(String word: answers){
      equals = equals && compare.contains(word);
    }
    assertTrue(equals);
  }

  @Test
  public void levTest(){
    List<String> words = new ArrayList<String>();
    words.add("the");
    words.add("then");
    words.add("there");
    words.add("thers");
    words.add("therst");
    words.add("h");
    words.add("hhe");
    words.add("th");
    List<List<String>> corpuses = new ArrayList<>();
    corpuses.add(words);
    WordData main = new WordData(corpuses);
    Command command = new Command(main, new ArrayList<String>(),
            "the", "", false);
    command.levenshtein(1);
    List<String> answers = command.getAnswers();
    List<String> compare = new ArrayList<String>();

    compare.add("hhe");
    compare.add("th");
    compare.add("the");
    compare.add("then");
    Boolean equals = compare.size() == answers.size();

    assertTrue(equals);
    for(String word: answers){
      equals = equals && compare.contains(word);
    }
    assertTrue(equals);
  }
  @Test
  public void whiteSpaceTest() {
    List<String> words = new ArrayList<String>();

    words.add("manbearpig");
    words.add("man");
    words.add("pig");
    words.add("manbear");
    words.add("bearpig");
    words.add("the");
    words.add("then");
    words.add("there");
    words.add("h");
    words.add("hhe");
    words.add("th");
    List<List<String>> corpuses = new ArrayList<>();
    corpuses.add(words);
    WordData main = new WordData(corpuses);
    Command command = new Command(main, new ArrayList<String>(),
            "manbearpig", "", false);
    command.whitespace();
    List<String> answers = command.getAnswers();
    List<String> compare = new ArrayList<String>();

    compare.add("man bearpig");
    compare.add("manbear pig");
    Boolean equals = compare.size() == answers.size();
    assertTrue(equals);
    equals = true;
    for(int i = 0; i < answers.size(); i++) {
      equals = equals && (answers.get(i).equals(compare.get(i)));
    }
    assertTrue(equals);
  }
  @Test
  public void testBigramUnigramPrefixLedOrdering() {
    /*
    Hi my name is
    adam and is
    adam is2adan
    is aden
    is Aden
    aden*/
    File dict = new File("data/small_corpus.txt");
    Reader red = new Reader();
    List<String> words = new ArrayList<>();
    try {
      words = red.read(dict);
    } catch (IOException e) {
      assertTrue(false);
    }
    List<List<String>> corpuses = new ArrayList<>();
    corpuses.add(words);
    WordData main = new WordData(corpuses);
    Command command = new Command(main, new ArrayList<String>(),
            "ad", "is", false);
    command.prefix();
    command.levenshtein(1);
    List<String> correct = new ArrayList<String>();
    correct.add("aden");
    correct.add("adam");
    correct.add("adan");
    correct.add("and");
    Boolean equals = true;
    List<String> answers = command.getAnswers();
    for(int i = 0; i < answers.size(); i++) {
      equals = equals && (answers.get(i).equals(correct.get(i)));
    }
    assertTrue(equals);
  }

  @Test
  public void rankingTest(){
    List<String> words = new ArrayList<String>();
    words.add("hero");
    words.add("heros");
    words.add("heros");
    words.add("her");
    words.add("the");
    words.add("her");
    words.add("herosa");
    words.add("herosa");

    List<List<String>> corpuses = new ArrayList<>();
    corpuses.add(words);
    WordData main = new WordData(corpuses);
    Command command = new Command(main, new ArrayList<String>(),
            "hero", "the", false);
    command.levenshtein(1);
    command.prefix();
    command.exact();
    List<String> answers = command.getAnswers();
    List<String> compare = new ArrayList<String>();
    compare.add("hero");
    compare.add("her");
    compare.add("heros");
    compare.add("herosa");

    Boolean equals = compare.size() == answers.size();

    assertTrue(equals);
    for(int i = 0; i < answers.size(); i++) {
      equals = equals && (answers.get(i).equals(compare.get(i)));
    }
    assertTrue(equals);
  }
  @Test
  public void alphabetTest(){
    List<String> words = new ArrayList<String>();
    words.add("a");
    words.add("b");
    words.add("c");
    words.add("d");
    words.add("e");
    words.add("f");
    words.add("g");
    words.add("h");
    List<List<String>> corpuses = new ArrayList<>();
    corpuses.add(words);
    WordData main = new WordData(corpuses);
    Command command = new Command(main, new ArrayList<String>(),
            "a", "", false);
    command.levenshtein(1);
    command.prefix();
    command.exact();
    List<String> answers = command.getAnswers();
    List<String> compare = new ArrayList<String>();
    compare.add("a");
    compare.add("b");
    compare.add("c");
    compare.add("d");
    compare.add("e");

    Boolean equals = compare.size() == answers.size();

    assertTrue(equals);
    for(int i = 0; i < answers.size(); i++) {
      equals = equals && (answers.get(i).equals(compare.get(i)));
    }
    assertTrue(equals);
  }

}
