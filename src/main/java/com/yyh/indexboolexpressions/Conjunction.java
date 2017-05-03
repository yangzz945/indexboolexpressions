/**
 * 
 */
package com.yyh.indexboolexpressions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author yyh
 *
 * Assignment 的集合
 * age 属于 3 ^ state 属于 CA
 */
public class Conjunction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4868515499286366443L;
	private List<Assignment> assignList;
	//private int size;
	public List<Assignment> getAssignList() {
		return assignList;
	}
	public void setAssignList(List<Assignment> assignList) {
		this.assignList = assignList;
	}
	public int getSize() {
		return (null==assignList)?0:assignList.size();
	}
	@Override
	public boolean equals(Object o) {
		if (o == this)
		    return true;
		if (o.getClass()!=this.getClass())
		    return false;

		ListIterator<Assignment> e1 = assignList.listIterator();
		ListIterator e2 = ((Conjunction)o).getAssignList().listIterator();
		while(e1.hasNext() && e2.hasNext()) {
			Assignment o1 = e1.next();
		    Object o2 = e2.next();
		    if (!(o1==null ? o2==null : o1.equals(o2))){
		    	return false;
		    }
		}
		return !(e1.hasNext() || e2.hasNext());
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.assignList.toString()).toHashCode();
	}
	@Override
	public String toString() {
		return assignList.toString();
	}
	
	public static Conjunction fromString(String assignStrs) {
		Conjunction c1=new Conjunction();
		List<Assignment> assignList=new ArrayList<Assignment>();
		for(String assignStr:assignStrs.split("&")) {
			String[] strs=assignStr.split(":");
			assignList.add(new Assignment(strs[0],"1".equals(strs[1]),strs[2]));
		//assignList.add(new Assignment("state",true,"NY"));
		}
 		c1.setAssignList(assignList);
 		return c1;
	}
}
