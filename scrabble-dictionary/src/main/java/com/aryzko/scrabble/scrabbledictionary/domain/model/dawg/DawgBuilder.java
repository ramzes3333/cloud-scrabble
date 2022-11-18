package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Based on Steve Hanov python script
 * Please see https://gist.github.com/smhanov/94230b422c2100ae4218
 */
public class DawgBuilder {
    private int idSeq = 0;
    private String previousWord = "";
    private final Node root = new Node(0);
    private final Map<Node, Node> minimizedNodes = new HashMap<>();
    private final Stack<UncheckedNode> uncheckedNodes = new Stack<>();

    public DawgBuilder insert(List<String> words) {
        words.stream().forEach(this::insert);
        return this;
    }

    public DawgBuilder insert(String word) {
        int commonPrefix = commonPrefix(word);
        minimize(commonPrefix);

        Node node = uncheckedNodes.size() == 0 ? root : uncheckedNodes.peek().childNode();

        for (Character character : word.substring(commonPrefix).toCharArray()) {
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

        return root;
    }

    private void minimize(int downTo) {
        for (var i = uncheckedNodes.size() - 1; i > downTo - 1; i--) {
            UncheckedNode unNode = uncheckedNodes.pop();
            Node parent = unNode.parentNode();
            Character character = unNode.character();
            Node child = unNode.childNode();

            if (minimizedNodes.containsKey(child)) {
                parent.getTransitions().put(character, minimizedNodes.get(child));
            } else {
                minimizedNodes.put(child, child);
            }
        }
    }

    private record UncheckedNode(Node parentNode, Character character, Node childNode) {
    }
}
