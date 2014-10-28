package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import tk.ANPRCloud.service.NumberPlateFilter;

public class CharsSegment implements NumberPlateFilter {
	
	// Result List to store the 7 characters
    ArrayList<Object> resultList = new ArrayList<Object>();

    // Constant for the char segment
    static private final int m_LiuDingSize = 7;
	static private final int CHAR_SIZE =  20;
	static private final int m_theMatWidth = 136;

    // Constructor to transmit arguments
    public CharsSegment(String arg){
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<Object> proc(ArrayList<Object> srcList) {
			    
		// Get input mat from srcList
		Mat input = (Mat) srcList.get(0);
				
		// Convert input to gray
		Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2GRAY);

		//Threshold input image
		Mat img_threshold = new Mat();
		Imgproc.threshold(input, img_threshold, 10, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY); // Highgui.imwrite("img_threshold.png", img_threshold);
			
		//去除车牌上方的柳钉以及下方的横线等干扰
		clearLiuDing(img_threshold);  //Highgui.imwrite("img_threshold.png", img_threshold);

		Mat img_contours = new Mat();
		img_threshold.copyTo(img_contours);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		
		Imgproc.findContours(img_contours,
			contours, // a vector of contours
			hierarchy,
			Imgproc.RETR_EXTERNAL, // retrieve the external contours
			Imgproc.CHAIN_APPROX_NONE); // all pixels of each contours

		//Start to iterate to each contour founded
		//vector<vector<Point> >::iterator itc = contours.begin();
		Iterator itc = contours.iterator();
			
		List<Rect> vecRect = new ArrayList<Rect>();

		//Remove patch that are no inside limits of aspect ratio and area.  
		//将不符合特定尺寸的图块排除出去
		while (itc.hasNext()) 
		{
			Rect mr = Imgproc.boundingRect((MatOfPoint) itc.next());
			Mat auxRoi =  new Mat(img_threshold, mr);
			if (verifySizes(auxRoi))
			{
				vecRect.add(mr);
			}
		}
		
		//if (vecRect.size() == 0)
		//	return -1;

		List<Rect> sortedRect = new ArrayList<Rect>();
		//对符合尺寸的图块按照从左到右进行排序
		SortRect(vecRect, sortedRect);

		int specIndex = 0;
		//获得指示城市的特定Rect,如苏A的"A"
		specIndex = GetSpecificRect(sortedRect);

		//根据特定Rect向左反推出中文字符
		//这样做的主要原因是根据findContours方法很难捕捉到中文字符的准确Rect，因此仅能
		//退过特定算法来指定
		Rect chineseRect;
		if (specIndex < sortedRect.size())
			chineseRect = GetChineseRect(sortedRect.get(specIndex));
		else
			return null;

		//新建一个全新的排序Rect
		//将中文字符Rect第一个加进来，因为它肯定是最左边的
		//其余的Rect只按照顺序去6个，车牌只可能是7个字符！这样可以避免阴影导致的“1”字符
		List<Rect> newSortedRect = new ArrayList<Rect>();
		newSortedRect.add(chineseRect);
		RebuildRect(sortedRect, newSortedRect, specIndex);

		/*if (newSortedRect.size() == 0)
			return -1;*/

		for (int i = 0; i < newSortedRect.size(); i++)
		{
			Rect mr = newSortedRect.get(i);
			Mat auxRoi = new Mat(img_threshold, mr);
			if (true)
			{
				auxRoi = preprocessChar(auxRoi);
				resultList.add(auxRoi);
			}
		}
		
		// Debug 
//		for (int i = 0; i < resultList.size(); i++){
//			Highgui.imwrite("chars_out" + i + ".png", (Mat) resultList.get(i));
//		}
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
	
	/*  
	 * Hard-work methods
	 */
	//! 直方图均衡，为判断车牌颜色做准备
	Mat histeq(Mat in)
	{
		Mat out = new Mat(in.size(), in.type());
		if(in.channels()==3)
		{
			Mat hsv = new Mat();
			List<Mat> hsvSplit =  new ArrayList<Mat>();
			Imgproc.cvtColor(in, hsv, Imgproc.COLOR_BGR2HSV);
			Core.split(hsv, hsvSplit);
			Imgproc.equalizeHist(hsvSplit.get(2), hsvSplit.get(2));
			Core.merge(hsvSplit, hsv);
			Imgproc.cvtColor(hsv, out, Imgproc.COLOR_HSV2BGR);
		}
		else if (in.channels() == 1)
		{
			Imgproc.equalizeHist(in, out);
		}
		return out;
	}
	
	//clearLiuDing
	//去除车牌上方的钮钉
	//计算每行元素的阶跃数，如果小于X认为是柳丁，将此行全部填0（涂黑）
	//X的推荐值为，可根据实际调整
	Mat clearLiuDing(Mat img)
	{
		int x = m_LiuDingSize;
		Mat jump = Mat.zeros(1, img.rows(), CvType.CV_32F);
		for(int i=0; i < img.rows(); i++)
		{
			int jumpCount = 0;
			for(int j=0; j < img.cols() - 1; j++)
			{
				//if (img.at<char>(i,j) != img.at<char>(i,j+1))
				if ( img.get(i, j)[0] != img.get(i, j+1)[0]) 
					jumpCount++;
			}	
			//jump.at<float>(i) = jumpCount;
			jump.put(0, i, new float[]{jumpCount});
		}
		for(int i=0; i < img.rows(); i++)
		{
			//if(jump.at<float>(i) <= x)
			if(jump.get(0, i)[0] <= x)
			{
				for(int j=0; j < img.cols(); j++)
				{
					//img.at<char>(i,j) = 0;
					img.put(i, j, new byte[]{0});
				}
			}
		}
		return img;
	}
	
	//! 字符尺寸验证
	boolean verifySizes(Mat r){
		//Char sizes 45x90
		float aspect = 45.0f/90.0f;
		float charAspect = (float)r.cols() / (float)r.rows();
		float error = 0.7f;
		float minHeight = 10;
		float maxHeight = 35;
		//We have a different aspect ratio for number 1, and it can be ~0.2
		float minAspect = 0.05f;
		float maxAspect = aspect+aspect*error;
		//area of pixels
		float area = Core.countNonZero(r);
		//bb area
		float bbArea=r.cols() * r.rows();
		//% of pixel in area
		float percPixels=area/bbArea;

		if(percPixels <= 1 && charAspect > minAspect && charAspect < maxAspect && r.rows() >= minHeight && r.rows() < maxHeight)
			return true;
		else
			return false;
	}

	//! 将Rect按位置从左到右进行排序
	int SortRect(List<Rect> vecRect, List<Rect> out)
	{
		List<Integer> orderIndex = new ArrayList<Integer>();
	    List<Integer> xpositions = new ArrayList<Integer>();

		for (int i = 0; i < vecRect.size(); i++)
		{
			orderIndex.add(i);
	        xpositions.add(vecRect.get(i).x);
		}

		float min=xpositions.get(0);
		int minIdx=0;
	    for(int i=0; i< xpositions.size(); i++)
		{
	        min=xpositions.get(i);
	        minIdx=i;
	        for(int j=i; j<xpositions.size(); j++)
			{
	            if(xpositions.get(j)<min){
	                min=xpositions.get(j);
	                minIdx=j;
	            }
	        }
	        int aux_i=orderIndex.get(i);
	        int aux_min=orderIndex.get(minIdx);
	        orderIndex.set(i, aux_min);
	        orderIndex.set(minIdx, aux_i);
	        
	        float aux_xi=xpositions.get(i);
	        float aux_xmin=xpositions.get(minIdx);
	        xpositions.set(i, (int) aux_xmin);
	        xpositions.set(minIdx, (int) aux_xi);
	    }

	    for(int i=0; i<orderIndex.size(); i++)
		{
	        out.add(vecRect.get(orderIndex.get(i)));
	    }

		return 0;
	}

	//! 找出指示城市的字符的Rect，例如苏A7003X，就是"A"的位置
	int GetSpecificRect(List<Rect> vecRect)
	{
		List<Integer> xpositions = new ArrayList<Integer>();
		int maxHeight = 0;
		int maxWidth = 0;

		for (int i = 0; i < vecRect.size(); i++)
		{
	        xpositions.add(vecRect.get(i).x);

			if (vecRect.get(i).height > maxHeight)
			{
				maxHeight = vecRect.get(i).height;
			}
			if (vecRect.get(i).width > maxWidth)
			{
				maxWidth = vecRect.get(i).width;
			}
		}

		int specIndex = 0;
		for (int i = 0; i < vecRect.size(); i++)
		{
			Rect mr = vecRect.get(i);
			int midx = mr.x + mr.width/2;

			//如果一个字符有一定的大小，并且在整个车牌的1/7到2/7之间，则是我们要找的特殊车牌
			if ((mr.width > maxWidth * 0.8 || mr.height > maxHeight * 0.8) &&
				(midx < (int)(m_theMatWidth / 7) * 2 && midx > (int)(m_theMatWidth / 7) * 1))
			{
				specIndex = i;
			}
		}

		return specIndex;
	}
	
	//! 根据特殊车牌来构造猜测中文字符的位置和大小
	Rect GetChineseRect(Rect rectSpe)
	{
		int height = rectSpe.height;
		float newwidth = (float) (rectSpe.width * 1.15);
		int x = rectSpe.x;
		int y = rectSpe.y;

		int newx = x - (int) (newwidth * 1.15);
		newx = newx > 0 ? newx : 0;

		Rect a = new Rect(newx, y, (int)(newwidth), height);

		return a;
	}
	
	//! 这个函数做两个事情
	//  1.把特殊字符Rect左边的全部Rect去掉，后面再重建中文字符的位置。
	//  2.从特殊字符Rect开始，依次选择6个Rect，多余的舍去。
	int RebuildRect(List<Rect> vecRect, List<Rect> outRect, int specIndex)
	{
		//最大只能有7个Rect,减去中文的就只有6个Rect
		int count = 6;
	
		for (int i = 0; i < vecRect.size(); i++)
		{
			//将特殊字符左边的Rect去掉，这个可能会去掉中文Rect，不过没关系，我们后面会重建。
			if (i < specIndex)
				continue;
	
			outRect.add(vecRect.get(i));
			if ((--count) == 0)
				break;
		}
		return 0;
	}
	
	//! 字符预处理
	Mat preprocessChar(Mat in){
		//Remap image
		int h=in.rows();
		int w=in.cols();
		int charSize=CHAR_SIZE ;	//统一每个字符的大小
		Mat transformMat=Mat.eye(2,3,CvType.CV_32F);
		int m =  (((w) > (h)) ? (w) : (h));
		//transformMat.at<float>(0,2)=m/2 - w/2;
		transformMat.put(0, 2, new float[]{ m/2 - w/2});
		//transformMat.at<float>(1,2)=m/2 - h/2;
		transformMat.put(1, 2, new float[]{ m/2 - h/2});

		Mat warpImage = new Mat(m,m, in.type());
		Imgproc.warpAffine(in, warpImage, transformMat, warpImage.size(), Imgproc.INTER_LINEAR, Imgproc.BORDER_CONSTANT, new Scalar(0) );
		Mat out = new Mat();
		Imgproc.resize(warpImage, out, new Size(charSize, charSize) ); 
		return out;
	}

}
