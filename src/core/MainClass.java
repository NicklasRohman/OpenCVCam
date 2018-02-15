package core;

import cam.CameraSnapShot;
import cam.FaceDetectionImage;
import cam.FaceDetectionUsingCam;

public class MainClass {

	public MainClass(){	}
	
	public static void main(String[] args) {
	MainClass main = new MainClass();
	main.startSnapShotCamApp();

	}
	
	public void startSnapShotCamApp(){
	String args = null;
		//CameraSnapShot cam = new CameraSnapShot(args);
		//FaceDetectionImage face = new FaceDetectionImage(args);
	FaceDetectionUsingCam fduc = new FaceDetectionUsingCam(args);
	}

}
