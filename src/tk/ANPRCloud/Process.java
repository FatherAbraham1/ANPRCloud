package tk.ANPRCloud;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class Process {
	public Process(String file){
		// Solve java.library.path problem
		// sudo apt-get install libopencv2.4-java
		// sudo ln -s /usr/lib/jni/libopencv_java248.so /usr/lib/libopencv_java248.so
		// Check java.library.path
        // String libPathProperty = System.getProperty("java.library.path");
        // System.out.println(libPathProperty);
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//	    Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
//	    System.out.println( "mat = " + mat.dump() );
		
		 System.out.println("\nRunning DetectFaceDemo");

		    // Create a face detector from the cascade file in the resources
		    // directory.
		    CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/lbpcascade_frontalface.xml").getPath());
//		    Mat image = Highgui.imread(getClass().getResource("/lena.png").getPath());
		    Mat image = Highgui.imread(file);

		    // Detect faces in the image.
		    // MatOfRect is a special container class for Rect.
		    MatOfRect faceDetections = new MatOfRect();
		    faceDetector.detectMultiScale(image, faceDetections);

		    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

		    // Draw a bounding box around each face.
		    for (Rect rect : faceDetections.toArray()) {
		        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		    }

		    // Save the visualized detection.
		    String filename = "faceDetection.png";
		    System.out.println(String.format("Writing %s", filename));
		    Highgui.imwrite("/tmp/"+filename, image);
		    Highgui.imread(file);
	}
}
