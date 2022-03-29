package myGame;

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
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Version;
import net.java.games.input.Component;


public class MyGame extends VariableFrameRateGame
{
	private static Engine engine;
	public static Engine getEngine() { return engine; }

	
	private int counter=0;
	private InputManager im;
	private Vector3f currentPosition;
	private double startTime, prevTime, elapsedTime, amt, plyScore;
	
	//private CameraOrbitController orbitController;
	private CameraOrbit3D orbit3D;
	
	private GameObject tor, avatar, cub, pyr,sph, x, y, z;
	private ObjShape torS, dolS, cubS, pyrS, sphS, linxS, linyS, linzS;
	private TextureImage doltx, brick,pyrx, earth, chest;
	private Light light1;
	private NodeController rc, sc;
	
	private int NUM_OF_COINS  = 30;

	private int[] coinList = new int [NUM_OF_COINS];

	public MyGame() { super(); }

	public static void main(String[] args)
	{	MyGame game = new MyGame();
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes()
	{	dolS = new ImportedModel("dolphinHighPoly.obj");
		torS = new Torus(0.5f, 0.2f, 48);
		cubS = new Cube();
		pyrS = new Plane();
		//sphS = new Sphere();	
		linxS = new Line(new Vector3f(0f,0f,0f), new Vector3f(3f,0f,0f));
		linyS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,3f,0f));
		linzS = new Line(new Vector3f(0f,0f,0f), new Vector3f(0f,0f,-3f));

	}

	@Override
	public void loadTextures()
	{	
		doltx = new TextureImage("Dolphin_HighPolyUV.png");
		brick = new TextureImage("brick1.jpg");
		chest =  new TextureImage("chest1.jpg");
		pyrx= new TextureImage("sunmap.jpg");
		//chest = new TextureImage("chest1.jpg");
	}

	@Override
	public void buildObjects()
	{	Matrix4f initialTranslation, initialRotation, initialScale;

		// build dolphin in the center of the window
		avatar = new GameObject(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(-1f,0f,1f);
		//initialScale = (new Matrix4f()).scaling(3.0f);
		avatar.setLocalTranslation(initialTranslation);
		//avatar.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f));
		avatar.setLocalRotation(initialRotation);

		//build torus along X-axis
		tor = new GameObject(GameObject.root(), torS);
		initialTranslation = (new Matrix4f()).translation(1,0,0);
		tor.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.25f);
		tor.setLocalScale(initialScale);
	
		//build chest at the right of the window
		cub = new GameObject(GameObject.root(), cubS, brick);
		initialTranslation = (new Matrix4f()).translation(3,0,0);
		initialScale = (new Matrix4f()).scaling(0.5f);
		cub.setLocalTranslation(initialTranslation);
		cub.setLocalScale(initialScale);
	
		// build cube at the right of the window
		/*cub = new GameObject(GameObject.root(), cubS, brick);
		initialTranslation = (new Matrix4f()).translation(3,0,0);
		initialScale = (new Matrix4f()).scaling(0.5f);
		cub.setLocalTranslation(initialTranslation);
		cub.setLocalScale(initialScale);*/
		
		//build plane at center
		pyr = new GameObject(GameObject.root(),pyrS,brick);
		initialTranslation = (new Matrix4f()).translation(0,0,3);
		pyr.setLocalTranslation(initialTranslation);
		pyr.getRenderStates().hasLighting (true);
		initialScale = (new Matrix4f()).scaling(0.25f);
		pyr.setLocalScale(initialScale);
		
		// add X,Y,-Z axes
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0f,0f));
		(y.getRenderStates()).setColor(new Vector3f(0f,1f,0f));
		(z.getRenderStates()).setColor(new Vector3f(0f,0f,1f));
		
		
			x.getRenderStates().disableRendering();
			z.getRenderStates().disableRendering();
		
			x.getRenderStates().enableRendering();
			z.getRenderStates().enableRendering();
		
	}
	
	
	/**private void positionCameraBehindAvatar()
	{
		
		Camera cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
		Vector3f loc = avatar.getWorldLocation();
		Vector3f fwd = avatar.getWorldForwardVector();
		Vector3f up = avatar.getWorldUpVector();
		Vector3f right = avatar.getWorldRightVector();
		cam.setU(right);
		cam.setV(up);
		cam.setN(fwd);
		cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
		
	}**/
	/**@Override
	public void createViewports(){ 	//3/2/22 @ 11:59pm
		(engine.getRenderSystem()).addViewport("LEFT",0,0,1f,1f);
		(engine.getRenderSystem()).addViewport("RIGHT",.75f,0,.25f,.25f);
		
		Viewport leftVp = (engine.getRenderSystem()).getViewport("LEFT");
		Viewport rightVp =(engine.getRenderSystem()).getViewport("RIGHT");
		Camera leftCamera  = leftVp.getCamera();
		Camera rightCamera = rightVp.getCamera();
		
		rightVp.setHasBorder(true);
		rightVp.setBorderWidth(4);
		rightVp.setBorderColor(0.0f,1.0f,0.0f);
		
		leftCamera.setLocation(new Vector3f(-2,0,2));
		leftCamera.setU(new Vector3f(1,0,0));
		leftCamera.setV(new Vector3f(0,1,0));
		leftCamera.setN(new Vector3f(0,0,-1));
		
		rightCamera.setLocation(new Vector3f(0,2,0));
		rightCamera.setU(new Vector3f(1,0,0));
		rightCamera.setV(new Vector3f(0,0,-1));
		rightCamera.setN(new Vector3f(0,-1,0));
	}**/
	
	@Override
	public void initializeGame()
	{	prevTime = System.currentTimeMillis();
		startTime = System.currentTimeMillis();
		(engine.getRenderSystem()).setWindowDimensions(1900,1000);

		// -----------------adding Light-----------------------
		
		Light.setGlobalAmbient(.5f,.5f,.5f);
		
		/**light1 = new Light();
		light1.setLocation(new Vector3f(0f,5f,0f));
		(engine.getSceneGraph()).addLight(light1);**/
		// -------------- adding node controllers -----------
		rc = new RotationController(engine, new Vector3f(0,1,0), 0.001f); (engine.getSceneGraph()).addNodeController(rc);
		rc.enable();
		sc = new StretchController(engine, 2.0f);
		rc.addTarget(avatar);
		rc.addTarget(cub);
		sc.addTarget(avatar);
		(engine.getSceneGraph()).addNodeController(rc);
		(engine.getSceneGraph()).addNodeController(sc);
		//----------------- adding lights -----------------
		Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);

		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);

		// ------------- positioning the camera -------------
		//positionCameraBehindAvatar();
		
		//-------------------------- Controllers---------------------
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();
		String kbName = im.getKeyboardName();
		// ------------- positioning the camera -------------
		//Camera cam =(engine.getRenderSystem()).getViewport("MAIN").getCamera();
		Camera ca = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
		
		//orbit3D = new CameraOrbit3D(cam, avatar, gpName, engine);
		orbit3D = new CameraOrbit3D(ca, avatar,gpName,engine);
			
		FwdAction fwdAction = new FwdAction(this);
		BwdAction bwdAction = new BwdAction(this);
		LeftAction leftAction = new LeftAction(this);
		TurnAction turnAction = new TurnAction(this);
		YawAction yawAction = new YawAction(this);
		RightAction rightAction = new RightAction(this);
		
		// Attach the action object to keyboard and gamepad components
		
		ArrayList<Controller> controllers = im.getControllers();
		for(Controller c : controllers)
		{
			if(c.getType() == Controller.Type.KEYBOARD)
			{
				im.associateAction(c, Component.Identifier.Key.W, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Key.S, bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Key.A, leftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Key.D, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Key.UP, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Key.DOWN, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Key.SPACE, positioncam, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				// many more actions...
				
			}
			
			if(c.getType() == Controller.Type.GAMEPAD) 
			{
				im.associateAction(c, Component.Identifier.Button._3, bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Axis.Y, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Axis.Y, bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Axis.X, leftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Button._1, turnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Axis.X, turnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Axis.Y, bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Axis.X, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Axis.X, leftAction, InputManger.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				// if 
				//im.associateAction(c, Component.Identifier.Axis.Y, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN
				
				//if
				// many more actions...
			}//else{
					
					//im.associateAction(c, Component.Identifier.Axis.X, turnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
					//im.associateAction(c, Component.Identifier.Axis.X, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
					
					//im.associateAction(c, Component.Identifier.Axis.Y, bwdAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
				//}
			
			
			/* if(c.getType() == Controller.Type.STICK){
				//im.associateAction(c, Component.Identifier.Axis.Y, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Axis.Y, bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				//im.associateAction(c, Component.Identifier.Axis.X, leftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
				im.associateAction(c, Component.Identifier.Axis.X, turnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			}*/
		}
		// Keyboard Action
		/*if(im.getKeyboardName() != null){
			
					
			//String kbName = im.getKeyboardName();
			
			im.associateAction(kbName, Component.Identifier.Key.W, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(kbName, Component.Identifier.Key.S, bwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(kbName, Component.Identifier.Key.A, turnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(kbName, Component.Identifier.Key.D, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(kbName, Component.Identifier.Key.UP, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(kbName, Component.Identifier.Key.DOWN, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		//}else if(im.getFirstGamepadName() != null){//Gampad Action
		//if(im.getFirstGamepadName() != null){
			
			//String gpName = im.getFirstGamepadName();
/*
			im.associateAction(gpName, Component.Identifier.Axis.Y, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName, Component.Identifier.Axis.X, yawAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			//im.associateAction(gpName, Component.Identifier.Axis.X, turnAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName, Component.Identifier.Axis.Y, bwdAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			im.associateAction(gpName, Component.Identifier.Axis.X, leftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			//im.associateAction(gpName, Component.Identifier.Key.W, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);*/
		//}
	
	}
	//public GameObject getAvatar(){ return avatar;}
	
	/*public void yaw(float deltaX){
		float tilt;
		Camera c = engine.getRenderSystem().getViewport("MAIN").getCamera();
		Vector3f rightVector = c.getU();
		Vector3f upVector = c.getV();
		Vector3f fwdVector = c.getN();
		if(deltaX < 0.0){
			tilt = -1.0f;
		}else if(deltaX > 0.0){
			tilt = 1.0f;
		}else{
			tilt = 0.0f;
		}
		rightVector.rotateAxis(0.01f*tilt, upVector.x(), upVector.y(), upVector.z());
		fwdVector.rotateAxis(0.01f*tilt, upVector.x(), upVector.y(), upVector.z());
		c.setU(rightVector);
		c.setN(fwdVector);
	}*/
	
	
	
	/*public String getPosition(){
		
		if(addNodeController.getViewport().equals("MAIN").getCamera()){
			return "OFF_DOLPHIN";
		}else{
			return "ON_DOLPHIN";
		}
	}*/
	
	
	public static float randInRangeFloat(int min, int max){
		
		return min + (float) (Math.random() * ((1 + max) - min));
	}
	
	@Override
	public void update()
	{
		elapsedTime = System.currentTimeMillis() - prevTime;
		prevTime = System.currentTimeMillis();
		amt = elapsedTime * 0.03;
		Camera c = (engine.getRenderSystem()).getViewport("MAIN").getCamera();

	
	// build and set HUD(System.currentTimeMillis()-startTime)/1000.0f);
		int elapsTimeSec = Math.round((float)(System.currentTimeMillis()-startTime)/1000.0f);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String counterStr = Integer.toString(counter);
		String dispStr1 = "Time = " + elapsTimeStr;
		String dispStr2 = "Prizes = " + counterStr;
		Vector3f hud1Color = new Vector3f(1,0,0);
		Vector3f hud2Color = new Vector3f(0,0,1);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, 500, 15);
	/*	Camera cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
		Vector3f loc = dol.getWorldLocation();
		Vector3f fw
		int elapsTimeSec = Math.round((float)d = dol.getWorldForwardVector();
		Vector3f up = dol.getWorldUpVector();
		Vector3f right = dol.getWorldRightVector();
		cam.setU(right);
		cam.setV(up);
		cam.setN(fwd);
		cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));*/
		im.update((float)elapsedTime);
		orbit3D.updateCameraPosition();
		//positionCameraBehindAvatar();
		
		
	
	}
	
	/**private void positionCameraBehindAvatar()
	{
		
		Camera cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
		Vector3f loc = avatar.getWorldLocation();
		Vector3f fwd = avatar.getWorldForwardVector();
		Vector3f up = avatar.getWorldUpVector();
		Vector3f right = avatar.getWorldRightVector();
		cam.setU(right);
		cam.setV(up);
		cam.setN(fwd);
		cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
		
	}**/
	
	//Collision Detection
	
	/**public float collisionCheck(){
		
	}**/
	
	public GameObject getAvatar(){
		return avatar;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_2:
					//System.out.println("hi");
					sc.toggle();
					break;
		}
		super.keyPressed(e);
	}



	/*@Override
	public void keyPressed(KeyEvent e)
	{	Vector3f loc, fwd, up, right, newLocation;
		Camera cam;

		switch (e.getKeyCode())
		{	case KeyEvent.VK_1:
				rc.toggle();
				break;
			case KeyEvent.VK_2: // move dolphin forward
				fwd = dol.getWorldForwardVector();
				loc = dol.getWorldLocation();
				newLocation = loc.add(fwd.mul(.02f));
				dol.setLocalLocation(newLocation);
				break;
			case KeyEvent.VK_3: // move dolphin backward
				fwd = dol.getWorldForwardVector();
				loc = dol.getWorldLocation();
				newLocation = loc.add(fwd.mul(-.02f));
				dol.setLocalLocation(newLocation);
				break;
			case KeyEvent.VK_4: // view from dolphin
				cam = (engine.getRenderSystem().getViewport("MAIN").getCamera());
				loc = dol.getWorldLocation();
				fwd = dol.getWorldForwardVector();
				up = dol.getWorldUpVector();
				right = dol.getWorldRightVector();
				cam.setU(right);
				cam.setV(up);
				cam.setN(fwd);
				cam.setLocation(loc.add(up.mul(1.3f)).add(fwd.mul(-2.5f)));
				break;
		}
		super.keyPressed(e);
	}*/
}