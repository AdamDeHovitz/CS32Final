package edu.brown.cs.adehovit;

import edu.brown.cs.adehovit.autocorrect.WordData;
import edu.brown.cs.adehovit.reader.Reader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by adamdeho on 3/1/16.
 */
public class ReaderTest {

  @Test
  public void testSmallData(){
    File dict = new File("data/small-dictionary.txt");
    Reader red = new Reader();
    List<String> words = new ArrayList<>();
    try {
      words = red.read(dict);
    } catch (IOException e) {
      assertTrue(false);
    }

    List<String> correct = new ArrayList<String>();
    correct.add("the");
    correct.add("then");
    correct.add("that");
    correct.add("to");
    correct.add("do");
    correct.add("done");
    correct.add("doth");
    correct.add("que");

    Boolean equals = correct.size() == words.size();

    assertTrue(equals);
    for(int i = 0; i < words.size(); i++) {
      equals = equals && (words.get(i).equals(correct.get(i)));
    }
    assertTrue(equals);
  }
  @Test
  public void testWeirdData(){
    File dict = new File("data/weird.txt");
    Reader red = new Reader();
    List<String> words = new ArrayList<>();
    try {
      words = red.read(dict);
    } catch (IOException e) {
      assertTrue(false);
    }

    List<String> correct = new ArrayList<String>();
    correct.add("what");
    correct.add("can");
    correct.add("we");
    correct.add("expect");
    correct.add("from");
    correct.add("the");
    correct.add("man");
    correct.add("we");
    correct.add("mouse");
    correct.add("cheese");

    Boolean equals = correct.size() == words.size();

    assertTrue(equals);
    for(int i = 0; i < words.size(); i++) {
      equals = equals && (words.get(i).equals(correct.get(i)));
    }
    assertTrue(equals);
  }
}


