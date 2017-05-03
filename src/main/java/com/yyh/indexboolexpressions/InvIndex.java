/**
 * 
 */
package com.yyh.indexboolexpressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yyh
 * 
 * 
 */
public class InvIndex<K> // extends HashMap<K,V> 
{
	protected Map<K,List<Integer>> revMap = new HashMap<K,List<Integer>>();//conjunction 到docId的倒排
	protected List<List<K>> docs=new ArrayList<List<K>>();//文档正排表
	/**
	 * @param doc       conjunction的组合
	 * @param curdocID  当前文档id
	 */
	public void add(List<K> doc,int curdocID) {
		docs.add(doc);
		//int curDocID=docs.size()-1;
		for(int w=0;w<doc.size();w++) {
			List<Integer> value=revMap.get(doc.get(w));
			if(null == value) {
				value = new ArrayList<Integer>();
				revMap.put(doc.get(w), value);
			}
			value.add(curdocID);
		}
	}
	
	/**
	 * @param query 查询的条件
	 * @param docIDs 返回的符合条件的docId集合
	 */
	public void retrieve(List<K> query,Set<Integer> docIDs) {
		int termNum = query.size();
		
		docIDs.clear();
		for(int t=0;t<termNum;t++) {
			List<Integer> value=revMap.get(query.get(t));
			if(null!=value) {
				docIDs.addAll(value);
			}
		}
		
	}

	public Map<K, List<Integer>> getRevMap() {
		return revMap;
	}
}
