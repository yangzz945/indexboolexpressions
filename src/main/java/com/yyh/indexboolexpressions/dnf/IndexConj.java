/**
 * 
 */
package com.yyh.indexboolexpressions.dnf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yyh.indexboolexpressions.Assignment;
import com.yyh.indexboolexpressions.Conjunction;
import com.yyh.indexboolexpressions.CurEntryLists;
import com.yyh.indexboolexpressions.Pair;

/**
 * @author yyh
 *
 */
public class IndexConj {
	private Map<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>> index = new HashMap<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>>();// 一维倒排索引结构的数据结构
	// 最外层的Map key: conjunction size
	// value:该conjunction size下的 Assignment 和 posting list的对应关系
	// 内层的map key: state属于CA
	// value: conjId true/false 的列表

	private int curConjId = 0;// 当前的conjunction id

	private int maxConjunctionSize = 0;// 最大的conjunction size

	private Map<Integer, Conjunction> conjIdMap = new HashMap<Integer, Conjunction>();// id
																						// 和
																						// Conjunction
																						// 的对应关系

	private List<Pair<Integer, Boolean>> zeroConjList = new ArrayList<Pair<Integer, Boolean>>();// 长度为0的conjunction对应的conjid
																								// 列表

	public IndexConj() {

	}

	public IndexConj(
			Map<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>> index,
			List<Pair<Integer, Boolean>> zeroConjList,
			Map<Integer, Conjunction> conjIdMap) {
		this.index = index;
		this.zeroConjList = zeroConjList;
		this.conjIdMap = conjIdMap;
		this.maxConjunctionSize = this.getMaxConjunctionSize(index);
	}

	/**
	 * @param index
	 * @return conjunction 的最大size
	 */
	private int getMaxConjunctionSize(
			Map<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>> index) {
		int maxConjSize = 0;
		for (Integer conjSize : index.keySet()) {
			maxConjSize = conjSize > maxConjSize ? conjSize : maxConjSize;
		}
		return maxConjSize;
	}

	// posting list 排序用到的比较器
	private Comparator<CurEntryLists> cmp = new Comparator<CurEntryLists>() {
		@Override
		public int compare(CurEntryLists o1, CurEntryLists o2) {
			Integer o1ConjId = o1.getCurrentEntryID();// Integer.valueOf(o1.get(0).getFirstType().getAssignList().get(0).getValue());
			Integer o2ConjId = o2.getCurrentEntryID();// Integer.valueOf(o2.get(0).getFirstType().getAssignList().get(0).getValue());
			if (o1ConjId > o2ConjId) {
				return 1;
			} else if (o1ConjId.intValue() == o2ConjId.intValue()) { // conjunction
																		// id相等的情况下，
																		// 不属于的值要小
				if (o1.getCurrentEntry() != -1 && o1.getCurrentEntry() != -1) {
					boolean o1Belong = o1.getList().get(o1.getCurrentEntry())
							.getSecondType();// o1.get(0).getFirstType().getAssignList().get(0).isBelong();
					boolean o2Belong = o2.getList().get(o2.getCurrentEntry())
							.getSecondType();// o2.get(0).getFirstType().getAssignList().get(0).isBelong();
					if (!o1Belong && o2Belong) {
						return -1;
					} else if (o1Belong && !o2Belong) {
						return 1;
					} else {
						return 0;
					}
				} else {
					return 0;
				}

			} else {
				return -1;
			}
		}
	};

	// 返回conjunction的size，计算 属于的 个数即可
	private int getSize(Conjunction conj) {
		int size = 0;
		for (Assignment ass : conj.getAssignList()) {
			if (ass.isBelong()) {
				size++;
			}
		}
		return size;
	}

	// posting list 初始化
	private void initPostingLists(List<CurEntryLists> PLists,
			List<Assignment> assigns,
			Map<Pair<String, String>, List<Pair<Integer, Boolean>>> map) {

		if (null == map) {
			return;
		}
		List<String> assignStr = new ArrayList<String>(assigns.size());
		StringBuilder sb = new StringBuilder();
		for (Assignment ass : assigns) {
			if (!ass.getValue().contains("|")) {
				sb.append(ass.getAttribute()).append('-')
						.append(ass.getValue());
				assignStr.add(sb.toString());
				sb.setLength(0);
			} else {
				for (String value : ass.getValue().split("\\|")) {
					sb.append(ass.getAttribute()).append('-').append(value);
					assignStr.add(sb.toString());
					sb.setLength(0);
				}
			}
		}
		for (Map.Entry<Pair<String, String>, List<Pair<Integer, Boolean>>> entry : map
				.entrySet()) {

			/*
			 * String secondType=entry.getKey().getSecondType();
			 * if(secondType.contains("|")) {
			 * secondType=secondType.split("|")[0]; }
			 */

			sb.append(entry.getKey().getFirstType()).append('-')
					.append(entry.getKey().getSecondType());
			if (assignStr.contains(sb.toString())) {
				PLists.add(new CurEntryLists(entry.getValue(), 0, entry
						.getKey()));
			}
			sb.setLength(0);
		}

	}

	private void sortByCurrentEntries(List<CurEntryLists> PLists) {
		Collections.sort(PLists, cmp);
	}

	/**
	 * 检索的核心方法
	 * 
	 * @param assigns
	 * @param conjs
	 */
	public void retrieve(List<Assignment> assigns, Set<Integer> conjs) {

		Set<String> stastifiedAttributeSet = new HashSet<String>();
		List<CurEntryLists> srcPLists = new ArrayList<CurEntryLists>();
		// K 值取 assigns和maxConjunctionSize 的最小值即可。
		// 因为 sizeof(assigns) < sizeof(Conjunction) 时，该Conjunction一定不满足该次请求
		for (int K = Math.min(this.maxConjunctionSize, assigns.size()); K >= 0; K--) {

			stastifiedAttributeSet.clear();
			srcPLists.clear();

			int oldK = K;
			if (K > assigns.size()) {
				break;
			}

			// 初始化满足 assigns 的 posting list
			initPostingLists(srcPLists, assigns, index.get(K));

			if (K == 0) {
				K = 1;
				// K=0时，增加 zeroConjList
				srcPLists.add(new CurEntryLists(zeroConjList, 0, new Pair("Z",
						0)));
			}

			// 该条件满足的时候，没有 conjunction 能够满足，继续下次循环
			if (srcPLists.size() < K) {
				continue;
			}

			sortByCurrentEntries(srcPLists);
			List<CurEntryLists> destPLists = new ArrayList<CurEntryLists>(
					srcPLists.size());// srcPLists 的拷贝
			for (int KK = K - 1; KK < srcPLists.size(); KK++) {
				// initPostingLists(PLists,assigns,index.get(K));
				destPLists.clear();

				// Collections.copy(destPLists, srcPLists);
				for (CurEntryLists curList : srcPLists) {
					try {
						destPLists.add((CurEntryLists) curList.clone());
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// System.out.println(destPLists);
				while (destPLists.get(KK).getCurrentEntry() != -1) {
					// sortByCurrentEntries(PLists);
					int nextID = destPLists.get(KK).getCurrentEntryID();
					if (destPLists.get(0).getCurrentEntryID() == destPLists
							.get(KK).getCurrentEntryID()) {
						int currentID = destPLists.get(0).getCurrentEntryID();
						int currentEntry = destPLists.get(0).getCurrentEntry();
						if (destPLists.get(0).getList().get(currentEntry)
								.getSecondType() == false) {
							int rejectID = currentID;
							for (int L = K; L < srcPLists.size(); L++) {
								if (destPLists.get(L).getCurrentEntryID() != rejectID) {
									break;
								} else {
									// skip to rejectID+1
									skipTo(destPLists, rejectID + 1, L);
								}
							}
						} else {
							// 判断属性是否都满足，都满足的时候，才加入到 conjs 集合中
							// 属性数量不满足的时候，不加入到 conjs 集合中
							for (int j = 0; j <= KK; j++) {
								stastifiedAttributeSet.add(destPLists.get(j)
										.getKey().getFirstType());
							}

							if (stastifiedAttributeSet.size() == K) {
								conjs.add(destPLists.get(KK)
										.getCurrentEntryID());
							}
							stastifiedAttributeSet.clear();
						}

						nextID = destPLists.get(KK).getCurrentEntryID() + 1;
					} else { // 忽略开始的 K-1 个 posting list
						nextID = destPLists.get(KK).getCurrentEntryID();
					}

					for (int i = 0; i < K; i++) {
						// skip to positions[K-1]
						skipTo(destPLists, nextID, i);
					}

					sortByCurrentEntries(destPLists);

				}
			}
			// 恢复外层的 K 值
			K = oldK;
		}
	}

	/**
	 * @param PLists
	 * @param skipToID  skip 到该id
	 * @param listIdx
	 * 
	 * 把PLists[listIdx] 中的 CurEntryLists，skip 到 skipToID
	 */
	private void skipTo(List<CurEntryLists> PLists, int skipToID, int listIdx) {
		CurEntryLists cur = PLists.get(listIdx);
		int entrySize = cur.getList().size();
		if (skipToID > cur.getList().get(entrySize - 1).getFirstType()) {
			cur.setCurrentEntry(-1);
			cur.setCurrentEntryID(Integer.MAX_VALUE);
		} else {

			for (int i = cur.getCurrentEntry(); i < cur.getList().size(); i++) {
				if (cur.getList().get(i).getFirstType() >= skipToID) {
					cur.setCurrentEntry(i);
					cur.setCurrentEntryID(cur.getList().get(i).getFirstType());
					break;
				}
			}

		}

	}

	/**
	 * @param conj  
	 * 增加 Conjunction ，非线程安全的
	 */
	public void add(Conjunction conj) {
		curConjId++;
		conjIdMap.put(curConjId, conj);
		int K = getSize(conj);
		this.maxConjunctionSize = K > maxConjunctionSize ? K
				: maxConjunctionSize;
		if (K == 0) {
			zeroConjList.add(new Pair(curConjId, true));
		}

		for (int i = 0; i != conj.getSize(); i++) {
			Map<Pair<String, String>, List<Pair<Integer, Boolean>>> map = index
					.get(K);
			if (null == map) {
				map = new HashMap<Pair<String, String>, List<Pair<Integer, Boolean>>>();
				index.put(K, map);
			}

			String[] attributeValues = conj.getAssignList().get(i).getValue()
					.split("\\|");
			for (String attValue : attributeValues) {
				Pair pair = new Pair(
						conj.getAssignList().get(i).getAttribute(), attValue);
				List<Pair<Integer, Boolean>> list = map.get(pair);
				if (null == list) {
					list = new ArrayList<Pair<Integer, Boolean>>();
					map.put(pair, list);
				}

				list.add(new Pair(curConjId, conj.getAssignList().get(i)
						.isBelong()));
			}

		}
	}

	public List<Pair<Integer, Boolean>> getZeroConjList() {
		if (zeroConjList.size() > 0) {
			return zeroConjList;
		} else {
			// zeroConjList
			synchronized (this) {
				if (zeroConjList.size() == 0) {
					zeroConjList.add(new Pair(this.curConjId + 1, true));
				}
			}
			return zeroConjList;
		}
	}

	public Map<Integer, Map<Pair<String, String>, List<Pair<Integer, Boolean>>>> getIndex() {
		return index;
	}

	public Map<Integer, Conjunction> getConjIdMap() {
		return conjIdMap;
	}
}
