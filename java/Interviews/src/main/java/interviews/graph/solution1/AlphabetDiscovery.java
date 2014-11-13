package interviews.graph.solution1;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Directed Graph
 */
class AlphabetGraphNode {
  final Character character;
  final Set<Character> antecedents;

  public AlphabetGraphNode(Character character) {
    this.character = character;
    this.antecedents = new HashSet<>();
  }

  @Override
  public String toString() {
    return "C:" + this.character + ",A:" + Joiner.on(',').join(this.antecedents);
  }
}

public class AlphabetDiscovery {
  final List<String> words;
  List<Character> alphabet;
  String alphabetAsString;
  final Map<Character, AlphabetGraphNode> characterGraphNodeMap;


  public AlphabetDiscovery(List<String> words) {
    this.words = words;
    this.characterGraphNodeMap = new HashMap<>();
  }

  public List<Character> getAlphabet() {
    if(alphabet != null) {
      return alphabet;
    }

    if(this.words.size() == 0) {
      this.alphabet = new ArrayList<>();
      return this.alphabet;
    }

    if(this.words.size() == 1) {
      this.alphabet = Lists.charactersOf(this.words.get(0));
      return this.alphabet;
    }

    processWords(0, this.words.size(), 0);

    this.alphabet = computeAlphabet(this.words, this.characterGraphNodeMap, new CharacterComparator());

    return this.alphabet;
  }

  public String getAlphabetAsString() {
    if(this.alphabetAsString != null) {
      return this.alphabetAsString;
    }

    if(this.words.size() == 0) {
      this.alphabetAsString = "";
      return this.alphabetAsString;
    }

    if(this.words.size() == 1) {
      this.alphabetAsString = this.words.get(0);
      return this.alphabetAsString;
    }

    StringBuilder alphabetStringBuilder = new StringBuilder();
    List<Character> alphabet = this.getAlphabet();
    for(Character character : alphabet) {
      alphabetStringBuilder.append(character);
    }

    this.alphabetAsString = alphabetStringBuilder.toString();

    return this.alphabetAsString;
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
      this.inEdgeCountMap = new HashMap<>();
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

  static List<Character> computeAlphabet(
      final List<String> words,
      final Map<Character, AlphabetGraphNode> characterGraphNodeMap,
      final CharacterComparator characterComparator) {

    List<Character> alphabet = new ArrayList<Character>();

    PriorityQueue<Character> characterPriorityQueue = new PriorityQueue<>(characterComparator);
    characterPriorityQueue.addAll(characterGraphNodeMap.keySet());
    while(!characterPriorityQueue.isEmpty()) {
      Character character = characterPriorityQueue.remove();
      assert characterComparator.inEdgeCountMap.get(character) == 0;
      characterComparator.removeCharacter(character);
      characterPriorityQueue.removeAll(characterGraphNodeMap.get(character).antecedents);
      characterPriorityQueue.addAll(characterGraphNodeMap.get(character).antecedents);
      alphabet.add(character);
    }

    Set<Character> unorderedCharacters = new HashSet<>();

    for(String word : words) {
      for(int characterIndex = 0; characterIndex < word.length(); characterIndex++) {
        Character character = word.charAt(characterIndex);
        if(!characterGraphNodeMap.containsKey(character)) {
          unorderedCharacters.add(character);
        }
      }
    }

    alphabet.addAll(unorderedCharacters);

    return alphabet;
  }

}
