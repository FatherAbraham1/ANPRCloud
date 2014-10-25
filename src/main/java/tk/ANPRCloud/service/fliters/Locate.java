package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.RotatedRect;

import tk.ANPRCloud.service.NumberPlateFilter;

public class Locate implements NumberPlateFilter {
	private Mat result;
    ArrayList<Object> resultList = new ArrayList<Object>();
	private float m_error = 0.6f;
	private float m_aspect = 3.75f;
	private int m_verifyMin = 3;
	private int m_verifyMax = 20;
	private float m_angle = 30f;
	private int HEIGHT = 36;
	private int WIDTH = 136;
	private int TYPE = CvType.CV_8UC3;
	public Locate(String arg){
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<Object> proc(ArrayList<Object> srcList) {

		// Get the src image from the srcList
		Mat src = (Mat)srcList.get(0);
		
		// Initialize the result Mat
		result = src;
		
		// Get contours from Contours class
		List<MatOfPoint> contours = (List<MatOfPoint>)srcList.get(1);

		//Start to iterate to each contour founded
		Iterator itc = contours.iterator();
		List<RotatedRect> rects =  new ArrayList<RotatedRect>();

		//Remove patch that are no inside limits of aspect ratio and area.
		int t = 0;
		while (itc.hasNext())
		{
			//Create bounding rect of object
			RotatedRect mr = Imgproc.minAreaRect(
					new MatOfPoint2f( ((MatOfPoint) itc.next()).toArray() ));

			//large the rect for more
			if( verifySizes(mr))
			{
				rects.add(mr);
			}
		}

		int k = 1;
		for(int i=0; i< rects.size(); i++)
		{
			RotatedRect minRect = rects.get(i);
			if(verifySizes(minRect))
			{	
				// rotated rectangle drawing 
				// Get rotation matrix
				// 旋转这部分代码确实可以将某些倾斜的车牌调整正，
				// 但是它也会误将更多正的车牌搞成倾斜！所以综合考虑，还是不使用这段代码。
				// 2014-08-14,由于新到的一批图片中发现有很多车牌是倾斜的，因此决定再次尝试
				// 这段代码。
				Point[] rect_points = new Point[4]; 
				minRect.points( rect_points );
				//for( int j = 0; j < 4; j++ )
				//	Core.line( result, rect_points[j], rect_points[(j+1)%4], new Scalar(0f,255f,255f), 1 );
				float r = (float)minRect.size.width / (float)minRect.size.height;
				float angle = (float) minRect.angle;
				Size rect_size = minRect.size;
				if (r < 1)
				{
					angle = 90 + angle;
					double tmp = rect_size.width;
					rect_size.width = rect_size.height;
					rect_size.height = tmp;
				}
				//如果抓取的方块旋转超过m_angle角度，则不是车牌，放弃处理
				if (angle - m_angle < 0 && angle + m_angle > 0)
				{
					//Create and rotate image
					Mat rotmat = Imgproc.getRotationMatrix2D(minRect.center, angle, 1);
					Mat img_rotated = new Mat();
					Imgproc.warpAffine(src, img_rotated, rotmat, src.size(), Imgproc.INTER_CUBIC);

					Mat resultMat;
					resultMat = corpResultMat(img_rotated, rect_size, minRect.center);
					Highgui.imwrite("/tmp/Locate.png", resultMat);
					resultList.add(resultMat);
				}
			}
		}
		return resultList;
	}

	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
	
	private boolean verifySizes(RotatedRect mr)
	{
		float error = m_error;
		//Spain car plate size: 52x11 aspect 4,7272
		//China car plate size: 440mm*140mm，aspect 3.142857
		float aspect = m_aspect;
		//Set a min and max area. All other patchs are discarded
		//int min= 1*aspect*1; // minimum area
		//int max= 2000*aspect*2000; // maximum area
		int min= 44*14*m_verifyMin; // minimum area
		int max= 44*14*m_verifyMax; // maximum area
		//Get only patchs that match to a respect ratio.
		float rmin= aspect-aspect*error;
		float rmax= aspect+aspect*error;

		int area= (int) (mr.size.height * mr.size.width);
		float r = (float)mr.size.width / (float)mr.size.height;
		if(r < 1)
		{
			r= (float)mr.size.height / (float)mr.size.width;
		}

		if(( area < min || area > max ) || ( r < rmin || r > rmax ))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//! 显示最终生成的车牌图像，便于判断是否成功进行了旋转。
	Mat corpResultMat(Mat src, Size rect_size, Point center)
	{
		// Get pixel rectangle of src with sub-pixel accuracy
		Mat img_crop = new Mat();
		Imgproc.getRectSubPix(src, rect_size, center, img_crop);

		// Resize to HEIGHT * WIDTH 
		Mat resultResized = new Mat();
		resultResized.create(HEIGHT, WIDTH, TYPE);
		Imgproc.resize(img_crop, resultResized, resultResized.size(), 0, 0, Imgproc.INTER_CUBIC);
		return resultResized;
	}
}
