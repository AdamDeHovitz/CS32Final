package edu.brown.cs.adehovit;

import edu.brown.cs.adehovit.autocorrect.Node;
import edu.brown.cs.adehovit.reader.Reader;
import edu.brown.cs.adehovit.autocorrect.TrieData;
import edu.brown.cs.adehovit.autocorrect.WordData;
import org.junit.Test;



import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by adamdeho on 2/29/16.
 */
public class TrieTest {
  @Test
  public void trieParent(){
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


    Boolean equal = true;
    Node head = main.getTrieHead();
    equal = head.getValue().equals("");
    assertTrue(equal);

    Map<String, TrieData> children = head.getChildren();
    List<String> compare = new ArrayList<String>();
    compare.add("t");
    compare.add("do");
    compare.add("que");
    equal = compare.size() == children.size();
    //System.out.println(equal);
    for(TrieData child: children.values()){
      //System.out.println(child);
      equal = equal && compare.contains(child.getValue());
    }
    assertTrue(equal);
  }

  @Test
  public void trieTestIterator(){
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
    WordData main = new WordData(corpuses);

    List<String> correct = new ArrayList<String>();
    correct.add("the");
    correct.add("then");
    correct.add("that");
    correct.add("to");
    correct.add("do");
    correct.add("done");
    correct.add("doth");
    correct.add("que");

    Boolean equal = true;
    Node head = main.getTrieHead();

    Iterator trier = main.getTrie().iterator();


    //System.out.println(equal);
    //make sure it's all there
    for(String word: main.getTrie()){
      //System.out.println(child);
      equal = equal && correct.remove(word);
    }
    equal = equal && (correct.size() == 0);
    assertTrue(equal);
  }
  @Test
  public void trieTestIteratorInsert(){
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
    WordData main = new WordData(corpuses);

    Node head = main.getTrieHead();
    boolean equal = main.getTrie().size() == 8;

    main.getTrie().insert(new Node("brown"), head, 0);

    equal = equal && main.getTrie().size() == 9;

    List<String> correct = new ArrayList<String>();
    correct.add("the");
    correct.add("then");
    correct.add("that");
    correct.add("to");
    correct.add("do");
    correct.add("done");
    correct.add("doth");
    correct.add("que");
    correct.add("brown");


    //checking inserted node
    equal = equal && (head.getChildren().get("b").getValue().equals("brown"));

    Iterator trier = main.getTrie().iterator();


    //System.out.println(equal);
    //make sure it's all there
    for(String word: main.getTrie()){
      //System.out.println(child);
      equal = equal && correct.remove(word);
    }
    equal = equal && (correct.size() == 0);
    assertTrue(equal);
  }
}
