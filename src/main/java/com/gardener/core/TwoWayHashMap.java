package com.gardener.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TwoWayHashMap<K, V>{
	private Map<K, V> keyValueMap;
	private Map<V, K> valueKeyMap;
	
	public TwoWayHashMap() {
		this(16);
	}
	
	public TwoWayHashMap(Integer initSize) {
		keyValueMap = new ConcurrentHashMap<>(initSize);
		valueKeyMap = new ConcurrentHashMap<>(initSize);
	}
	
	public int size() {
		return keyValueMap.size();
	}
	
	public boolean isEmpty() {
		return keyValueMap.isEmpty();
	}
	
	public void clear() {
		keyValueMap.clear();
		valueKeyMap.clear();
	}
	
	public void put(K k, V v) {
		keyValueMap.put(k, v);
		valueKeyMap.put(v, k);
	}
	
	public V get(K k) {
		return keyValueMap.get(k);
	}
	
	public K inverseGet(V v) {
		return valueKeyMap.get(v);
	}
	
	public void remove(K k) {
		V v = keyValueMap.get(k);
		keyValueMap.remove(k);
		valueKeyMap.remove(v);
	}
	
	public void inverseRemove(V v) {
		K k = valueKeyMap.get(v);
		keyValueMap.remove(k);
		valueKeyMap.remove(v);
	}
}
