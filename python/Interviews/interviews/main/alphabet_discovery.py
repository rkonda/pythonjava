from queue import PriorityQueue

class CustomPriorityQueue(PriorityQueue):
    class PriorityQueueEntry:
        def __init__(self, priority_queue, character):
            assert isinstance(priority_queue, CustomPriorityQueue)  #isinstance is the only way PyCharm knows the type

            self.character = character
            self.priority_queue = priority_queue

        def __lt__(self, other):
            return self.priority_queue.in_edge_count_dict[self.character] < \
                   self.priority_queue.in_edge_count_dict[other.character]

        def __repr__(self):
            return self.character

    def __init__(self, character_graph_node_dict):
        super(CustomPriorityQueue, self).__init__()

        self.in_edge_count_dict = {}
        self.character_graph_node_dict = dict(character_graph_node_dict)

        for character in self.character_graph_node_dict.keys():
            if(character not in self.in_edge_count_dict):
                self.in_edge_count_dict[character] = 0

            for antecedentCharacter in self.character_graph_node_dict.get(character).antecedents:
                if(antecedentCharacter not in self.in_edge_count_dict):
                    self.in_edge_count_dict[antecedentCharacter] = 0

                self.in_edge_count_dict[antecedentCharacter] += 1


    def remove(self, character):
        del self.in_edge_count_dict[character]
        for antecedentCharacter in self.character_graph_node_dict[character].antecedents:
            assert antecedentCharacter in self.in_edge_count_dict
            assert self.in_edge_count_dict[antecedentCharacter] > 0
            self.in_edge_count_dict[antecedentCharacter] -= 1


class AlphabetGraphNode:
    def __init__(self, character):
        self.character = character
        self.antecedents = set()

    def __repr__(self):
        return "C:" + self.character + ",A:" + str(self.antecedents)


class AlphabetDiscovery(object):
    def __init__(self, words):
        self.words = list(words)
        self.character_graph_node_dict = {}
        self.alphabet = None

    def get_alphabet(self):
        if(self.alphabet is not None):
            return self.alphabet

        if(len(self.words) is 0):
            self.alphabet = []
            return self.alphabet

        if(len(self.words) is 1):
            self.alphabet = self.words[0]
            return self.alphabet

        self.processWords(0, len(self.words), 0)

        self.alphabet = self.compute_alphabet(self.words, self.character_graph_node_dict)

        return self.alphabet

    # process words creating a directed acyclic graph
    def processWords(self, word_start_index, word_count, character_skip_count):

        # create the newly found character, and graph node
        def create_graph_node(characterSkipCount, wordIndex):
            character = self.words[wordIndex][characterSkipCount]
            if (character not in self.character_graph_node_dict):
                graph_node = AlphabetGraphNode(character)
                self.character_graph_node_dict[character] = graph_node

            return character


        word_end_index = word_start_index + word_count

        if(len(self.words[word_start_index]) == character_skip_count):
            word_start_index += 1

        current_character = create_graph_node(character_skip_count, word_start_index)

        previous_character = current_character

        flag_range_discovered = False
        range_start_index = -1
        word_index = -1
        for index in range(word_start_index + 1, word_end_index):
            word_index = index
            if(self.words[word_index][character_skip_count] == previous_character):
                if(not flag_range_discovered):
                    # start range
                    flag_range_discovered = True
                    range_start_index = word_index - 1
            else:
                # complete the range; process it
                if(flag_range_discovered):
                    assert range_start_index >= 0
                    self.processWords(range_start_index, word_index - range_start_index, character_skip_count + 1)

                    # close range
                    flag_range_discovered = False
                    range_start_index = -1

                # create the newly found character, and graph node
                current_character = create_graph_node(character_skip_count, word_index)

                # create the graph connection
                self.character_graph_node_dict[previous_character].antecedents.add(current_character)

                # reset the previous character
                previous_character = current_character

        # complete the range; process it
        if(flag_range_discovered):
            assert range_start_index >= 0
            self.processWords(range_start_index, word_index - range_start_index, character_skip_count + 1)



    def compute_alphabet(self, words, character_graph_node_dict):

        alphabet = []

        queue = CustomPriorityQueue(character_graph_node_dict)
        for character in character_graph_node_dict.keys():
            queue.put(CustomPriorityQueue.PriorityQueueEntry(queue, character))

        while(not queue.empty()):
            character_queue_entry = queue.get()
            assert queue.in_edge_count_dict[character_queue_entry.character] == 0
            queue.remove(character_queue_entry.character)
            alphabet.append(character_queue_entry.character)

        unorderedCharacters = set()

        for word in words:
            for character in word:
                if character not in self.character_graph_node_dict:
                    unorderedCharacters.add(character)

        for character in unorderedCharacters:
            alphabet.append(character)

        return alphabet

























