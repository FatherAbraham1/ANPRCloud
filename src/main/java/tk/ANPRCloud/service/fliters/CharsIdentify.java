package tk.ANPRCloud.service.fliters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.CvANN_MLP;
import org.opencv.ml.CvSVM;

import tk.ANPRCloud.service.NumberPlateFilter;

public class CharsIdentify implements NumberPlateFilter {
	private String result;
    ArrayList<Object> resultList = new ArrayList<Object>();
    
    // The ann object
    static private CvANN_MLP ann;
	
    // Constants for configuration the mat
    static private int HORIZONTAL = 1;
	static private int VERTICAL = 0;
	static private int m_predictSize = 10;
	
	//中国车牌
	static private char strCharacters[] = {'0','1','2','3','4','5',
		'6','7','8','9','A','B', 'C', 'D', 'E','F', 'G', 'H', /* 没有I */
		'J', 'K', 'L', 'M', 'N', /* 没有O */ 'P', 'Q', 'R', 'S', 'T', 
		'U','V', 'W', 'X', 'Y', 'Z'}; 
	static private int numCharacter = 34; /* 没有I和0,10个数字与24个英文字符之和 */

	//以下都是我训练时用到的中文字符数据，并不全面，有些省份没有训练数据所以没有字符
	static private String strChinese[] = {"zh_cuan" /* 川 */, "zh_e" /* 鄂 */,  "zh_gan" /* 赣*/, 
		"zh_hei" /* 黑 */, "zh_hu" /* 沪 */,  "zh_ji" /* 冀 */, 
		"zh_jl" /* 吉 */, "zh_jin" /* 津 */, "zh_jing" /* 京 */, "zh_shan" /* 陕 */, 
		"zh_liao" /* 辽 */, "zh_lu" /* 鲁 */, "zh_min" /* 闽 */, "zh_ning" /* 宁 */, 
		"zh_su" /* 苏 */,  "zh_sx" /* 晋 */, "zh_wan" /* 皖 */,
		 "zh_yu" /* 豫 */, "zh_yue" /* 粤 */, "zh_zhe" /* 浙 */};

	static private int numChinese = 20;
	static private int numAll = 54; /* 34+20=54 */
	
	static private Map<String, String> m_map = new HashMap<String, String>();
		
	public static void ANNLoadModel(String path){
		ann = new CvANN_MLP();
		ann.clear();
		ann.load(path,"ann");
	}
	
	public static void LoadProvince(){
		if (m_map.size() == 0)
		{
			m_map.put("zh_cuan","川");
			m_map.put("zh_e","鄂");
			m_map.put("zh_gan","赣");
			m_map.put("zh_hei","黑");
			m_map.put("zh_hu","沪");
			m_map.put("zh_ji","冀");
			m_map.put("zh_jl","吉");
			m_map.put("zh_jin","津");
			m_map.put("zh_jing","京");
			m_map.put("zh_shan","陕");
			m_map.put("zh_liao","辽");
			m_map.put("zh_lu","鲁");
			m_map.put("zh_min","闽");
			m_map.put("zh_ning","宁");
			m_map.put("zh_su","苏");
			m_map.put("zh_sx","晋");
			m_map.put("zh_wan","皖");
			m_map.put("zh_yu","豫");
			m_map.put("zh_yue","粤");
			m_map.put("zh_zhe","浙");
		}
	}
	
	public CharsIdentify(String arg){
	}

	@Override
	public ArrayList<Object> proc(ArrayList<Object> srcList) {
		// Initialize result string
		result = "";		

		for (int i = 0; i < srcList.size(); i++){
			// Get src form srcList
		    Mat src = (Mat) srcList.get(i); 
			
			// Set isChinese flag
			boolean isChinese = ( i == 0 );
		    
			// Get the features from src
			Mat f = features(src, m_predictSize ); 

			int index = classify(f, isChinese);
	
			if (!isChinese)
			{
				result = result + strCharacters[index];
			}
			else
			{
				String s = strChinese[index - numCharacter];
				String province = (String) m_map.get(s);
				result = province + result;
			}
		}
		resultList.add(result); System.out.println(result);
		return resultList;
	}

	/*
	 *  Methods to do some hardcore work!
	 */
	//! 获得字符的特征图
	Mat features(Mat in, int sizeData)
	{
		//Histogram features
		Mat vhist = ProjectedHistogram(in, VERTICAL ); 
		Mat hhist = ProjectedHistogram(in, HORIZONTAL); 
		
		//Low data feature
		Mat lowData = new Mat();
		Imgproc.resize(in, lowData, new Size(sizeData, sizeData) );

		//Last 10 is the number of moments components
		int numCols = vhist.cols() + hhist.cols() + lowData.cols() * lowData.cols();

		Mat out = Mat.zeros(1, numCols, CvType.CV_32F);

		//Asign values to feature,ANN的样本特征为水平、垂直直方图和低分辨率图像所组成的矢量
		int j = 0;
		for(int i = 0; i < vhist.cols(); i++)
		{
			float[] pixel = new float[1];
			vhist.get(0, i, pixel);
			out.put(0, j, pixel);
			j++;
		}
		for(int i=0; i<hhist.cols(); i++)
		{
			//out.at<float>(j)=hhist.at<float>(i);
			float[] pixel = new float[1];
			hhist.get(0, i, pixel);
			out.put(0, j, pixel);
			j++;
		}
		for(int x=0; x<lowData.cols(); x++)
		{
			for(int y=0; y<lowData.rows(); y++){
				//out.at<float>(j)=(float)lowData.at<unsigned char>(x,y);
				byte[] pixel = new byte[1];
				lowData.get(x, y, pixel);
				float[] pixelf = new float[1];
				pixelf[0] = (float)unsignedToBytes(pixel[0]);
				out.put(0, j, pixelf);
				j++;
			}
		}

		return out;
	}

	//create the accumulation histograms,img is a binary image, t is 水平或垂直
	Mat ProjectedHistogram(Mat img, int t)
	{
		int sz = (t == 1) ? img.rows () :img.cols();
		Mat mhist = Mat.zeros(1, sz , CvType.CV_32F);

		for( int j = 0; j < sz; j++ ){
			Mat data = (t == 1) ? img.row(j) : img.col(j);
			float[] pixel = new float[1];
			pixel[0] = Core.countNonZero(data);
			mhist.put(0, j, pixel);	//统计这一行或一列中，非零元素的个数，并保存到mhist中
		}

		//Normalize histogram
		double min, max;
		MinMaxLocResult minMaxLocResult = Core.minMaxLoc(mhist);
		min = minMaxLocResult.minVal;
		max = minMaxLocResult.maxVal;

		if(max>0)
			mhist.convertTo(mhist,-1 , 1.0f/max, 0);//用mhist直方图中的最大值，归一化直方图

		return mhist;
	}

	//! 利用神经网络做识别
	int classify(Mat f, boolean isChinses){
		int result = -1;
		Mat output =  new Mat(1, numAll, CvType.CV_32FC1);
		ann.predict(f, output); // Highgui.imwrite("classify.png", output);

		if (!isChinses)
		{
			result = 0;
			float maxVal = -2;
			for(int j = 0; j < numCharacter; j++)
			{

				//float val = output.at<float>(j);
				float[] pixel = new float[1];
				output.get(0, j, pixel);
				float val = pixel[0];
				
				//cout << "j:" << j << "val:"<< val << endl;
				if (val > maxVal)
				{
					maxVal = val;
					result = j;
				}
			}
		}
		else
		{
			result = numCharacter;
			float maxVal = -2;
			for(int j = numCharacter; j < numAll; j++)
			{
				//float val = output.at<float>(j);
				float[] pixel = new float[1];
				output.get(0, j, pixel);
				float val = pixel[0];
				
				//cout << "j:" << j << "val:"<< val << endl;
				if (val > maxVal)
				{
					maxVal = val;
					result = j;
				}
			}
		}
		return result;
	}
	
	private static int unsignedToBytes(byte b) {
	    return b & 0xFF;
	}
	
	@Override
	public ArrayList<Object> getResult() {
		return resultList;
	}
}
