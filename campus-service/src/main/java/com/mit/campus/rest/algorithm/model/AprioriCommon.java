package com.mit.campus.rest.algorithm.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mit.campus.rest.algorithm.AprioriRuleModel;



/**
 * 
* 关联算法的通用模型
* @author shuyy
* @date 2018年9月10日
 */
public class AprioriCommon {

	// 迭代次数
	public static int times = 0;
	// 循环状态，迭代标识
	private static boolean endTag = false;
	// 数据集
	static List<List<String>> record = null;
	// 存储所有的频繁项集
	static List<List<String>> frequentItemset = null;
	// 存放频繁项集和对应的支持度计数
	static List<MymapCom> map = null;
	//最小支持度百分比
	private static  double MIN_SUPPROT;
	//最小置信度
    private static  double MIN_CONFIDENCE;
      
    /**
     * 
    * @param @param minSupport 输入最小支持度
    * @param @param minConfidence 输入最小置信度
    * @param @param rc 离散数据集合
    * @param @param rank 要得到满足要求（支持度和置信度满足）输出等级
    * @param @return 关联规则
    * @return List<AprioriRuleModel>
    * @throws
    * @author shuyy
     */
    public static List<AprioriRuleModel> getRules(double minSupport, double minConfidence,
												  List<List<String>> rc, String rank) {
    	MIN_SUPPROT = minSupport;
	    MIN_CONFIDENCE = minConfidence;
	    
	    /******注意：new对象，否则数据叠加*****/
	  //频繁项集及支持度计数
	    map = new ArrayList<MymapCom>();
	 // 数据集
	    record = new ArrayList<List<String>>();
	 // 存储所有的频繁项集
		frequentItemset = new ArrayList<>();
		/*******************************/
		//置换数据集
		record = rc; 
		// 调用Apriori算法获得频繁项集
		Apriori();
		System.out.println("频繁模式挖掘完毕，进行关联度挖掘，最小支持度百分比为："+minSupport+"  最小置信度为："+minConfidence);
		//挖掘关联规则
		List<AprioriRuleModel> la = AssociationRulesMining(rank);
		return la;
    }
    
    /**
     * 
    * 调用Apriori算法获得频繁项集
    * @param 
    * @return void
    * @throws
    * @author shuyy
     */
    private static void Apriori() {
    	/***********求频繁项集开始*******************/
		System.out.println("第一次扫描后的1级候选集");
		List<List<String>> CandidateItemset = findFirstCandidate();
		ShowData(CandidateItemset);
		System.out.println("第一次扫描后的1级频繁集");
		List<List<String>> FrequentItemset = getSupprotedItemset(CandidateItemset);
		// 添加到所有的频繁项集中
		AddToFrequenceItem(FrequentItemset);
		// 控制台输出1项频繁集
		ShowData(FrequentItemset);

		// *****************************迭代过程**********************************
		times = 2;
		while (endTag != true) {
			System.out.println("*******************************第" + times + "次扫描后备选集");
			// **********连接操作****获取候选times项集**************
			List<List<String>> nextCandidateItemset = getNextCandidate(FrequentItemset);
			// 输出所有的候选项集
			ShowData(nextCandidateItemset);

			/************** 计数操作***由候选k项集选择出频繁k项集 ****************/
			System.out.println("*******************************第" + times + "次扫描后频繁集");
			List<List<String>> nextFrequentItemset = getSupprotedItemset(nextCandidateItemset);
			AddToFrequenceItem(nextFrequentItemset);// 添加到所有的频繁项集中
			// 输出所有的频繁项集
			ShowData(nextFrequentItemset);

			// *********如果循环结束，输出最大模式**************
			if (endTag == true) {
				System.out.println("Apriori算法--->最大频繁集：");
				ShowData(FrequentItemset);
			}
			// ****************下一次循环初值********************
			FrequentItemset = nextFrequentItemset;
			times++;// 迭代次数加一
		}
    }
    
    /**
     * 
    *  获得一项候选集
    * @param @return
    * @return List<List<String>>
    * @throws
    * @author shuyy
     */
	private static List<List<String>> findFirstCandidate() {
		List<List<String>> tableList = new ArrayList<List<String>>();
		// 新建一个hash表，存放所有的不同的一维数据
		HashSet<String> hs = new HashSet<String>();
		// 遍历所有的数据集，找出所有的不同的商品存放到hs中
		for (int i = 0; i < record.size(); i++) { 
			for (int j = 0; j < record.get(i).size(); j++) {
				hs.add(record.get(i).get(j));
			}
		}
		// hs代表所有的离散值域，A1,A2,B1,B2,B3,C1,C2,C3,D1,D2,D3,E1,E2,F1,F2,G1,G2,H1,H2,H3,I1,I2,J1,J2,J3,J4,K1,K2
		Iterator<String> itr = hs.iterator();
		while (itr.hasNext()) {
			List<String> tempList = new ArrayList<String>();
			String Item = (String) itr.next();
			// 将每一种商品存放到一个List<String>中
			tempList.add(Item); 
			// 所有的list<String>存放到一个大的list中
			tableList.add(tempList);
		}
		// 返回所有的商品
		return tableList;
	}
	
	/**
	 * 
	* 显示出candidateitem中的所有的项集
	* @param @param itemset
	* @return void
	* @throws
	* @author shuyy
	 */
	public static void ShowData(List<List<String>> itemset) {
		for (int i = 0; i < itemset.size(); i++) {
			List<String> list = new ArrayList<String>(itemset.get(i));
			for (int j = 0; j < list.size(); j++) {
				System.out.print(list.get(j) + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * 
	* 
	* 由k项候选集剪枝得到k项频繁集
	* @param @param CandidateItemset
	* @param @return
	* @return List<List<String>>
	* @throws
	* @author shuyy
	 */
	private static List<List<String>> getSupprotedItemset(List<List<String>> CandidateItemset) { 
		// 对所有的商品进行支持度计数
		boolean end = true;
		List<List<String>> supportedItemset = new ArrayList<List<String>>();
		for (int i = 0; i < CandidateItemset.size(); i++) {
			// 统计记录数
			int count = countFrequent1(CandidateItemset.get(i));
			if (count >= MIN_SUPPROT * (record.size())) {
				supportedItemset.add(CandidateItemset.get(i));
				// 存储当前频繁项集以及它的支持度计数
				map.add(new MymapCom(CandidateItemset.get(i), count));
				end = false;
			}
		}
		// 存在频繁项集则不会结束
		endTag = end;
		if (endTag == true) {
			System.out.println("*****************无满足支持度的" + times + "项集,结束连接");
		}
		return supportedItemset;
	}
	
	/**
	 * 
	* 统计record中出现list集合的个数
	* @param @param list
	* @param @return
	* @return int
	* @throws
	* @author shuyy
	 */
	private static int countFrequent1(List<String> list) {
		// 遍历所有数据集record，对单个候选集进行支持度计数
		int count = 0;
		// 从record的第一个开始遍历
		for (int i = 0; i < record.size(); i++){
			boolean flag = true;
			// 如果record中的第一个数据集包含list中的所有元素
			for (int j = 0; j < list.size(); j++){
				String t = list.get(j);
				if (!record.get(i).contains(t)) {
					flag = false;
					break;
				}
			}
			if (flag){
				// 支持度加一
				count++;
			}				
		}
		// 返回支持度计数
		return count;
	}
	
	/**、
	 * 
	* 添加到所有项目集中
	* @param @param fre
	* @param @return
	* @return boolean
	* @throws
	* @author shuyy
	 */
	public static boolean AddToFrequenceItem(List<List<String>> fre) {
		for (int i = 0; i < fre.size(); i++) {
			frequentItemset.add(fre.get(i));
		}
		return true;
	}
	
	/**
	 * 
	* 
	* 有当前频繁项集自连接求下一次候选集
	* @param @param FrequentItemset
	* @param @return
	* @return List<List<String>>
	* @throws
	* @author shuyy
	 */
	@SuppressWarnings("unchecked")
	private static List<List<String>> getNextCandidate(List<List<String>> FrequentItemset) {
		List<List<String>> nextCandidateItemset = new ArrayList<List<String>>();
		for (int i = 0; i < FrequentItemset.size(); i++) {
			HashSet<String> hsSet = new HashSet<String>();
			HashSet<String> hsSettemp = new HashSet<String>();
			// 获得频繁集第i行
			for (int k = 0; k < FrequentItemset.get(i).size(); k++)
				hsSet.add(FrequentItemset.get(i).get(k));
			// 添加前长度
			int hsLength_before = hsSet.size();
			hsSettemp = (HashSet<String>) hsSet.clone();
			// 频繁集第i行与第j行(j>i)连接
			for (int h = i + 1; h < FrequentItemset.size(); h++) {
				// 每次添加且添加一个元素组成
				// 新的频繁项集的某一行，
				// ！！！做连接的hasSet保持不变
				hsSet = (HashSet<String>) hsSettemp.clone();
				for (int j = 0; j < FrequentItemset.get(h).size(); j++)
					hsSet.add(FrequentItemset.get(h).get(j));
				int hsLength_after = hsSet.size();
				if (hsLength_before + 1 == hsLength_after && isnotHave(hsSet, nextCandidateItemset)) {
					// 如果不相等，表示添加了1个新的元素 同时判断其不是候选集中已经存在的一项
					Iterator<String> itr = hsSet.iterator();
					List<String> tempList = new ArrayList<String>();
					while (itr.hasNext()) {
						String Item = (String) itr.next();
						tempList.add(Item);
					}
					nextCandidateItemset.add(tempList);
				}

			}

		}
		return nextCandidateItemset;
	}
	
	/**
	 * 
	* 
	* 判断新添加元素形成的候选集是否在新的候选集中
	* @param @param hsSet
	* @param @param nextCandidateItemset
	* @param @return
	* @return boolean
	* @throws
	* @author shuyy
	 */
	private static boolean isnotHave(HashSet<String> hsSet, List<List<String>> nextCandidateItemset) {
		// 判断hsset是不是candidateitemset中的一项
		List<String> tempList = new ArrayList<String>();
		Iterator<String> itr = hsSet.iterator();
		// 将hsset转换为List<String>
		while (itr.hasNext()) {
			String Item = (String) itr.next();
			tempList.add(Item);
		}
		// 遍历candidateitemset，看其中是否有和templist相同的一项
		for (int i = 0; i < nextCandidateItemset.size(); i++)
			if (tempList.equals(nextCandidateItemset.get(i)))
				return false;
		return true;
	}
	
	/**
	 * 
	* 
	* 关联规则挖掘
	* @param @param poorRank
	* @param @return
	* @return List<AprioriRuleModel>
	* @throws
	* @author shuyy
	 */
	public static List<AprioriRuleModel> AssociationRulesMining(String poorRank) {
		List<AprioriRuleModel> rs = new ArrayList<AprioriRuleModel>();
		for (int i = 0; i < frequentItemset.size(); i++) {
			List<String> tem = frequentItemset.get(i);
			if (tem.size() > 1) {
				List<String> temclone = new ArrayList<>(tem);
				// 得到频繁项集tem的所有子集
				List<List<String>> AllSubset = getSubSet(temclone);
				AprioriRuleModel ap = null;
				for (int j = 0; j < AllSubset.size(); j++) {
					List<String> s1 = AllSubset.get(j);
					List<String> s2 = gets2set(tem, s1);
					ap = new AprioriRuleModel();
					String result = isAssociationRules(s1, s2, tem, poorRank);
					if (result != null) {
						ap.setRule1(result.split("&")[0]);
						ap.setRule2(result.split("&")[1]);
						double sup = Double.parseDouble(result.split("&")[2]);
						double supNum = sup / (record.size());
						ap.setSupport(Double.toString(supNum));
						ap.setConfidence(result.split("&")[3]);
						rs.add(ap);
					}
				}
			}

		}
		return rs;
	}
	
	/**
	 * 
	* 
	* 得到子频繁集
	* @param @param set
	* @param @return
	* @return List<List<String>>
	* @throws
	* @author shuyy
	 */
	public static List<List<String>> getSubSet(List<String> set) {
		// 用来存放子集的集合，如{{},{1},{2},{1,2}}
		List<List<String>> result = new ArrayList<>(); 
		int length = set.size();
		// 2的n次方，若集合set为空，num为0；若集合set有4个元素，那么num为16.
		int num = length == 0 ? 0 : 1 << (length); 
		// 从0到2^n-1（[00...00]到[11...11]）
		for (int i = 1; i < num - 1; i++) {
			List<String> subSet = new ArrayList<>();
			int index = i;
			for (int j = 0; j < length; j++) {
				// 每次判断index最低位是否为1，为1则把集合set的第j个元素放到子集中
				if ((index & 1) == 1) { 
					subSet.add(set.get(j));
				}
				// 右移一位
				index >>= 1; 
			}
			 // 把子集存储起来
			result.add(subSet);
		}
		return result;
	}
	
	/**
	 * 
	* 
	* 计算tem减去s1后的集合即为s2
	* @param @param tem
	* @param @param s1
	* @param @return
	* @return List<String>
	* @throws
	* @author shuyy
	 */
    public static  List<String> gets2set(List<String> tem, List<String> s1){
        List<String> result=new ArrayList<>();
      //去掉s1中的所有元素
        for(int i=0;i<tem.size();i++)
        {
            String t=tem.get(i);
            if(!s1.contains(t))
                result.add(t);
        }
        return  result;
    }
    
    /**
     * 
    * 
    * 判断是否为关联规则
    * @param @param s1
    * @param @param s2
    * @param @param tem
    * @param @param rank
    * @param @return
    * @return String
    * @throws
    * @author shuyy
     */
	public static String isAssociationRules(List<String> s1, List<String> s2, List<String> tem, String rank) {
		double confidence = 0;
		int counts1;
		int countTem;
		if (s1.size() != 0 && s1 != null && tem.size() != 0 && tem != null) {
			counts1 = getCount(s1);
			countTem = getCount(tem);
			confidence = countTem * 1.0 / counts1;
			if (confidence >= MIN_CONFIDENCE) {
				if (s2.toString().equals(rank)) {
					return s1.toString() + "&" + s2.toString() + "&" + countTem + "&" + confidence;
				} else {
					return null;
				}
			} else
				return null;

		} else
			return null;
	}
	
	/**
	 * 
	* 
	*  根据频繁项集得到其支持度计数
	* @param @param in
	* @param @return
	* @return int
	* @throws
	* @author shuyy
	 */
    public static int getCount(List<String> in){
        int rt=0;
        for(int i=0;i<map.size();i++){
        	MymapCom tem=map.get(i);
            if(tem.isListEqual(in)) {
                rt = tem.getcount();
                return rt;
            }
        }
        return rt;
    }
}

/**
 * 
* 自定义的map类，一个对象存放一个频繁项集以及其支持度计数
* @author shuyy
* @date 2018年9月10日
 */
class  MymapCom{
    public List<String> li=new LinkedList<>();
    public  int count;
    //构造函数  新建一个对象
    public MymapCom(List<String> l,int c){
        li=l;
        count=c;
    }
    //返回得到当前频繁项集的支持度计数
    public int getcount(){
        return count;
    }
    //判断传入的频繁项集是否和本频繁项集相同
    public boolean isListEqual(List<String> in){
        if(in.size()!=li.size())//先判断大小是否相同
            return false;
        else {
        	//遍历输入的频繁项集，判断是否所有元素都包含在本频繁项集中
        	for(int i=0;i<in.size();i++){
                if(!li.contains(in.get(i))){
                	return false;
                }                    
            }
        }
        return true;//如果两个频繁项集大小相同，同时本频繁项集包含传入的频繁项集的所有元素，则表示两个频繁项集是相等的，返回为真
    }
}
