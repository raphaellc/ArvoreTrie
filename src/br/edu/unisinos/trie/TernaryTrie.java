package br.edu.unisinos.trie;

import java.util.LinkedList;
import java.util.Queue;

public class TernaryTrie<V> implements TrieADT<V> {
	private Node<V> root;
	
	private static class Node<V> {
		private char c;
		private Node<V> left, middle, right;
		private V value;
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
		Node<V> node =  search(root, key, 0);
		if (node != null) return node.value;
		return null;
	}
	
	private Node<V> search(Node<V> node, String key, int index) {
        if (node == null) return null;
        
        char c = key.charAt(index);
        if (c < node.c) return search(node.left, key, index);
        else if (c > node.c) return search(node.right, key, index);
        else if (index < key.length() - 1) return search(node.middle, key, index + 1);
        else return node;
    }	
	
	@Override
	public void insert(String key, V value) {
		if (search(key) == null) {
			root = insert(root, key, value, 0);
		}
	}
	
	private Node<V> insert(Node<V> node, String key, V value, int index) {
        char c = key.charAt(index);
        if (node == null) {
            node = new Node<V>();
            node.c = c;
        }
        if (c < node.c) node.left = insert(node.left, key, value, index);
        else if (c > node.c) node.right = insert(node.right, key, value, index);
        else if (index < key.length() - 1) node.middle = insert(node.middle, key, value, index + 1);
        else node.value = value;
        return node;
    }	

	@Override
	public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new LinkedList<>();
        Node<V> node = search(root, prefix, 0);
        if (node == null) return queue;
        if (node.value != null) queue.add(prefix);
        collect(node.middle, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node<V> node, StringBuilder prefix, Queue<String> queue) {
        if (node == null) return;
        collect(node.left,  prefix, queue);
        if (node.value != null) queue.add(prefix.toString() + node.c);
        collect(node.middle, prefix.append(node.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(node.right, prefix, queue);
    }
    
    @Override
    public void delete(String key) {
    }
}
