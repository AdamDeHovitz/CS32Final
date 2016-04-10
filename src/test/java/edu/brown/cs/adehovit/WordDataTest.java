package edu.brown.cs.adehovit;

import edu.brown.cs.adehovit.reader.Reader;
import edu.brown.cs.adehovit.autocorrect.WordData;
import org.junit.Test;



import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by adamdeho on 2/28/16.
 */
public class WordDataTest {

  @Test
  public void testSmallData(){
    File dict = new File("data/small-dictionary.txt");
    Reader red = new Reader();
    List<List<String>> corpuses = new ArrayList<>();
    List<String> words = new ArrayList<>();
    try {
      words = red.read(dict);
    } catch (IOException e) {
      assertTrue(false);
    }
    corpuses.add(words);
    //for (String word : words) { System.out.println(word); }
    WordData main = new WordData(corpuses);

    List<String> correct = new ArrayList<String>();
    correct.add("the");
    correct.add("then");
    correct.add("that");
    correct.add("to");
    correct.add("do");
    correct.add("done");
    correct.add("doth");

    Boolean equal = true;
    for (String word:correct){
      equal = equal && main.getBigram().containsKey(word);
    }
    assertTrue(equal);

  }
  @Test
  public void testSmallCorpus(){
    /*
    Hi my name is
    adam and is
    adam is2adan
    is aden
    is Aden
    aden*/
    File dict = new File("data/small_corpus.txt");
    Reader red = new Reader();
    List<List<String>> corpuses = new ArrayList<>();
    List<String> words = new ArrayList<>();
    try {
      words = red.read(dict);
    } catch (IOException e) {
      assertTrue(false);
    }
    corpuses.add(words);
    //for (String word : words) { System.out.println(word); }
    WordData main = new WordData(corpuses);

    List<String> correct = new ArrayList<String>();
    correct.add("hi");
    correct.add("my");
    correct.add("name");
    correct.add("is");
    correct.add("adam");
    correct.add("aden");
    correct.add("adan");
    correct.add("and");

    Boolean equal = true;
    for (String word:correct){
      equal = equal && main.getBigram().containsKey(word);
    }
    assertTrue(equal);
    equal = (main.getFrequency().get("aden") == 3)
            && (main.getFrequency().get("adan") == 1)
            && (main.getFrequency().get("adam") == 2);
    for(String word: main.getTrie()){
      //System.out.println(child);
      equal = equal && correct.remove(word);
    }
    equal = equal && (correct.size() == 0);
    assertTrue(equal);
  }
  @Test
  public void testTwoFilesBigram(){

    Reader red = new Reader();
    List<List<String>> corpuses = new ArrayList<>();
    try {
      corpuses.add(red.read(new File("data/double_a.txt")));
      corpuses.add(red.read(new File("data/double_b.txt")));
    } catch (IOException e) {
      assertTrue(false);
    }
    WordData main = new WordData(corpuses);


    Boolean equal = main.getBigram().get("is") == null
            && main.getBigram().get("b") != null;
    assertTrue(equal);
  }
  @Test
  public void frequencyCrossFile(){

    List<List<String>> corpuses = new ArrayList<>();
    List<String> a = new ArrayList<String>();
    List<String> b = new ArrayList<String>();
    a.add("a");
    a.add("a");
    b.add("a");
    b.add("b");
    b.add("a");

    corpuses.add(a);
    corpuses.add(b);
    WordData main = new WordData(corpuses);


    Boolean equal = main.getFrequency().get("a") == 4
            && main.getFrequency().get("c") == null
            && main.getFrequency().get("b") != 2;
    assertTrue(equal);
  }
}
