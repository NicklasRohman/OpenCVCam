package cam;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

public class CameraSnapShot extends Application {
	Mat matrix = null;

	public CameraSnapShot(String args) {
		launch(args);
	}

	public CameraSnapShot() {}

	@Override
	public void start(Stage stage) throws Exception {
		// Capturing the snapshot from the camera
		CameraSnapShot obj = new CameraSnapShot();
		WritableImage writableImage = obj.capureSnapShot();

		// Saving the image
		obj.saveImage();

		// setting the image view
		ImageView imageView = new ImageView(writableImage);
		imageView.setFitHeight(500);
		imageView.setFitWidth(500);
		imageView.setPreserveRatio(true);

		Group root = new Group(imageView);
		Scene scene = new Scene(root, 600, 600);

		stage.setTitle("Snapshot");
		stage.setScene(scene);
		stage.show();

	}

	private WritableImage capureSnapShot() {
		WritableImage writableImage = null;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// instantiating the VideoCapture class using camera 0 (laptop cam)
		VideoCapture videoCapture = new VideoCapture(0);

		// Reading the next video frame from the camera;
		Mat matrix = new Mat();
		videoCapture.read(matrix);

		// if camera i opened
		if (videoCapture.isOpened()) {
			// if there is next video frame
			if (videoCapture.read(matrix)) {
				// creating buffredImage from the matrix
				BufferedImage buffImg = new BufferedImage(matrix.width(), matrix.height(),
						BufferedImage.TYPE_3BYTE_BGR);
				WritableRaster raster = buffImg.getRaster();
				DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
				byte[] data = dataBuffer.getData();
				matrix.get(0, 0, data);
				this.matrix = matrix;

				writableImage = SwingFXUtils.toFXImage(buffImg, null);
			}
		}
		return writableImage;
	}

	private void saveImage() {
		// Saving the image
		String file = "C:/Users/Nicklas/Workspace SilverSpin/OpenCVVideoProgram/SnapShot.jpg";
//		// Instantiating the imgcodecs class;
//		Imgcodecs imgcodecs = new Imgcodecs();
		//saving it again
		Imgcodecs.imwrite(file, matrix);
		
	}

	}
	
