package tage;
//a2
import tage.*;
import tage.shapes.*;
import tage.nodeControllers.*;

import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import java.io.*;
import javax.swing.*;
import org.joml.*;

import tage.input.*;
import tage.input.action.*;
//import tage.rml.MathUtils;
//import graphicslib3D.Point3D;
//import graphicslib3D.Vector3D;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Version;
import net.java.games.input.Component;

import net.java.games.input.Event;
import tage.input.IInputManager.INPUT_ACTION_TYPE.*;

public class CameraOrbit3D{
	
	private Engine engine;
	private Camera camera;			//the camera being controlled
	private GameObject avatar;		// the target avatar the camera looks at
	private float cameraAzimuth; 	// rotation around target Y axis
	private float cameraElevation; 	//eleva trionof camera above target
	private float cameraRadius; 	// distance between camera and target
	//private Point3D targetPos; 		// target's position in the world
	private Vector3f worldUp;
	private float mult = 1f;
	
	public CameraOrbit3D(Camera cam, GameObject av, String gpName, Engine e){
		
		engine = e;
		camera = cam;
		avatar  = av;
		worldUp = new Vector3f(0.0f,1.0f,0.0f);
		cameraRadius = 2.0f; 		//distance from camera to avatar

		/**start from front of and Above the avatar**/
		cameraAzimuth = 0.0f; 		//start BEHIND and ABOV the target
		cameraElevation = 20.0f; 	// elevation is in degrees
		setupInputs(gpName);
		update(0.0f); //initialize camera state
		/**updateCameraPosition();**/
	}
	
	public void update(float time){
		//updateTarget();
		updateCameraPosition();
		camera.lookAt(avatar); //tage built-in function
	}
	
	/*private void  updateTarget(){
		targetPos = new Point3D(av.getWorldTranslation().getCol(3));
	}*/ 
	
	private void setupInputs(String gp){
		
		OrbitAzimuthAction azmAction = new OrbitAzimuthAction();
		OrbitElevationAction elevaAction  =  new OrbitElevationAction();
		//OrbitRadiusAction radAction = new OrbitRadiusAction();
		ZoomInAction zoominAction = new ZoomInAction();
		ZoomOutAction zoomoutAction = new ZoomOutAction();
		InputManager im = engine.getInputManager();
		
		//String gpName = im.getFirstGamepadName();
		//String kbName = im.getKeyboardName();
		if(gp == im.getFirstGamepadName()){
			im.associateAction(gp, net.java.games.input.Component.Identifier.Axis.RX, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gp, net.java.games.input.Component.Identifier.Axis.RY, elevaAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gp, net.java.games.input.Component.Identifier.Button._3, zoominAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gp, net.java.games.input.Component.Identifier.Button._4, zoomoutAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		if(gp == im.getMouseName()){
			im.associateAction(gp, net.java.games.input.Component.Identifier.Axis.X, azmAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			mult = 3f;
		}
		if(gp == im.getKeyboardName()){
			im.associateAction(gp, net.java.games.input.Component.Identifier.Key.I, zoominAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gp, net.java.games.input.Component.Identifier.Key.O, zoomoutAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
	}
	
	// Compute the camera’s azimuth, elevation, and distance, relative to
	// the target in spherical coordinates, then convert to world Cartesian
	// coordinates and set the camera position from that.
	
	public void updateCameraPosition(){
		
		Vector3f avatarRot = avatar.getWorldForwardVector();
		Vector3f avaRot = avatar.getWorldForwardVector();
		double avatarAngle = Math.toDegrees((double) avatarRot.angleSigned(new Vector3f(0f,0f,-1f), new Vector3f(0f,1f,0f)));
		double avaAngle = Math.toDegrees((double) avaRot.angleSigned(new Vector3f(0f,0f,-1f), new Vector3f(0f,1f,0f)));
		float totalAz = cameraAzimuth - (float)avatarAngle;double theta = Math.toRadians(cameraAzimuth);
		//float totalElv = cameraElevation - (float)avaAngle;
		double phi = Math.toRadians(cameraElevation);
		double r = Math.toRadians(cameraRadius);
		//double phi = Math.toRadians(cameraElevation);
		float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
		float y = cameraRadius * (float)(Math.sin(phi));
		float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
		
		//calculate new camera position in Cartesian coords
		//Point3D relativePosition = MathUtils.sphericalToCartesian(theta,phi,r);
		//Point3D desiredCameraLoc = relativePosition.add(targetPos);
		camera.setLocation(new Vector3f(x,y,z).add(avatar.getWorldLocation()));
		camera.lookAt(avatar);
	}

	private class OrbitAzimuthAction extends AbstractInputAction{
		
		public void performAction(float time, Event e){
			
			float rotAmount;
			if(e.getValue() < -0.2){
				rotAmount = -0.2f;
			}else if(e.getValue() > 0.2){
				rotAmount = 0.2f;
			}else{
				rotAmount = 0.0f;
			}
			cameraAzimuth += rotAmount;
			cameraAzimuth = cameraAzimuth % 360;
			updateCameraPosition();
		}
	}
	
	/**private class OrbitRadiusAction extends AbstractAction{
		ZoomInAction zoominAction  = new ZoomInAction();
		ZoomOutAction zoomoutAction  = new ZoomOutAction();
		
		public performAction(float time, Event e){
			
			float radAmount;
			if(e.getValue() < -0.1){
				radAmount = this.zoominAction;
			}else if(e.getValue() > 0.1){
				radAmount = this.zoomoutAction;
			}
			cameraRadius += radAmount;
			updateCameraPosition();
		}
	}**/
	
	private class ZoomInAction extends AbstractInputAction{
		
		public void performAction(float time, Event e){
			
			cameraRadius -= 0.1f;
		}
	}
	
	private class ZoomOutAction extends AbstractInputAction{
		
		public void performAction(float time, Event e){
			
			cameraRadius += 0.1f;
		}
	}
	private class OrbitElevationAction extends AbstractInputAction{
		
		public void performAction(float time, Event e){
			
			float rotAmount;
			if(e.getValue() < -0.2){
				rotAmount = -0.2f;
			}else if(e.getValue() > 0.2){
				rotAmount = 0.2f;
			}else{
				rotAmount = 20.0f;
			}
			cameraElevation += rotAmount;
			//cameraElevation = cameraElevation % 180;
			updateCameraPosition();
		}
	}
	
/*	public Point3D getLocation(){
		return camera.getLocation();
	}*/
}