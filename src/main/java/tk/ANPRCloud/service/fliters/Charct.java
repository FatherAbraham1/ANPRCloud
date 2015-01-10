package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.Mat;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Charct implements NumberPlateFilter {

	private Mat sou;
	private int[][] data;
	private int[][] flag;
	private int h, w;
	private String re = " ";

	public Charct(Mat a) {
		// TODO Auto-generated constructor stub
		this.sou = a;
		h = a.rows();
		w = a.cols();

		data = new int[h][w];
		flag = new int[h][w];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				data[i][j] = (int) sou.get(i, j)[0];
			}
		}
	}

	public Mat doimage() {
		// TODO Auto-generated method stub
		int n = 0;
		for (int i = 1; i < h - 1; i++) {
			for (int j = 1; j < w; j++) {
				if (flag[i][j] == 0 && data[i][j] == 0) {
					n++;
					dome(i, j, n);
					i = 1;
					j = 1;
				}
			}
		}
		re = " " + n;
		switch (n) {
		case 1:
			identify1();
			break;
		case 2:
			identify2();
			break;
		case 3:
			identify3();
			break;
		default:
			break;

		}
		System.out.print(re);
		return null;
	}

	private void dome(int i, int j, int f) {

		if (data[i][j] == 0 && flag[i][j] == 0) {
			flag[i][j] = f;
			if (i == 0 || i == h - 1 || j == 0 || j == w - 1)
				return;
			else {
				dome(i + 1, j, f);
				dome(i - 1, j, f);
				dome(i, j + 1, f);
				dome(i, j - 1, f);
			}

		}
	}

	private void identify1() {
		boolean heng=false,shu=false;
		
		int sumheng =0 , sumshu =0;
		for(int i = 1;i<h;i++)
		{
			for(int j = 1;j<w;j++)
			{
				if(data[i][j]!=0)
					sumheng++;		
			}
			if(sumheng>=w/2)
				heng=true;
		sumheng=0;		
		}
			for(int j = 1;j<w;j++)
			{
				for(int i = 1;i<h;i++)
				{
				if(data[i][j]!=0)
					sumshu++;
			}
				if(sumshu>=h/3)
					shu=true;
		sumshu=0;		
		}
			
			if(heng)
			{
				if(shu)
					efghlt5();
				else
					z27();
			}
			else {
				if(shu)
					cjkmnuwy1();
					else 
					svx3();
			}

	}

	private void identify2() {
		int f = 0;
		for (int i = h / 2; i < h; i++)
			for (int j = 1; j < w; j++) {
				if (flag[i][j] == 2) {
					f = 2;
					break;
				}
			}
		switch (f) {
		case 0:
			P9R();
			break;
			
		case 2:
			OQD4A6();
			break;

		default:
			break;
		}

	}


	private void efghlt5() {
		int nheng = 0;
		int sum = 0;
		for(int i = 1;i<h;i++)
		{
			for(int j = 1 ;j<w;j++)
			{
				if(data[i][j]!=0)
					sum++;						
			}
			if(sum > (w/3))
				nheng++;
			sum = 0;
		}
		
		switch (nheng) {
		case 3:
			re = " E";
			return;
		case 2:
			re = " F";
			return;
		default:
			ghlt();
			break;
		}
		
	}

	private void ghlt() {
		// TODO Auto-generated method stub
		int nshu = 0;
		int sumshu = 0;
		for(int j = 1;j<w;j++)
		{
			for(int i = 1;i<h;i++)
			{
			if(data[i][j]!=0)
				sumshu++;
		}
			if(sumshu>=h/3)
				nshu++;
			sumshu=0;		
	}
		if(nshu ==1)
		{
			int sum = 0;
			for(int i = 1;i<=h/2;i++)
			{
				for(int j = 1 ;j<w;j++)
				{
					if(data[i][j]!=0)
						sum++;
				}
				if(sum>w/2)
				{
					re = " T";
					return;
				}
				sum = 0;
			}
			re = " L";
		}
		else
		{
			for(int i = 2*h/3;i<h;i++)
			{
				for(int j = w/3;j<2*w/3;j++)
					if(data[i][j]!= 0)
					{
						re = " G";
						return;
					}
			}
			re = " H";
			return;
		}
		
	}

	private void z27() {
		// TODO Auto-generated method stub
		int sum = 0;
		int sum1= 0;
		boolean shang=false,xia=false;
		for(int i = 1;i<=h/2;i++)
		{
			for(int j = 1 ;j<w;j++)
			{
				if(data[i][j]!=0)
					sum++;
			}
			if(sum>w/2)
				shang = true;
			sum = 0;
		}
		
		for(int i = h-1;i>h/2;i--)
		{
			for(int j = 1 ;j<w;j++)
			{
				if(data[i][j]!=0)
					sum1++;
			}
			if(sum1>w/2)
				xia = true;
			sum1 = 0;
		}
		
		if(shang && xia)
			re = " Z";
		else if(shang)
			re = " 7";
		else
			re = " 2";
		
	}

	private void svx3() {
		// TODO Auto-generated method stub
		int index=-1;
		digMatch svx1 = new digMatch(sou, "/home/lihua/桌面/1/sxv/", 4);
		svx1.doimage();
		index = svx1.getInd();
		switch (index) {
		case 0:
			re = " S";
			return;
		case 1:
			re = " V";
			return;
		case 2:
			re = " X";
			return;
		default:
			re = " 3";
			return;
		}
	}

	private void cjkmnuwy1() {
		// TODO Auto-generated method stub
		int nshu = 0;
		int sumshu = 0;
		for(int j = 1;j<w;j++)
		{
			for(int i = 1;i<h;i++)
			{
			if(data[i][j]!=0)
				sumshu++;
		}
			if(sumshu>=h/2.7)
				nshu++;
			sumshu=0;		
	}
		switch (nshu) {
		case 1:
			cjky1();
			return;
		case 2:
			mnu();
			return;
		default:
			re = " W";
			break;
		}
	}

	private void mnu() {
		// TODO Auto-generated method stub
		for(int i = 1;i<h/2;i++)
		{
			for(int j = w/3;j<2*w/3;j++)
				if(data[i][j]!= 0)
				{
					re = " N";
					return;
				}
		}
		re = " U";
		return;
	}

	private void cjky1() {
		// TODO Auto-generated method stub
		int sumshu = 0;
		for(int j = 1;j<w/3;j++)
		{
			for(int i = 1;i<h;i++)
			{
			if(data[i][j]!=0)
				sumshu++;
		}
			if(sumshu>=h/3)
			{
				if(sumshu>=h/1.5)
				{
					re= " K";
				  return;
				}
				else
				{
					re = " C";
					return;
				}
			}
			sumshu=0;		
	}
		for(int j = w/3;j<2*w/3;j++)
		{
			for(int i = 1;i<h;i++)
			{
			if(data[i][j]!=0)
				sumshu++;
		}
			if(sumshu>=h/3)
			{
				for(int j1=1;j1<w/3;j1++)
				{
					for(int i1=1;i1<h;i1++)
					{
						if(data[i1][j1]!=0)
							{re = " Y";
							return;
									}
						
							}
					}
				re = " 1";
				return;
				}
			}
			sumshu=0;	
			re = " J";
			return;
	}


	private void identify3() {
		int sum = 0;
		for (int j = 1; j < w / 2; j++) {
			for (int i = 1; i < h; i++) {
				if (data[i][j] != 0)
					sum++;
				if (sum >= h / 1.5) {
					re = " B";
					return;
				}
			}
			sum = 0;
		}
		re = " 8";
		return;

	}

	private void P9R() {

		int sum = 0;
		for (int j = 1; j < w / 2; j++) {
			for (int i = 1; i < h; i++) {
				if (data[i][j] != 0)
					sum++;
				if (sum >= h / 1.5) {
					for (int k1 = w - 2; k1 > w / 1.5; k1--) {
						for (int k2 = h - 2; k2 > h / 1.5; k2--) {
							if (data[k2][k1] != 0) {
								re = " R";
								return;
							}
						}
					}
					re = " P";
					return;
				}
			}
			sum = 0;
		}
		re = " 9";

	}

	private void OQD4A6() {
		int sum = 0;
		boolean a6 = false;
		int a46 = 1;

		for (int i = 1; i < h; i++) {
			for (int j = 1; j < w; j++) {
				if (flag[i][j] == 2)
					sum++;
			}
		}

		if (sum >= (h * w / 3))
			a46 = 2;

		switch (a46) {
		case 2:	
			boolean aq = false;
			for (int i = h - 2; i > 0; i--) {
				for (int j = 1; j < w; j++) {
					if (data[i][j] != 0) {
						for (int k = 1; k < w; k++) {
							if (flag[i - 1][k] == 2) {
								aq = true;
							}
						}
						if (!aq) {
							re = " Q";
							return;
						} else {
							int sumd = 0;
							for (int jd = 1; jd < w / 2; jd++) {
								for (int id = 1; id < h; id++) {
									if (data[id][jd] != 0)
										sumd++;
									if (sumd >= h / 1.8) {
										re = " D";
										return;
									}
								}
								sumd = 0;
							}
							re = " O";
							return;
						}
					}
				}
			}
			
		default:      //////////////4,6,A
			int sum1 = 0;
			for (int j = w - 1; j > w / 2; j--) {
				for (int i = 1; i < h; i++) {
					if (data[i][j] != 0)
						sum1++;
					if (sum1 >= h / 1.2) {
						re = " 4";
						return;
					}
				}
			}

			for (int i = h - 2; i > 0; i--) {
				for (int j = 1; j < w; j++) {
					if (data[i][j] != 0) {
						for (int k = 1; k < w; k++) {
							if (flag[i - 1][k] == 2) {
								a6 = true;
							}
						}
						if (a6) {
							re = " 6";
							return;
						} else {
							re = " A";
							return;
						}
					}
				}
			}
		}
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
