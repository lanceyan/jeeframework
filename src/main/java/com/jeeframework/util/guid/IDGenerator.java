package com.jeeframework.util.guid;


/**
 * @author lance
 *  
 */
public class IDGenerator implements IDGenService {
	
	public String getID() {
		
		return DefaultIDGenerator.generateID();
	}
}
