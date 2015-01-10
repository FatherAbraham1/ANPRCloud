package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Mat;

import tk.ANPRCloud.service.NumberPlateFilter;

public class digMatch implements NumberPlateFilter {
	private Mat sou;
	private String moban;
	private int[][] data;
	private int h, w,mobanshu; 
	private ArrayList<Mat> type = new ArrayList<Mat>();
//	private imgIO io = new imgIO();
	private int ind;
	
	public digMatch(Mat a,String mo, int n) {
		// TODO Auto-generated constructor stub
		this.sou = a;
		h = a.rows();
		w = a.cols();
		moban = mo;
		mobanshu = n;

		data = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				data[i][j] = (int) sou.get(i, j)[0];
			}
		}
	}

	private void moType () {
		
		String inname1="";
		for(int i=1;i<=mobanshu;i++)
		{
			inname1 = moban + i + ".jpg";
//			type.add(io.inImg(inname1));
		}
	}

	public Mat doimage() {
		// TODO Auto-generated method stub
		moType();
		int sum = 0;
		int N = 10000;
		int index = 0;
		for(int k =0;k<type.size();k++)
 {
			sum = 0;
			for (int i = 0; i < h-2; i++) {
				for (int j = 0; j < w-2; j++) {

					if ( (data[i+2][j+2] ==255) && ((int) type.get(k).get(i, j)[0]) != 255 ) {
						sum++;
					}
				}
			}
			if (sum <= N) {
				N = sum;
				index = k;
			}
		}
		setInd(index);
		return type.get(index);
	}

	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
