from interviews.main import alphabet_discovery


def test_one_word():
    alphabet = alphabet_discovery.AlphabetDiscovery(["cat"]).get_alphabet()
    print(alphabet)

    assert alphabet == "cat"


def test_two_words():
    alphabet = alphabet_discovery.AlphabetDiscovery(["cat", "bat"]).get_alphabet()
    print(alphabet)

    character_position_dict = {}
    for characterIndex in range(0, len(alphabet)):
        character_position_dict[alphabet[characterIndex]] = characterIndex

    assert character_position_dict['c'] < character_position_dict['b']


def test_two_words_first_character_matches():
    alphabet = alphabet_discovery.AlphabetDiscovery(["cat", "cbt"]).get_alphabet()
    print(alphabet)

    character_position_dict = {}
    for characterIndex in range(0, len(alphabet)):
        character_position_dict[alphabet[characterIndex]] = characterIndex

    assert character_position_dict['a'] < character_position_dict['b']


def test_partial_order():
    alphabet = alphabet_discovery.AlphabetDiscovery(["cat", "cbt", "b"]).get_alphabet()
    print(alphabet)

    character_position_dict = {}
    for characterIndex in range(0, len(alphabet)):
        character_position_dict[alphabet[characterIndex]] = characterIndex

    assert character_position_dict['a'] < character_position_dict['b']
    assert character_position_dict['c'] < character_position_dict['b']


def test_partial_order2():
    alphabet = alphabet_discovery.AlphabetDiscovery(["ab", "ad", "b", "c", "d"]).get_alphabet()
    print(alphabet)

    character_position_dict = {}
    for characterIndex in range(0, len(alphabet)):
        character_position_dict[alphabet[characterIndex]] = characterIndex

    assert character_position_dict['a'] < character_position_dict['b']
    assert character_position_dict['b'] < character_position_dict['c']
    assert character_position_dict['c'] < character_position_dict['d']


def test_partial_order3():
    alphabet = alphabet_discovery.AlphabetDiscovery(["ab", "ac", "ad", "beb", "bfb", "bbb", "cgc", "chc", "ccc", "dgd", "ddd"]).get_alphabet()
    print(alphabet)

    character_position_dict = {}
    for characterIndex in range(0, len(alphabet)):
        character_position_dict[alphabet[characterIndex]] = characterIndex

    assert character_position_dict['a'] < character_position_dict['b']
    assert character_position_dict['b'] < character_position_dict['c']
    assert character_position_dict['c'] < character_position_dict['d']
    assert character_position_dict['e'] < character_position_dict['b']
    assert character_position_dict['f'] < character_position_dict['b']
    assert character_position_dict['g'] < character_position_dict['c']
    assert character_position_dict['h'] < character_position_dict['c']
    assert character_position_dict['g'] < character_position_dict['d']


def test_partial_order4():
    alphabet = alphabet_discovery.AlphabetDiscovery(["ab", "ad", "b", "c", "d", "e"]).get_alphabet()
    print(alphabet)

    character_position_dict = {}
    for characterIndex in range(0, len(alphabet)):
        character_position_dict[alphabet[characterIndex]] = characterIndex

    assert character_position_dict['a'] < character_position_dict['b']
    assert character_position_dict['b'] < character_position_dict['c']
    assert character_position_dict['c'] < character_position_dict['d']
    assert character_position_dict['d'] < character_position_dict['e']

