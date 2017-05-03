package com.yyh.indexboolexpressions;

import java.util.ArrayList;
import java.util.List;

public class CurEntryLists  {
	
	private Pair<String,String> key;// 属性的Pair对象，比如 Pari<age,3> 
	
	private List<Pair<Integer,Boolean>> list; // key 对应的conjunction id 列表
	private int currentEntry;//List 中的当前Entry id
	private int currentEntryID;//对应的 Conjunction ID
	
	public CurEntryLists(List<Pair<Integer,Boolean>> list,
			int currentEntry,
			Pair<String,String> key) {
		this.list=list;
		this.currentEntry=currentEntry;
		this.currentEntryID=list.get(currentEntry).getFirstType();//currentEntryID;
		this.key=key;
	}
	public CurEntryLists() {
		
	}

	public List<Pair<Integer, Boolean>> getList() {
		return list;
	}
	public void setList(List<Pair<Integer, Boolean>> list) {
		this.list = list;
	}
	public int getCurrentEntry() {
		return currentEntry;
	}
	public void setCurrentEntry(int currentEntry) {
		this.currentEntry = currentEntry;
	}
	public int getCurrentEntryID() {
		return currentEntryID;
	}
	public void setCurrentEntryID(int currentEntryID) {
		this.currentEntryID = currentEntryID;
	}
	public Pair<String, String> getKey() {
		return key;
	}
	public void setKey(Pair<String, String> key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(this.key).append(':').append(this.currentEntryID)
		.append(this.list.toString());
		return sb.toString();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		CurEntryLists clo=new CurEntryLists();
		clo.setCurrentEntry(this.currentEntry);
		clo.setCurrentEntryID(this.currentEntryID);
		clo.setKey(new Pair(this.key.getFirstType(),this.key.getSecondType()));
		List<Pair<Integer,Boolean>> cloList=new ArrayList<Pair<Integer,Boolean>>();
		for(Pair<Integer,Boolean> pair:this.list) {
			cloList.add(pair);
		}
		clo.setList(cloList);
		return clo;
		
		
	} 
	

}
