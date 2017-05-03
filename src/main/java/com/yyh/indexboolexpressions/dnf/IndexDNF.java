/**
 * 
 */
package com.yyh.indexboolexpressions.dnf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yyh.indexboolexpressions.Conjunction;
import com.yyh.indexboolexpressions.InvIndex;
import com.yyh.indexboolexpressions.Pair;
import com.yyh.indexboolexpressions.utils.BEUtils;

/**
 * @author yyh
 *
 *布尔表达式索引的 DNF算法实现
 */
public class IndexDNF extends InvIndex<Conjunction> {

	public IndexDNF() {
		
	}
	
	public IndexDNF(Map<Conjunction,List<Integer>> revMap) {
		this.revMap.putAll(revMap);
	}
	
	private IndexConj conjIndex=new IndexConj();
	//将一个DNF 表达式加入到 revMap 中
	public void add(List<Conjunction> conjs,int curDocId) {
		for(int i=0;i<conjs.size();i++) {
			List<Integer> list=this.revMap.get(conjs.get(i));
			if( null==list ) {
				//conjIndex.add(conjs.get(i));
				////针对新加入的Conjunction 建立二级索引
				conjIndex.add(conjs.get(i));
			}
		}
		// 加入一级索引
		super.add(conjs,curDocId);
	}
	
	public void retrieveResult(List<Conjunction> query,
			Set<Integer> dnfIDs) {
		Set<Integer> conjID_set=new HashSet<Integer>();
		for(int i=0;i<query.size();i++) {
			conjIndex.retrieve(query.get(i).getAssignList(), conjID_set);
		}
		
		List<Conjunction> conj_List=new ArrayList<Conjunction>(conjID_set.size());
		for(Integer conjId:conjID_set) {
			if(null!=conjIndex.getConjIdMap().get(conjId)) {
				conj_List.add(conjIndex.getConjIdMap().get(conjId));
			}
		}
		
		super.retrieve(conj_List, dnfIDs);
		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		IndexDNF dnf=new IndexDNF();
		List<Conjunction> conjs=new ArrayList<Conjunction>();
		
		conjs.add(Conjunction.fromString("age:1:3&state:1:NY"));	
		conjs.add(Conjunction.fromString("age:1:3&gender:1:F"));
		dnf.add(conjs,1);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("age:1:3&gender:1:M&state:0:CA"));
		conjs.add(Conjunction.fromString("state:1:CA&gender:1:M"));
		dnf.add(conjs,2);
		conjs.clear();
		
		
		conjs.add(Conjunction.fromString("age:1:3|4"));
		conjs.add(Conjunction.fromString("state:0:CA|NY"));
		dnf.add(conjs,3);
		conjs.clear();
		
		System.out.println("revMap:");
		System.out.println(dnf.revMap.toString());
		System.out.println("conjIndex:");
		System.out.println(dnf.conjIndex.getIndex());
		//retrieveResult
		List<Conjunction> query=new ArrayList<Conjunction>();
		query.add(Conjunction.fromString("age:1:3&state:1:CA&gender:1:M"));//&state:1:CA&gender:1:M
		Set<Integer> dnfIDs = new HashSet<Integer>();
		dnf.retrieveResult(query, dnfIDs);
		System.out.println("conj id is "+dnfIDs);
				
				
				
		System.out.println("");
		System.out.println("new begin:");
		String revMapStr=BEUtils.objSerial2String(dnf.revMap);
		System.out.println("revMap new:");
		Map<Conjunction,List<Integer>> revMapNew=(Map<Conjunction,List<Integer>>)BEUtils.strDeserial2Obj(revMapStr);
		System.out.println(revMapNew);
		
		String conjIndexStr=BEUtils.objSerial2String(dnf.conjIndex.getIndex());
		Map<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>>
		indexNew=(Map<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>>)BEUtils.strDeserial2Obj(conjIndexStr);
		System.out.println("index new:");
		System.out.println(indexNew);
		
		String zeroConjListStr=BEUtils.objSerial2String(dnf.conjIndex.getZeroConjList());
		List<Pair<Integer,Boolean>> zeroConjListNew=
		(List<Pair<Integer,Boolean>>)BEUtils.strDeserial2Obj(zeroConjListStr);		
		String conjIdMapStr=BEUtils.objSerial2String(dnf.conjIndex.getConjIdMap());
		Map<Integer,Conjunction> conjIdMapNew=
		(Map<Integer,Conjunction>)BEUtils.strDeserial2Obj(conjIdMapStr);
		
		
		IndexDNF newIndexDNF=new IndexDNF(revMapNew);
		IndexConj indexConj=new IndexConj(indexNew,zeroConjListNew,conjIdMapNew);
		newIndexDNF.setConjIndex(indexConj);
		
		//retrieveResult
		List<Conjunction> queryNew=new ArrayList<Conjunction>();
		queryNew.add(Conjunction.fromString("age:1:4&state:1:CA&gender:1:M"));
		Set<Integer> dnfIDsNew = new HashSet<Integer>();
		newIndexDNF.retrieveResult(queryNew, dnfIDsNew);
		System.out.println(dnfIDsNew);
		//System.out.println(dnf.conjIndex);
	}

	public IndexConj getConjIndex() {
		return conjIndex;
	}
	public void setConjIndex(IndexConj conjIndex) {
		this.conjIndex = conjIndex;
	}
}
