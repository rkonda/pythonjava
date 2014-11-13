package interviews.graph;

import com.google.common.collect.Lists;
import interviews.graph.solution2.AlphabetDiscovery;
import org.junit.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Test
public class AlphabetDiscoveryTest {
  @BeforeMethod
  public void setUp(Method method)
  {
    Reporter.log("::" + method.getName(), true);
    System.out.flush();
  }

  public void testOneWord() {
    AlphabetDiscovery alphabetDiscovery = new AlphabetDiscovery(
        Lists.newArrayList("cat")
    );

    String alphabet = alphabetDiscovery.getAlphabet();

    Reporter.log(alphabet, true);
    System.out.flush();

    Assert.assertEquals(alphabet, "cat");
  }

  public void testTwoWords() {
    AlphabetDiscovery alphabetDiscovery = new AlphabetDiscovery(
        Lists.newArrayList("cat", "bat")
    );

    String alphabet = alphabetDiscovery.getAlphabet();

    Reporter.log(alphabet, true);
    System.out.flush();

    Map<Character, Integer> characterPositionMap = new HashMap<Character, Integer>();
    for(int characterIndex = 0; characterIndex < alphabet.length(); characterIndex++) {
      characterPositionMap.put(alphabet.charAt(characterIndex), characterIndex);
    }

    Assert.assertTrue(characterPositionMap.get('c') < characterPositionMap.get('b'));
  }

  public void testTwoWordsFirstCharacterMatches() {
    AlphabetDiscovery alphabetDiscovery = new AlphabetDiscovery(
        Lists.newArrayList("cat", "cbt")
    );

    String alphabet = alphabetDiscovery.getAlphabet();

    Reporter.log(alphabet, true);
    System.out.flush();

    Map<Character, Integer> characterPositionMap = new HashMap<Character, Integer>();
    for(int characterIndex = 0; characterIndex < alphabet.length(); characterIndex++) {
      characterPositionMap.put(alphabet.charAt(characterIndex), characterIndex);
    }

    Assert.assertTrue(characterPositionMap.get('a') < characterPositionMap.get('b'));
  }

  public void testPartialOrder() {
    AlphabetDiscovery alphabetDiscovery = new AlphabetDiscovery(
        Lists.newArrayList("cat", "cbt", "b")
    );

    String alphabet = alphabetDiscovery.getAlphabet();

    Reporter.log(alphabet, true);
    System.out.flush();

    Map<Character, Integer> characterPositionMap = new HashMap<Character, Integer>();
    for(int characterIndex = 0; characterIndex < alphabet.length(); characterIndex++) {
      characterPositionMap.put(alphabet.charAt(characterIndex), characterIndex);
    }

    Assert.assertTrue(characterPositionMap.get('a') < characterPositionMap.get('b'));
    Assert.assertTrue(characterPositionMap.get('c') < characterPositionMap.get('b'));
  }

  public void testPartialOrder2() {
    AlphabetDiscovery alphabetDiscovery = new AlphabetDiscovery(
        Lists.newArrayList("ab", "ad", "b", "c", "d")
    );

    String alphabet = alphabetDiscovery.getAlphabet();

    Reporter.log(alphabet, true);
    System.out.flush();

    Map<Character, Integer> characterPositionMap = new HashMap<Character, Integer>();
    for(int characterIndex = 0; characterIndex < alphabet.length(); characterIndex++) {
      characterPositionMap.put(alphabet.charAt(characterIndex), characterIndex);
    }

    Assert.assertTrue(characterPositionMap.get('a') < characterPositionMap.get('b'));
    Assert.assertTrue(characterPositionMap.get('b') < characterPositionMap.get('c'));
    Assert.assertTrue(characterPositionMap.get('c') < characterPositionMap.get('d'));
  }

  public void testPartialOrder3() {
    AlphabetDiscovery alphabetDiscovery = new AlphabetDiscovery(
        Lists.newArrayList("ab", "ac", "ad", "beb", "bfb", "cgc", "chc", "dgd")
    );

    String alphabet = alphabetDiscovery.getAlphabet();

    Reporter.log(alphabet, true);
    System.out.flush();

    Map<Character, Integer> characterPositionMap = new HashMap<Character, Integer>();
    for(int characterIndex = 0; characterIndex < alphabet.length(); characterIndex++) {
      characterPositionMap.put(alphabet.charAt(characterIndex), characterIndex);
    }

    Assert.assertTrue(characterPositionMap.get('a') < characterPositionMap.get('b'));
    Assert.assertTrue(characterPositionMap.get('b') < characterPositionMap.get('c'));
    Assert.assertTrue(characterPositionMap.get('c') < characterPositionMap.get('d'));
    Assert.assertTrue(characterPositionMap.get('e') < characterPositionMap.get('b'));
    Assert.assertTrue(characterPositionMap.get('f') < characterPositionMap.get('b'));
    Assert.assertTrue(characterPositionMap.get('g') < characterPositionMap.get('c'));
    Assert.assertTrue(characterPositionMap.get('h') < characterPositionMap.get('c'));
    Assert.assertTrue(characterPositionMap.get('g') < characterPositionMap.get('d'));
  }

}
