package com.jeeframework.util.io.bs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.Set;

/**
 * ����洢ͬһ��key,�洢���ֵ�� map
 * 
 * @author anderszhou
 */
public class MultiMap<K, V> {
	private int						count;
	private HashMap<K, Vector<V>>	map	= new HashMap<K, Vector<V>>();

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(K key, V value) {
		Vector<V> v = map.get(key);

		if (v == null) {
			v = new Vector<V>();
			map.put(key, v);
		}

		count++;
		v.add(value);

		return value;
	}

	public int size() {
		return map.size();
	}

	public Set<java.util.Map.Entry<K, Vector<V>>> entrySet() {
		return map.entrySet();
	}

	public Vector<V> get(Object key) {
		return map.get(key);
	}

	public Vector<V> remove(Object key) {
		Vector<V> ret = map.remove(key);
		count -= ret.size();
		return ret;
	}

	public Collection<Vector<V>> values() {
		return map.values();
	}

	public int totalSize() {
		return count;
	}
}
