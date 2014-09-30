package tk.ANPRCloud;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Process {
	public Process(){
		// Solve java.library.path problem
		// sudo apt-get install libopencv2.4-java
		// sudo ln -s /usr/lib/jni/libopencv_java248.so /usr/lib/libopencv_java248.so
		// Check java.library.path
        // String libPathProperty = System.getProperty("java.library.path");
        // System.out.println(libPathProperty);
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    System.out.println( "mat = " + mat.dump() );
	}
}
