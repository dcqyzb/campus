package com.mit.campus.rest.algorithm.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 
* Kmeans算法
* @author shuyy
* @date 2018年9月10日
 */
public class KAverage {  
	//样例数据个数
    private int sampleCount = 0;      
    //数据维数
    private int dimensionCount = 0;  
  //中心点个数
    private int centerCount = 0;      
    //样例数据
    private double[][] sampleValues; 
  //中心点数据
    private double[][] centers;       
  //虚拟中心点数据
    private double[][] tmpCenters;    
    //测试文件路径
   // private String dataFile = "";    
    private List<String> datas;
    /** 
     * 通过构造器传入数据
     */  
    public KAverage(List<String> datalist) throws NumberInvalieException{  
        this.datas=datalist;
    }  
        
	private int initData(List<String> datas,int counts) throws NumberInvalieException{     
 	  
            /** 
             * 获取参数 
             */  
    	    if(datas.size()<1) {
    	    	return -1;
    	    }
    	    //样例个数
            this.sampleCount = datas.size();  
          //样例特征维数
            this.dimensionCount =datas.get(0).split(",").length ;
            //聚类中心个数  
            this.centerCount = counts; 
            String dimensionsValue[] = new String[dimensionCount];
            double tmpValue = 0.0;  
            if (sampleCount <= 0 || dimensionCount <= 0 || centerCount <= 0) {  
                throw new NumberInvalieException("input number <= 0.");  
            }  
            if (sampleCount < centerCount) {  
                throw new NumberInvalieException(  
                        "sample number < center number");  
            }  
  
          //样例个数容器
            sampleValues = new double[sampleCount][dimensionCount + 1];  
            //中心点容器
            centers = new double[centerCount][dimensionCount]; 
          //虚拟中心点容器
            tmpCenters = new double[centerCount][dimensionCount];
  
             
               
                for (int j = 0; j < datas.size(); j++) {  
                    dimensionsValue = datas.get(j).split(",");  
                    for (int k = 0; k < dimensionsValue.length; k++) {  
                        tmpValue = Double.parseDouble(dimensionsValue[k]);  
                        sampleValues[j][k] = tmpValue;  
                    }                   
                }  
                    
        return 1;  
    }  
  
    /** 
     * 返回样本中第s1个和第s2个间的欧式距离 
     */ 
    @SuppressWarnings("unused")
	private double getDistance(int s1, int s2) throws NumberInvalieException {  
        double distance = 0.0;  
        if (s1 < 0 || s1 >= sampleCount || s2 < 0 || s2 >= sampleCount) {  
            throw new NumberInvalieException("number out of bound.");  
        }  
        for (int i = 0; i < dimensionCount; i++) {  
            distance += (sampleValues[s1][i] - sampleValues[s2][i])  
                    * (sampleValues[s1][i] - sampleValues[s2][i]);  
        }  
        return distance;  
    }  
    
    /** 
     * 返回给定两个向量间的欧式距离 
     */ 
    private double getDistance(double s1[], double s2[]) {  
        double distance = 0.0;  
        for (int i = 0; i < dimensionCount; i++) {  
            distance += (s1[i] - s2[i]) * (s1[i] - s2[i]);  
        }  
        return distance;  
    }  
  
    /** 
     * 修改样例数据分组标志
     */   
    private int getNearestCenter(int s) {  
        int center = 0;  
        double minDistance = Double.MAX_VALUE;  
        double distance = 0.0;  
        for (int i = 0; i < centerCount; i++) {  
            distance = getDistance(sampleValues[s], centers[i]);  
            if (distance < minDistance) {  
                minDistance = distance;  
                center = i;  
            }  
        }  
        sampleValues[s][dimensionCount] = center;  
        return center;  
    }  
  
    /** 
     * 更新所有中心点 
     */ 
    private void updateCenters() {  
        double center[] = new double[dimensionCount];          
        int count = 0;  
        for (int i = 0; i < centerCount; i++) {  
            count = 0;  
            for (int j = 0; j < sampleCount; j++) {  
                if (sampleValues[j][dimensionCount] == i) {  
                    count++;  
                    for (int k = 0; k < dimensionCount; k++) {  
                        center[k] += sampleValues[j][k];  
                    }  
                }  
            } 
            
        	if(count!=0) { 
                for (int j = 0; j < dimensionCount; j++) {
                	centers[i][j] = center[j] / count; 
                	 }
            	}else {
            		Random rand = new Random();
            		int index  = rand.nextInt(sampleValues.length);
            		for (int j = 0; j < dimensionCount; j++) {
                     centers[i][j] = sampleValues[index][j]; 
                     	 }	           		
            	}
            
            
            for (int m = 0; m < dimensionCount; m++) {  
                center[m] = 0;  
            }  
        }  
    }  
  
    /** 
     * 判断算法是否终止 
     */   
    private boolean toBeContinued() {  
        for (int i = 0; i < centerCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
                if (tmpCenters[i][j] != centers[i][j]) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 关键方法，调用其他方法，处理数据
     * zengb,相对原算法，直接在计算部分返回最终聚类结果。
     * 并在后台打印出分组内容。
     */  
    public double[][] doCaculate(int cpunt) {  
        try {
			initData(datas,cpunt);
		} catch (NumberInvalieException e) {			
			e.printStackTrace();
		}
        //初始化数据（样例数据）
        //初始化数据（中心点数据）
        initCenters(datas);       
        //初始化虚拟中心点
        for (int i = 0; i < centerCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
            	  //起初虚拟中心点数据为（0）
                tmpCenters[i][j] = 0;
            }  
        }  
        int diedCount = 1;
        while (toBeContinued()) {  
            for (int i = 0; i < sampleCount; i++) {  
                getNearestCenter(i);  
            }
            System.out.println("打印当前迭代的中心点值");
            for (int i = 0; i < centerCount; i++) { 
            	System.out.print("第"+i+"个: ");
                for (int j = 0; j < dimensionCount; j++) {  
                    tmpCenters[i][j] = centers[i][j];
                    System.out.print(" "+centers[i][j]);
                }  
                System.out.println();
            }  
            updateCenters();  
            System.out.println("***第" + diedCount + "次迭代聚类结果:****");  
            showResultData(); 
            diedCount++;
        }  
        
        System.out.println("*******<<最终结果>>********");  
        showResultData();  
        return  showData();
    }  
  
    /**
     * 初始化中心点
     */
    private void initCenters(List<String> datas) {
    	List<String> sortdata = new ArrayList<String>(datas);
    	Comparator<String> arg1 = new Comparator<String>() {
			//数据比较算法
			public int compare(String arg0, String arg1) {
				String oo = arg0.toString();
				String tt = arg1.toString();
				String oos[] = oo.split(",");
				String tts[] = tt.split(",");
				double datao = 0.0;
				double datat = 0.0;
				
				//如果是多维，则将每一维相加之和拿来比较
				for(int i=0;i<oos.length;i++) {
					datao = datao + Double.parseDouble(oos[i]);					
				}
				
				for(int k=0;k<tts.length;k++) {
					datat = datat + Double.parseDouble(tts[k]);					
				}
				
				if(datao==datat) { 
					return 0;
				}
				if(datao<datat) { 
					return -1;
				}
				if(datao>datat) { 
					return 1;
				}
				
				return 0;
			}
		};
		
		Collections.sort(sortdata, arg1 );
    	System.out.println("===数据排序后===");
    	for(String datao: sortdata) {
    		System.out.println(datao);
    	}
    	System.out.println("=====数据排序前====");
    	for(String datat: datas) {
    		System.out.println(datat);
    	}
    	int distance = sortdata.size()/centerCount;
    	int indext[] = new int[centerCount] ;
    	for(int kk = 0;kk<centerCount;kk++) {    		
    		indext[kk]=0+kk*distance;
    	}
    	System.out.println("初始化选中" + centerCount + "个中心点");  
    	for(int i = 0;i<centerCount;i++) {    		
    		String dataString =sortdata.get(indext[i]);
    		for(int k=0;k<dimensionCount;k++){
         		centers[i][k]=Double.parseDouble(dataString.split(",")[k]) ;   //选出K个中心点         		
         		System.out.print(" "+centers[i][k]);
         	}
    		System.out.println();
    	}
    	
      /*  Random rand = new Random();
    	boolean[]  bool = new boolean[sampleValues.length];
    	int index;//索引第几个样本  
    	System.out.println("初始化选中" + centerCount + "个中心点");  
    	for (int i = 0; i < centerCount; i++) {  
    		//随机选取centerCoun个不重复中心，但是如果不同中心点的数值完全一样未过滤。zengb
    		do{											//为使生成的数不重复
               index  = rand.nextInt(sampleValues.length);
            }while(bool[index]);//为使生成的数不重复 
            bool[index] = true;		                 	
         	for(int k=0;k<dimensionCount;k++){
         		centers[i][k]=sampleValues[index][k];   //选出K个中心点         		
         		System.out.print(" "+centers[i][k]);
         	}
         	System.out.println();
         } */  
	}

    /**
     * 显示数据 
     */  
    @SuppressWarnings("unused")
	private void showSampleData() {  
        for (int i = 0; i < sampleCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
                if (j == 0) {  
                    System.out.print(sampleValues[i][j]);  
                } else {  
                    System.out.print("," + sampleValues[i][j]);  
                }  
            }  
            System.out.println();  
        }  
    }  
  
    /**
     * 分组显示结果 
     */  
    private void showResultData() {  
        for (int i = 0; i < centerCount; i++) {  
            System.out.println("第" + i + "个分组内容：");  
            for (int j = 0; j < sampleCount; j++) {  
                if (sampleValues[j][dimensionCount] == i) {  
                    for (int k = 0; k <= dimensionCount; k++) {  
                        if (k == 0) {  
                            System.out.print(sampleValues[j][k]);  
                        } else {  
                            System.out.print("," + sampleValues[j][k]);  
                        }  
                    }  
                    System.out.println();  
                }  
            }  
        }  
    } 
    
    /** 
     * 显示数据 
     */
    private double[][] showData() { 
    	System.out.println("*****显示数据******");
        for(int i=0;i<sampleCount;i++){
        	for(int j=0;j<=dimensionCount;j++){
        		System.out.print(sampleValues[i][j]+"\t");        		
        	}
        	System.out.println();
        }
        return sampleValues;
    }  
    
    
    /**
     * 
    * 
    * 主函数入口，测试数据入口
    * @param @param args
    * @return void
    * @throws
    * @author shuyy
     */
    public static void main(String[] args) {  
        
    	/**
    	 * 1、也可以通过命令行得到参数
    	 * 2、测试数据来源文件
    	 */
       // String fileName = "D:\\sample4.txt";
        
        //通用法初始化数据，示例   
    	/**
    	 * bin 重点说明，datalist数据的索引顺序必须和传入对象数据list的保持一致，
    	 * 以保证可以通过索引一一对应。
    	 * 如book1在booklist中的索引是0，那么book1用于聚类的属性在datalist中的索引也是0。
    	 */
        List<String> datalist = new ArrayList<String>();
        datalist.add("8,7,4,6");        
        datalist.add("9,10,4,6");
        datalist.add("6,2,4,6");
        datalist.add("3,1,4,6");
        datalist.add("1,2,4,6");
        datalist.add("0,2,4,6");
        datalist.add("10,7,3,4");
        datalist.add("9,10,3,4");
        datalist.add("8,2,3,4");
        datalist.add("3,1,3,4");
        datalist.add("1,2,3,4");
        datalist.add("2,0,3,4"); 
        
        
        List<String> datalistt = new ArrayList<String>();
        datalistt.add("1");        
        datalistt.add("3");
        datalistt.add("9");
        datalistt.add("11");
        datalistt.add("9");
        datalistt.add("22");
        datalistt.add("7");
        datalistt.add("8");
        datalistt.add("7");
        datalistt.add("2");
        datalistt.add("6");
        datalistt.add("7");   
                 
        try {         	
            KAverage ka = new KAverage(datalistt); 
            //参数为中心点个数
            ka.doCaculate(3);             
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
} 