package cam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class FaceDetectionUsingCam extends Application {

	public FaceDetectionUsingCam(String args) {
		launch(args);
	}

	public FaceDetectionUsingCam() {
	}

	Mat matrix = null;
	
	@Override
	public void start(Stage stage) throws Exception {

FaceDetectionUsingCam obj = new FaceDetectionUsingCam();
			
		WritableImage writableImage = obj.capureFrame();
		obj.saveImage();
		int height = 1000;
		int width = 1900;
		
		
		ImageView imageView = new ImageView(writableImage);
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		
		Group root = new Group(imageView);
		Scene scene =  new Scene(root,width,height);
		
		stage.setTitle("Face Detection Using Cam");
		stage.centerOnScreen();
		stage.setScene(scene);
		stage.show();
			
			}

	private WritableImage capureFrame() {
		WritableImage writableImage = null;
		//loading library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//instanting the VideoCapture using camera 0 (computer webcam)
		VideoCapture capture = new VideoCapture(0);
		
		//reading the next video frame from the camera
		Mat matrix = new Mat();
		capture.read(matrix);
		
		if (!capture.isOpened()) {
			System.out.println("Camera not detected");
		}
		
		if (capture.read(matrix)) {
			String file = "C:/opencv/sources/modules/java/common_test/res/raw/lbpcascade_frontalface.xml";
		CascadeClassifier classifier =  new CascadeClassifier(file);
		
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(matrix, faceDetections);
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(
					matrix, 
					new Point(rect.x , rect.y), 
					new Point(rect.x + rect.width ,rect.y+rect.height), 
					new Scalar(0,0,255));
		}
		
		//creating BuffredImage from matrix
		BufferedImage image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
		
		WritableRaster writableRaster = image.getRaster();
		DataBufferByte databuffer = (DataBufferByte) writableRaster.getDataBuffer();
		byte[] data = databuffer.getData();
		matrix.get(0, 0,data);
		this.matrix = matrix;
		
		//createing the writable Image
		writableImage = SwingFXUtils.toFXImage(image, null);
		
		}
		return writableImage;
	}

	private void saveImage() {
//give path were to Save the image
		String file ="C:/Users/Nicklas/Workspace SilverSpin/OpenCVVideoProgram/SnapShot.jpg";
//Save the image		
		Imgcodecs.imwrite(file, matrix);
		
	}

}
