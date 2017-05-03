/**
 * 
 */
package com.yyh.indexboolexpressions;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author yyh
 * 单个赋值集 比如  age = 3 或者 state = CA
 * age = 3  new Assignment("age",true,"3");
 * state != CA new Assignment("state",false,"CA");
 */
public class Assignment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3736722132489514299L;
	public Assignment() {
		
	}
	public Assignment(String attribute,boolean belong,String value) {
		this.attribute=attribute;
		this.belong=belong;
		this.value=value;
	}
	private String attribute;
	private boolean belong;
	private String value;
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public boolean isBelong() {
		return belong;
	}
	public void setBelong(boolean belong) {
		this.belong = belong;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return this.attribute+(belong?" belongto ":" not belongto ")+this.value;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.attribute).append('-')
				.append(this.belong).append('-').append(this.value).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {

		if(obj!=null && obj.getClass()== this.getClass()) {
			Assignment assign= (Assignment) obj;
			//if(pair.getFirstType().equals(obj))
			if(this.attribute==null||this.value==null
					||assign.getAttribute()==null || assign.getValue()==null) {
				return false;
			} else {
			
				return assign.getAttribute().equals(this.attribute)
						&&assign.getValue().equals(this.value)
						&&assign.isBelong()==this.isBelong();
				}
		}
		return false;
	
	}
}
