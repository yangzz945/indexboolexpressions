package com.yyh.indexboolexpressions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yyh.indexboolexpressions.dnf.IndexDNF;

/**
 * IndexDNFExample!
 *
 */
public class IndexDNFExample 
{
	
	/**
	 * d1=(age:1:3&state:1:NY)
	 * d2=(age:1:3&gender:1:F)
	 * d3=(age:1:3&gender:1:M&state:0:CA)
	 * d4=(state:1:CA&gender:1:M)
	 * d5=(age:1:3|4)
	 * d6=(state:0:CA|NY)
	 * 
	 * s=(age:1:3&state:1:CA&gender:1:M)  查询条件s中，每个属性都是单值的情况
	 */
	public static void example1() {
		IndexDNF dnf=new IndexDNF();
		List<Conjunction> conjs=new ArrayList<Conjunction>();
		
		conjs.add(Conjunction.fromString("age:1:3&state:1:NY"));	
		dnf.add(conjs,1);
		conjs.clear();
			
		conjs.add(Conjunction.fromString("age:1:3&gender:1:F"));
		dnf.add(conjs,2);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("age:1:3&gender:1:M&state:0:CA"));
		dnf.add(conjs,3);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("state:1:CA&gender:1:M"));
		dnf.add(conjs,4);
		conjs.clear();
		
		
		conjs.add(Conjunction.fromString("age:1:3|4"));
		dnf.add(conjs,5);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("state:0:CA|NY"));
		dnf.add(conjs,6);
		conjs.clear();
		
		System.out.println("revMap:");
		System.out.println(dnf.revMap.toString());
		System.out.println("conjIndex:");
		System.out.println(dnf.getConjIndex().getIndex());
		//retrieveResult
		List<Conjunction> query=new ArrayList<Conjunction>();
		query.add(Conjunction.fromString("age:1:3&state:1:CA&gender:1:M"));//&state:1:CA&gender:1:M
		Set<Integer> dnfIDs = new HashSet<Integer>();
		dnf.retrieveResult(query, dnfIDs);
		System.out.println("conj id is "+dnfIDs);
	}
	
	
	/**
	 * d1=(age:1:3&state:1:NY)
	 * d2=(age:1:3&gender:1:F)
	 * d3=(age:1:3&gender:1:M&state:0:CA)
	 * d4=(state:1:CA&gender:1:M)
	 * d5=(age:1:3|4)
	 * d6=(state:0:CA|NY)
	 * 
	 * s=(age:1:3|4&state:1:CA&gender:1:M)  查询条件s中，属性出现多值的情况，age 属于 3或者4
	 */
	public static void example2() {
		IndexDNF dnf=new IndexDNF();
		List<Conjunction> conjs=new ArrayList<Conjunction>();
		
		conjs.add(Conjunction.fromString("age:1:3&state:1:NY"));	
		dnf.add(conjs,1);
		conjs.clear();
			
		conjs.add(Conjunction.fromString("age:1:3&gender:1:F"));
		dnf.add(conjs,2);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("age:1:3&gender:1:M&state:0:CA"));
		dnf.add(conjs,3);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("state:1:CA&gender:1:M"));
		dnf.add(conjs,4);
		conjs.clear();
		
		
		conjs.add(Conjunction.fromString("age:1:3|4"));
		dnf.add(conjs,5);
		conjs.clear();
		
		conjs.add(Conjunction.fromString("state:0:CA|NY"));
		dnf.add(conjs,6);
		conjs.clear();
		
		System.out.println("revMap:");
		System.out.println(dnf.revMap.toString());
		System.out.println("conjIndex:");
		System.out.println(dnf.getConjIndex().getIndex());
		//retrieveResult
		List<Conjunction> query=new ArrayList<Conjunction>();
		query.add(Conjunction.fromString("age:1:3&state:1:CA&gender:1:M|F"));//&state:1:CA&gender:1:M
		Set<Integer> dnfIDs = new HashSet<Integer>();
		dnf.retrieveResult(query, dnfIDs);
		System.out.println("conj id is "+dnfIDs);
	}
	
	
    public static void main( String[] args ) {
    	// query 查询条件中，值都为单值的情况
    	//IndexDNFExample.example1();
    	// query 查询条件中，值存在多值的情况 比如 gender 属于 F或者M
    	IndexDNFExample.example2();
    }
}
