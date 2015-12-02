package com.jeeframework.util.hash.ketama;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<T> {
	
	private final HashAlgorithm hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

	public ConsistentHash(HashAlgorithm hashFunction, int numberOfReplicas, Collection<T> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (T node : nodes) {
			add(node);
		}
	}

	public void add(T node) {
		for (int i = 0; i < numberOfReplicas / 4; i++) {
			
			byte[] digest = hashFunction.computeMd5(node.toString() + i);
			for(int h = 0; h < 4; h++) {
				long m = hashFunction.hash(digest, h);
				circle.put(m, node);
			}
			
		}
	}

	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas / 4; i++) {
			
			byte[] digest = hashFunction.computeMd5(node.toString() + i);
			for(int h = 0; h < 4; h++) {
				long m = hashFunction.hash(digest, h);
				circle.remove(m);
			}
			
		}
	}
	
	public T getPrime(String key) {
		return get(key, 0);
	}

	public T get(String key, int index) {
		if (circle.isEmpty()) {
			return null;
		}
		
		byte[] digest = hashFunction.computeMd5(key);
		
		T t = getNodeForKey(hashFunction.hash(digest, index));
		
		return t;
		
		/*int hash = hashFunction.hash(key);
		
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		
		return circle.get(hash);*/
	}
	
	public T getNodeForKey(long hash) {
		final T t;
		
		Long key = hash;
		if(!circle.containsKey(key)) {
			SortedMap<Long, T> tailMap = circle.tailMap(key);
			if(tailMap.isEmpty()) {
				key = circle.firstKey();
			} else {
				key = tailMap.firstKey();
			}
		}
		
		t = circle.get(key);
		return t;
	}
	
	public static void main(String[] args) {
		List<String> nodes = new ArrayList<String>();
		nodes.add("34");
		nodes.add("35");
		nodes.add("89");
		
		ConsistentHash consistentHash = new ConsistentHash(HashAlgorithm.KETAMA_HASH, 4, nodes);
		
		System.out.println(consistentHash.circle);
		
		
	}

}
