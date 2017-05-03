/**
 * 
 */
package com.yyh.indexboolexpressions;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author yyh
 *
 */
public class Pair<T1,T2> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8069769949020125473L;
	private T1 firstType;
	private T2 secondType;
	public Pair() {
		
	}
	public Pair(T1 t1,T2 t2) {
		this.firstType = t1;
		this.secondType = t2;
	}
	public T1 getFirstType() {
		return firstType;
	}
	public void setFirstType(T1 firstType) {
		this.firstType = firstType;
	}
	public T2 getSecondType() {
		return secondType;
	}
	public void setSecondType(T2 secondType) {
		this.secondType = secondType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj.getClass()== this.getClass()) {
			Pair pair= (Pair) obj;
			//if(pair.getFirstType().equals(obj))
			if(pair.getFirstType()==null||this.getFirstType()==null
					||pair.getSecondType()==null || this.getSecondType()==null) {
				return false;
			} else {
			
				return pair.getFirstType().equals(this.getFirstType())
						&&pair.getSecondType().equals(this.secondType);
				}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.firstType).append('-')
				.append(this.secondType).toHashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(this.firstType).append(':').append(this.secondType);
		return sb.toString();
	}
	

}
