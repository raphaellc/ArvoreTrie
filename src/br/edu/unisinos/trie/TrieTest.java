package br.edu.unisinos.trie;

public class TrieTest {

	public static void main(String[] args) {
		WayTrie<String> trie = new WayTrie<>();
		trie.insert("Gusta", "Nada Pode ser Maior");
		trie.insert("Gustavo", "Maior Rede de Lojas de Clubes do Brasil");
		
		System.out.println(trie.countKeysWithPrefix("Gust"));
//		System.out.println(trie.longestPrefixOf("Gusta"));
	}

}
