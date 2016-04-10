package edu.brown.cs.adehovit;

import edu.brown.cs.adehovit.autocorrect.Node;
import edu.brown.cs.adehovit.autocorrect.TrieData;
import edu.brown.cs.adehovit.reader.Reader;
import org.junit.Test;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adamdeho on 2/28/16.
 */
public class NodeTest {

  @Test
  public void simpleNode(){
    Node a = new Node("hey");
    boolean equals = a.getValue().equals("hey")
            && a.isWord()
            && a.getChildren().size() == 0;
    assertTrue(equals);
  }

  @Test
  public void biggerNode(){
    Map<String, TrieData> children = new HashMap<String, TrieData>();
    Node b = new Node("there");
    children.put("r", b);
    Node a = new Node(false, children, "the" );
    boolean equals = a.getValue().equals("the")
            && ! a.isWord()
            && a.getChildren().size() == 1
            && a.getChildren().get("r").equals(b);

    assertTrue(equals);
  }



}
