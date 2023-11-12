package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import lombok.extern.slf4j.Slf4j;

import java.text.Collator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;

/**
 * Based on Steve Hanov python script
 * Please see https://gist.github.com/smhanov/94230b422c2100ae4218
 */
@Slf4j
public class DawgBuilder {
    private int idSeq;
    private String previousWord;

    private final Node root;
    private final Map<Node, Node> minimizedNodes;
    private final Deque<UncheckedNode> uncheckedNodes;
    private final Collator collator;

    public DawgBuilder() {
        this.idSeq = 0;
        this.previousWord = "";

        this.collator = Collator.getInstance(new Locale("pl", "PL"));
        this.collator.setStrength(Collator.PRIMARY);

        this.root = new Node(0);
        this.minimizedNodes = new HashMap<>(150_000);
        this.uncheckedNodes = new ArrayDeque<>();
    }

    public DawgBuilder insert(List<String> words) {
        words.stream().forEach(this::insert);
        return this;
    }

    public DawgBuilder insert(String word) {
        if(collator.compare(word, previousWord) < 0) {
            throw new IllegalArgumentException(
                    format("Words must be inserted in alphabetical order (prev: %s, actual: %s)",
                            previousWord,
                            word));
        }
        int commonPrefix = commonPrefix(word);
        minimize(commonPrefix);

        Node node = uncheckedNodes.size() == 0 ? root : uncheckedNodes.peek().childNode();

        for (char character : word.substring(commonPrefix).toCharArray()) {
            var nextNode = new Node(++idSeq);
            node.getTransitions().put(character, nextNode);
            uncheckedNodes.push(new UncheckedNode(node, character, nextNode));
            node = nextNode;
        }

        node.setTerminal(true);
        previousWord = word;
        return this;
    }

    private int commonPrefix(String word) {
        int commonPrefix;
        for (commonPrefix = 0; commonPrefix < Math.min(word.length(), previousWord.length()); commonPrefix++) {
            if (word.charAt(commonPrefix) != previousWord.charAt(commonPrefix)) {
                return commonPrefix;
            }
        }
        return commonPrefix;
    }

    public Node build() {
        minimize(0);
        minimizedNodes.clear();
        uncheckedNodes.clear();
        previousWord = null;

        return root;
    }

    private void minimize(int downTo) {
        for (var i = uncheckedNodes.size() - 1; i > downTo - 1; i--) {
            UncheckedNode unNode = uncheckedNodes.pop();
            Node parent = unNode.parentNode();
            char character = unNode.character();
            Node child = unNode.childNode();

            if (minimizedNodes.containsKey(child)) {
                parent.getTransitions().put(character, minimizedNodes.get(child));
            } else {
                minimizedNodes.put(child, child);
            }
        }
    }

    private record UncheckedNode(Node parentNode, char character, Node childNode) {
    }
}
