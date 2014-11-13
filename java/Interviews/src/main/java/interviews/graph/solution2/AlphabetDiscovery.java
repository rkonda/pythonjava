package interviews.graph.solution2;

import com.google.common.base.Joiner;

import java.util.*;

/**
 * Directed Graph
 */
class AlphabetGraphNode {
  final Character character;
  final Set<Character> antecedents;

  public AlphabetGraphNode(Character character) {
    this.character = character;
    this.antecedents = new HashSet<Character>();
  }

  @Override
  public String toString() {
    return "C:" + this.character + ",A:" + Joiner.on(',').join(this.antecedents);
  }
}

public class AlphabetDiscovery {
  final List<String> words;
  String alphabet;
  final Map<Character, AlphabetGraphNode> characterGraphNodeMap;


  public AlphabetDiscovery(List<String> words) {
    this.words = words;
    this.characterGraphNodeMap = new HashMap<Character, AlphabetGraphNode>();
  }

  public String getAlphabet() {
    if(this.alphabet != null) {
      return this.alphabet;
    }

    if(this.words.size() == 0) {
      this.alphabet = "";
      return this.alphabet;
    }

    if(this.words.size() == 1) {
      this.alphabet = this.words.get(0);
      return this.alphabet;
    }

    processWords(0, this.words.size(), 0);

    this.alphabet = computeAlphabet(this.words, this.characterGraphNodeMap, new CharacterComparator());

    return this.alphabet;
  }

  // process words creating a directed acyclic graph
  void processWords(int wordStartIndex, int wordCount, int characterSkipCount) {
    int wordIndex = wordStartIndex;
    int wordEndIndex = wordStartIndex + wordCount;

    if(this.words.get(wordStartIndex).length() == characterSkipCount) {
      wordIndex++;
    }

    // create the newly found character, and graph node
    Character currentCharacter = this.words.get(wordIndex).charAt(characterSkipCount);
    if(!this.characterGraphNodeMap.containsKey(currentCharacter)) {
      this.characterGraphNodeMap.put(currentCharacter, new AlphabetGraphNode(currentCharacter));
    }

    Character previousCharacter = currentCharacter;
    boolean flagRangeDiscovered = false;
    int rangeStartIndex = -1;
    for(wordIndex++; wordIndex < wordEndIndex; wordIndex++) {
      if(this.words.get(wordIndex).charAt(characterSkipCount) == previousCharacter) {
        if(!flagRangeDiscovered) {
          // start range
          flagRangeDiscovered = true;
          rangeStartIndex = wordIndex - 1;
        }

      } else {

        // complete the range; process it
        if(flagRangeDiscovered) {
          assert rangeStartIndex >= 0;
          processWords(rangeStartIndex, wordIndex - rangeStartIndex, characterSkipCount + 1);

          // close range
          flagRangeDiscovered = false;
          rangeStartIndex = -1;
        }

        // create the newly found character, and graph node
        currentCharacter = this.words.get(wordIndex).charAt(characterSkipCount);
        if(!this.characterGraphNodeMap.containsKey(currentCharacter)) {
          this.characterGraphNodeMap.put(currentCharacter, new AlphabetGraphNode(currentCharacter));
        }

        // create the graph connection
        this.characterGraphNodeMap.get(previousCharacter).antecedents.add(currentCharacter);

        // reset the previous character
        previousCharacter = currentCharacter;
      }
    }

    // complete the range; process it
    if(flagRangeDiscovered) {
      assert rangeStartIndex >= 0;
      processWords(rangeStartIndex, wordIndex - rangeStartIndex, characterSkipCount + 1);
    }

  } // for

  class CharacterComparator implements Comparator<Character> {
    final Map<Character, Integer> inEdgeCountMap;

    public CharacterComparator() {
      this.inEdgeCountMap = new HashMap<Character, Integer>();
      for(Character character : characterGraphNodeMap.keySet()) {
        if(!this.inEdgeCountMap.containsKey(character)) {
          this.inEdgeCountMap.put(character, 0);
        }

        for(Character antecedentCharacter : characterGraphNodeMap.get(character).antecedents) {
          if(!this.inEdgeCountMap.containsKey(antecedentCharacter)) {
            this.inEdgeCountMap.put(antecedentCharacter, 0);
          }

          this.inEdgeCountMap.put(antecedentCharacter, this.inEdgeCountMap.get(antecedentCharacter) + 1);
        }
      }
    }

    @Override
    public int compare(Character firstCharacter, Character secondCharacter) {
      return Integer.compare(this.inEdgeCountMap.get(firstCharacter), this.inEdgeCountMap.get(secondCharacter));
    }

    void removeCharacter(Character character) {
      this.inEdgeCountMap.remove(character);
      for(Character antecedentCharacter : characterGraphNodeMap.get(character).antecedents) {
        assert this.inEdgeCountMap.containsKey(antecedentCharacter);
        assert this.inEdgeCountMap.get(antecedentCharacter) > 0;
        this.inEdgeCountMap.put(antecedentCharacter, this.inEdgeCountMap.get(antecedentCharacter) - 1);
      }
    }
  }

  static String computeAlphabet(
      final List<String> words,
      final Map<Character, AlphabetGraphNode> characterGraphNodeMap,
      final CharacterComparator characterComparator) {

    StringBuilder alphabet = new StringBuilder();

    PriorityQueue<Character> characterPriorityQueue = new PriorityQueue<Character>(characterComparator);
    characterPriorityQueue.addAll(characterGraphNodeMap.keySet());
    while(!characterPriorityQueue.isEmpty()) {
      Character character = characterPriorityQueue.remove();
      assert characterComparator.inEdgeCountMap.get(character) == 0;
      characterComparator.removeCharacter(character);
      alphabet.append(character);
    }

    Set<Character> unorderedCharacters = new HashSet<Character>();

    for(String word : words) {
      for(int characterIndex = 0; characterIndex < word.length(); characterIndex++) {
        Character character = word.charAt(characterIndex);
        if(!characterGraphNodeMap.containsKey(character)) {
          unorderedCharacters.add(character);
        }
      }
    }

    alphabet.append(Joiner.on("").join(unorderedCharacters));

    return alphabet.toString();
  }

}
