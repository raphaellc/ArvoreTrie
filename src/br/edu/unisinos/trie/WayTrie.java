package br.edu.unisinos.trie;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class WayTrie<V> implements TrieADT<V> {
	private static final int R = 256; // extended ASCII

	private Node root;

	private static class Node {
		private Object value;
		private Node[] next = new Node[R];
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public V search(String key) {
		Node node = search(root, key, 0);
		if (node == null)
			return null;
		return (V) node.value;
	}

	private Node search(Node node, String key, int index) {
		if (node == null)
			return null;
		if (index == key.length())
			return node;
		char c = key.charAt(index);
		return search(node.next[c], key, index + 1);
	}

	@Override
	public void insert(String key, V value) {
		root = insert(root, key, value, 0);
	}

	private Node insert(Node node, String key, V value, int index) {
		if (node == null)
			node = new Node();
		if (index == key.length()) {
			node.value = value;
			return node;
		}
		char c = key.charAt(index);
		node.next[c] = insert(node.next[c], key, value, index + 1);
		return node;
	}

	@Override
	public void delete(String key) {
		root = delete(root, key, 0);
	}

	private Node delete(Node node, String key, int index) {
		if (node == null)
			return null;
		if (index == key.length()) {
			node.value = null;
		} else {
			char c = key.charAt(index);
			node.next[c] = delete(node.next[c], key, index + 1);
		}

		if (node.value != null)
			return node;
		for (int c = 0; c < R; c++)
			if (node.next[c] != null)
				return node;
		return null;
	}

	@Override
	public Iterable<String> keysWithPrefix(String prefix) {
		Queue<String> results = new LinkedList<>();
		Node node = search(root, prefix, 0);
		collect(node, new StringBuilder(prefix), results);
		return results;
	}

	private void collect(Node node, StringBuilder prefix, Queue<String> results) {
		if (node == null)
			return;
		if (node.value != null)
			results.add(prefix.toString());
		for (char c = 0; c < R; c++) {
			prefix.append(c);
			collect(node.next[c], prefix, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	public Iterable<String> keysWithPrefixPattern(String prefix) {
		Queue<String> results = new LinkedList<>();
		keysWithPrefixPattern(root, new StringBuilder(prefix), results, 0);
		return results;
	}

	private void keysWithPrefixPattern(Node node, StringBuilder prefix, Queue<String> results, int index) {
		if (node == null)
			return;
		char c = '.';
		if (prefix.length() > index)
			c = prefix.charAt(index);
		if (node.value != null && index >= prefix.length())
			results.add("" + node.value);
		if (c == '.') {
			for (int i = 0; i < R; i++) {
				if (node.next[i] != null) {
					keysWithPrefixPattern(node.next[i], prefix, results, index + 1);
				}
			}
		} else if (node.next[c] != null) {
			keysWithPrefixPattern(node.next[c], prefix, results, index + 1);
		}
	}

	public int countKeysWithPrefix(String prefix) {
		return ((Collection<?>) keysWithPrefix(prefix)).size();
	}
	
	public String longestPrefixOf(String key) {
		int length = longestPrefixOf(root, key, 0, -1);
		if (length == -1)
			return null;
		return key.substring(0, length);
	}

	private int longestPrefixOf(Node node, String key, int index, int length) {
		if (node == null)
			return length;
		if (node.value != null)
			length = index;
		if (index == key.length())
			return length;
		char c = key.charAt(index);
		return longestPrefixOf(node.next[c], key, index + 1, length);
	}
	
	public Iterable<String> keysByPattern(String pattern) {
		Queue<String> results = new LinkedList<>();
		keysByPattern(root, new StringBuilder(), pattern, results);
		return results;
	}

	private void keysByPattern(Node node, StringBuilder prefix, String pattern, Queue<String> results) {
		if (node == null)
			return;
		if (prefix.length() == pattern.length()) {
			if (node.value != null)
				results.add(prefix.toString());
			return;
		}
		char c = pattern.charAt(prefix.length());
		if (c == '.') {
			for (char ch = 0; ch < R; ch++) {
				if (node.next[ch] != null) {
					prefix.append(ch);
					keysByPattern(node.next[ch], prefix, pattern, results);
					prefix.deleteCharAt(prefix.length() - 1);
				}
			}
		} else {
			prefix.append(c);
			keysByPattern(node.next[c], prefix, pattern, results);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

}
