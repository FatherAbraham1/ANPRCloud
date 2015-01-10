package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import tk.ANPRCloud.service.NumberPlateFilter;

public class reSize implements NumberPlateFilter {
	private Mat source;
	private Mat result;
	public static final int ncol = 24;
	public static final int nrow = 48;
	private double cscar, rscar;

	public reSize(Mat re) {
		// TODO Auto-generated constructor stub
		this.source = re.clone();
		cscar = (double) re.cols() / ncol;
		rscar = (double) re.rows() / nrow;
		result = new Mat(48, 24, CvType.CV_8UC3);
	}

	public Mat doimage() {
		// TODO Auto-generated method stub
		int newx, newy;
		double newc, newr;
		double x, y;

		// byte[] gray = new byte[3];

		// byte[] gray1 = new byte[3];
		// byte[] gray2 = new byte[3];
		// byte[] gray3 = new byte[3];
		// byte[] gray4 = new byte[3];

		for (int i = 24; i >= 0; i--) {
			for (int j = 48; j >= 0; j--) {

				x = (i + 0.5) * cscar - 0.5;
				newx = (int) x;
				newc = x - newx;

				y = (j + 0.5) * rscar - 0.5;
				newy = (int) y;
				newr = y - newy;

				if (newx > source.cols() - 2)
					newx = source.cols() - 2;

				if (newy > source.rows() - 2)
					newy = source.rows() - 2;

				// source.get(newy, newx, gray1);
				// source.get(newy, newx + 1, gray2);
				// source.get(newy + 1, newx, gray3);
				// source.get(newy + 1, newx + 1, gray4);

				double[] gray = new double[3];
				double[] gray1 = source.get(newy, newx);
				double[] gray2 = source.get(newy, newx + 1);
				double[] gray3 = source.get(newy + 1, newx);
				double[] gray4 = source.get(newy + 1, newx + 1);

				for (int k = 0; k < 3; k++) {
					gray[k] = (gray1[k] * (1 - newc) * (1 - newr) +
							        gray2[k] * newc       * (1 - newr) + 
							        gray3[k] * (1 - newc) * newr       + 
							        gray4[k] * newc       * newr);
				}
				result.put(j, i, gray);
			}
		}

		return result;
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
