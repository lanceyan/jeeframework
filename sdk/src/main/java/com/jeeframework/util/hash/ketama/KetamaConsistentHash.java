package com.jeeframework.util.hash.ketama;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public final class KetamaConsistentHash {
	
	private static KetamaConsistentHash ketamaConsistentHash = null;
	
	private TreeMap<Long, String> ketamaNodes;
	private Map<String, Long> nodeHashMap;
	
	private HashAlgorithm hashAlg = HashAlgorithm.KETAMA_HASH;
	
	private int numReps = 160;
	
	public final static int DEFAULT_REP_NUM = 160;
	
	public static KetamaConsistentHash getInstance(List<String> nodes) {
		if(ketamaConsistentHash == null) {
			ketamaConsistentHash = new KetamaConsistentHash(nodes);
		}
		
		return ketamaConsistentHash;
	}
	
//	public static KetamaConsistentHash getInstance(Map<Long, String> nodeMap) {
//		if(ketamaConsistentHash == null) {
//			ketamaConsistentHash = new KetamaConsistentHash(nodeMap);
//		}
//		
//		return ketamaConsistentHash;
//	}
	
	private KetamaConsistentHash(List<String> nodes) {
		hashAlg = HashAlgorithm.KETAMA_HASH;
		ketamaNodes = new TreeMap<Long, String>();
		nodeHashMap = new HashMap<String, Long>();
		
        numReps = DEFAULT_REP_NUM;
        
        initKetamaNodes(nodes);
//		for (String node : nodes) {
//			for (int i = 0; i < numReps / 4; i++) {
//				byte[] digest = hashAlg.computeMd5(node + i);
//				for(int h = 0; h < 4; h++) {
//					long m = hashAlg.hash(digest, h);
//					
//					ketamaNodes.put(m, node);
//				}
//			}
//		}
    }
	
	private synchronized void initKetamaNodes(List<String> nodes) {
		for (String node : nodes) {
			boolean isFirst = true;
			for (int i = 0; i < numReps / 4; i++) {
				byte[] digest = hashAlg.computeMd5(node + i);
				for(int h = 0; h < 4; h++) {
					long m = hashAlg.hash(digest, h);
					
					if(isFirst) {
						nodeHashMap.put(node, m);
						isFirst = false;
					}
					
					ketamaNodes.put(m, node);
				}
			}
		}
	}
	
//	private KetamaConsistentHash(Map<Long, String> nodeMap) {
//		for(Long hash : nodeMap.keySet()) {
//			String nodeName = nodeMap.get(hash);
//			ketamaNodes.put(hash, nodeName);
//		}
//    }
//	
//	private KetamaConsistentHash(List<String> nodes, HashAlgorithm alg) {
//		hashAlg = alg;
//		ketamaNodes = new TreeMap<Long, String>();
//		
//        numReps = DEFAULT_REP_NUM;
//        
//		for (String node : nodes) {
//			for (int i = 0; i < numReps / 4; i++) {
//				byte[] digest = hashAlg.computeMd5(node + i);
//				for(int h = 0; h < 4; h++) {
//					long m = hashAlg.hash(digest, h);
//					
//					ketamaNodes.put(m, node);
//				}
//			}
//		}
//    }
	
	private KetamaConsistentHash(List<String> nodes, HashAlgorithm alg, int nodeCopies) {
		hashAlg = alg;
		ketamaNodes = new TreeMap<Long, String>();
		
        numReps = nodeCopies;
        
        initKetamaNodes(nodes);
//		for (String node : nodes) {
//			for (int i = 0; i < numReps / 4; i++) {
//				byte[] digest = hashAlg.computeMd5(node + i);
//				for(int h = 0; h < 4; h++) {
//					long m = hashAlg.hash(digest, h);
//					
//					ketamaNodes.put(m, node);
//				}
//			}
//		}
    }

	public String getPrimary(final String k) {
		byte[] digest = hashAlg.computeMd5(k);
		String rv = getNodeForKey(hashAlg.hash(digest, 0));
		return rv;
	}

	public String getNodeForKey(long hash) {
		final String rv;
		Long key = hash;
		
		if(!ketamaNodes.containsKey(key)) {
			SortedMap<Long, String> tailMap = ketamaNodes.tailMap(key);
			if(tailMap.isEmpty()) {
				key = ketamaNodes.firstKey();
			} else {
				key = tailMap.firstKey();
			}
			
			//For JDK1.6 version
//			key = ketamaNodes.ceilingKey(key);
//			if (key == null) {
//				key = ketamaNodes.firstKey();
//			}
		}
		
		rv = ketamaNodes.get(key);
		return rv;
	}

	public TreeMap<Long, String> getKetamaNodes() {
		return ketamaNodes;
	}

	public void setKetamaNodes(TreeMap<Long, String> ketamaNodes) {
		this.ketamaNodes = ketamaNodes;
	}

	public Map<String, Long> getNodeHashMap() {
		return nodeHashMap;
	}

	public void setNodeHashMap(Map<String, Long> nodeHashMap) {
		this.nodeHashMap = nodeHashMap;
	}
	
}
