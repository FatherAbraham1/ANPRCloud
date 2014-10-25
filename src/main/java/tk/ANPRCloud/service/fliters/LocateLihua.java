package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.highgui.Highgui;

import tk.ANPRCloud.service.NumberPlateFilter;

public class LocateLihua implements NumberPlateFilter {
	ArrayList<Object> resultList = new ArrayList<Object>();
	
	private Mat img, img1,img2;
	private int[][] data;
	private int [] s;
	private int [] e;
	private int [] r;
	
	private int [] s1 = new int [10];
	private int [] e1 = new int [10];
	private int [] r1;
	
	private int [] sr = new int [10];
	private int [] er = new int [10];
	
	public LocateLihua (String arg){
		
	}
	
	private void ini() {
		int t = 15;
		int k = 0;      /////跳变间距
		int c = 14;
		boolean flag = false;
		
		for(int i =0;i<img.rows();i++){		 
			for(int j = 1;j<img.cols();j++)
			{
				if(data[i][j-1]!=data[i][j])
				{
					if(flag)
					{
					if(k<t){   ////////满足要求的跳变点
					e[i] = j;
					r[i]++;
					}else if(r[i]<c) {     ////////排除前面，扫描后面
							s[i]=j;
							e[i]=j;
							r[i]=1;
					}else {
						k=0;
						break;
					}
				}else {      //////确定起始点
					s[i]=j;
					e[i]=j;
					r[i]=1;
					flag = true;
				}
					k=0;
				}else{
				k++;
				}
			}
		}
		
	}
	
	private void row(){
		int b = 5;
		int lmin = 30;
		int lmax = 200;
		int i=img.rows()-1,j=0;
		int jmin = 15;
		int jmax = 50;
		int c=0,d=0,cmin = 10;
		boolean flag = true;
		
		while(i>0)
		{
			if(r[i]>jmin && r[i]<jmax && e[i]-s[i]>=lmin && e[i]-s[i]<=lmax)
			{		
				if(flag)
				{
					sr[j]=i;				
					flag = false;
				}
				 c++;
				 d=0;
			}
			else {
				if(d<b)
				{
					d++;
				}else if(c<cmin)    //////以上行不符合条件
				{
					c=0;
					flag = true;
				}else{
					er[j] = i;
					
					if(sr[j]-er[j]<=50){
					j++;
					c=0;
					flag = true;
				}else{
					c=0;
					flag = true;
				}
				}
			}
			i--;
		}
	}
	
	private void cols(){
		boolean flag = false;
		int sum = 0;
		int insum = 0;	
		for(int i =0;i<img.cols();i++)   ///////////统计白点数
		{
			r1[i]=0;
			for(int j = sr[0];j>er[0];j--)
			{
				if(data[j][i]==255)
				{
					r1[i]++;
				}
			}
		}
		
		for (int i = 1; i < img.cols() - 1; i++) {
			if (r1[i] > 10) {
				sum=0;
				int k = r1[i] - r1[i-1];
				if (k >=7) {
					if (!flag) {
						s1[0] = i;
						e1[0] = i;
						insum =1;
						flag = true;
					}			
				}
				insum++;
			} else {
				sum++;
				if( flag && insum>60 && sum>20)	{
					e1[0] = i-sum;
					break;
				}	
       if (sum >20) {
					flag = false;
				} 
			}
		}

	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
		Mat mat1 = (Mat)src.get(0); // Gray
		Mat mat2 = (Mat)src.get(1); // Color
		
		this.img = mat1;
		this.img2 = mat2;
		s = new int [img.rows()];
		e = new int [img.rows()];
		r = new int [img.rows()];
		
		r1 = new int [img.cols()];

		data = new int[img.rows()][img.cols()];
		for (int i = 0; i < this.img.rows(); i++) {
			for (int j = 0; j < this.img.cols(); j++) {
				data[i][j] = (int) img.get(i, j)[0];
			}
		}
		
		ini();
		row();
		cols();
		
		Range rangeR = new Range(er[0], sr[0]);
		Range rangeC = new Range(s1[0], e1[0]);	
		img1 = new Mat(img2, rangeR, rangeC).clone();
		Highgui.imwrite("/tmp/locate.png", img1);
		resultList.add(img1);
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
