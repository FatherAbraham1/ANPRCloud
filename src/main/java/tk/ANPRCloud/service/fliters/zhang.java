package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import tk.ANPRCloud.service.NumberPlateFilter;

public class zhang implements NumberPlateFilter {
	private Mat sou, result,result1;
	private int[][] data;
	private int h, w;

	public zhang(Mat a) {
		// TODO Auto-generated constructor stub
		this.sou = a;
		
		h = a.rows();
		w = a.cols();
		
		result = new Mat(h,w,CvType.CV_8UC3);
		result1 = new Mat(h+4,w+4,CvType.CV_8UC3,Scalar.all(0));

		data = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				data[i][j] = (int) sou.get(i, j)[0];
			}
		}
	}

	public Mat doimage() {
		// TODO Auto-generated method stub

		int[] neighbor = new int[8];
		int[][] mark = new int[h][w];
		Boolean loop = true;
		int x, y;
		int markNum = 0;

		while (loop) {
			loop = false;

			// 第一步
			markNum = 0;
			for (y = 1; y < h - 1; y++) {
				for (x = 1; x < w - 1; x++) {
					// 条件1：p必须是前景点
					if (data[y][x] == 0)
						continue;

					neighbor[0] = data[y][x + 1]; // 8
					neighbor[1] = data[y - 1][x + 1]; // 9
					neighbor[2] = data[y - 1][x];
					neighbor[3] = data[y - 1][x - 1];
					neighbor[4] = data[y][x - 1];
					neighbor[5] = data[y + 1][x - 1];
					neighbor[6] = data[y + 1][x];
					neighbor[7] = data[y + 1][x + 1];
					// 条件2：2<=N(p）<=6
					int np = (neighbor[0] + neighbor[1] + neighbor[2]
							+ neighbor[3] + neighbor[4] + neighbor[5]
							+ neighbor[6] + neighbor[7]) / 255;
					if (np < 2 || np > 6)
						continue;

					// 条件3：S(p）=1
					int sp = 0;
					for (int i = 1; i < 8; i++) {
						if (neighbor[i] - neighbor[i - 1] == 255)
							sp++;
					}
					if (neighbor[0] - neighbor[7] == 255)
						sp++;
					if (sp != 1)
						continue;

					// 条件4：p2*p0*p6=0
					if ((neighbor[2] & neighbor[0] & neighbor[6]) != 0)
						continue;
					// 条件5：p0*p6*p4=0
					if ((neighbor[0] & neighbor[6] & neighbor[4]) != 0)
						continue;

					// 标记删除
					mark[y][x] = 1;
					markNum++;
					loop = true;
				}
			} // 将标记删除的点置为背景色
			if (markNum > 0) {
				for (y = 0; y < h; y++) {
					for (x = 0; x < w; x++) {
						if (mark[y][x] == 1) {
							data[y][x] = 0;
							double [] resdata = {0, 0, 0};
							result1.put(y+2, x+2, resdata);
						}
						else 
						{
							double [] resdata = {data[y][x],data[y][x],data[y][x]};
							result1.put(y+2, x+2, resdata);
						}
					}
				}
			}
			// //第二步
			markNum = 0;
			for (y = 1; y < h - 1; y++) {
				for (x = 1; x < w - 1; x++) {
					// 条件1：p必须是前景点
					if (data[y][x] == 0)
						continue;

					neighbor[0] = data[y][x + 1]; // 8
					neighbor[1] = data[y - 1][x + 1]; // 9
					neighbor[2] = data[y - 1][x];
					neighbor[3] = data[y - 1][x - 1];
					neighbor[4] = data[y][x - 1];
					neighbor[5] = data[y + 1][x - 1];
					neighbor[6] = data[y + 1][x];
					neighbor[7] = data[y + 1][x + 1];
					// 条件2：2<=N(p）<=6
					int np = (neighbor[0] + neighbor[1] + neighbor[2]
							+ neighbor[3] + neighbor[4] + neighbor[5]
							+ neighbor[6] + neighbor[7]) / 255;
					if (np < 2 || np > 6)
						continue;

					// 条件3：S(p）=1
					int sp = 0;
					for (int i = 1; i < 8; i++) {
						if (neighbor[i] - neighbor[i - 1] == 255)
							sp++;
					}
					if (neighbor[0] - neighbor[7] == 255)
						sp++;
					if (sp != 1)
						continue;

					// 条件4：p2*p0*p6=0
					if ((neighbor[2] & neighbor[0] & neighbor[6]) != 0)
						continue;
					// 条件5：p0*p6*p4=0
					if ((neighbor[0] & neighbor[6] & neighbor[4]) != 0)
						continue;

					// 标记删除
					mark[y][x] = 1;
					markNum++;
					loop = true;
				}
			}
			// 将标记删除的点置为背景色
			for (y = 0; y < h; y++) {
				for (x = 0; x < w; x++) {
					if (mark[y][x] == 1) {
						data[y][x] = 0;
						double[] resdata = { 0, 0, 0 };
						result1.put(y + 2, x + 2, resdata);
					} else {
						double[] resdata = { data[y][x], data[y][x], data[y][x] };
						result1.put(y + 2, x + 2, resdata);
					}
				}
			}
		}
	
		return result1;
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
