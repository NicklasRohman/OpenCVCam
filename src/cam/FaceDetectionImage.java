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

public class FaceDetectionImage extends Application {
	WritableImage writableImage = null;
	public FaceDetectionImage(String args) {
		launch(args);
	}

	public FaceDetectionImage() {
	}

	@Override
	public void start(Stage stage) throws Exception {

		FaceDetectionImage obj = new FaceDetectionImage();
		WritableImage writableImage = obj.snapShot();

		ImageView imageView = new ImageView(writableImage);
		imageView.setFitHeight(500);
		imageView.setFitWidth(500);
		imageView.setPreserveRatio(true);

		Group root = new Group(imageView);
		Scene scene = new Scene(root, 600, 600);

		stage.setTitle("Your Face?");
		stage.setScene(scene);
		stage.show();
	}

	private WritableImage snapShot() {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture cam = new VideoCapture(0);

		Mat matrix = new Mat();
		cam.read(matrix);

		if (cam.isOpened()) {
			if (cam.read(matrix)) {
				BufferedImage buffImg = new BufferedImage(matrix.width(), matrix.height(),
						BufferedImage.TYPE_3BYTE_BGR);
				WritableRaster writableRaster = buffImg.getRaster();
				DataBufferByte dataBufferByte = (DataBufferByte) writableRaster.getDataBuffer();
				byte[] data = dataBufferByte.getData();
				matrix.get(0, 0, data);
				Imgcodecs.imwrite("C:/Users/Nicklas/Workspace SilverSpin/OpenCVVideoProgram/SnapShot.jpg", matrix);
				
				writableImage = faceRecognizer(buffImg);
			}
		}

		return writableImage;
	}

	private WritableImage faceRecognizer(BufferedImage buffImg) {
		// load library and load file
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String file = "SnapShot.jpg";
		Mat src = Imgcodecs.imread(file);

		// Instantiating the CascadeClassifier
		String xmlFile = "C:/opencv/sources/modules/java/common_test/res/raw/lbpcascade_frontalface.xml";
		CascadeClassifier classifier = new CascadeClassifier(xmlFile);

		// Detecting the face in the snap
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(src, faceDetections);
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

		// Drawing Boxes
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(src, // Where to draw the box
					new Point(rect.x, rect.y), // bottom lift
					new Point(rect.x + rect.width, rect.y + rect.height), // top
																			// right
					new Scalar(0, 0, 255), 
					3); // RGB color
		}
		
		Imgcodecs.imwrite("C:/Users/Nicklas/Workspace SilverSpin/OpenCVVideoProgram/SnapShot.jpg", src);
		writableImage = SwingFXUtils.toFXImage(buffImg, null);
		return writableImage;
	}

}
