package myGame;

//import a2.MyGame;/
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class RightAction extends AbstractInputAction{

	private MyGame game;
	private Camera c;
	//private GameObject av;
	//private Vector4f oldUp;
	private Vector3f upVector, rightVector, fwdVector;
	//private Matrix4f rotAroundAvatarUp, oldRotation, newRotation;
	//private Matrix3f rightVector, fwdVector;
	public RightAction(MyGame g){
		game = g;
	}
		
	public void performAction(float time, Event e){
			
		float keyValue = e.getValue();
		if (keyValue > -.2 && keyValue < .2) return;  // deadzone

		c =(game.getEngine().getRenderSystem()).getViewport("MAIN").getCamera();
		rightVector = c.getU();
		upVector = c.getV();
		fwdVector = c.getN();
		
		//oldRotation = new Matrix4f(av.getWorldRotation());
		//oldUp = new Vector4f(0f,1f,0f,1f).mul(oldRotation);
		
		c.setV(upVector); /**Left off here @ 4:05 am need to fix this error**/
		c.setN(fwdVector);
		c.setU(rightVector);
		
		upVector.rotateAxis(0.01f, rightVector.x(), rightVector.y(), rightVector.z());
		fwdVector.rotateAxis(0.01f, rightVector.x(), rightVector.y(), rightVector.z());

		//c.setV(upVector); /**Left off here @ 4:05 am need to fix this error**/
		c.setN(fwdVector);
		c.setU(rightVector);
		
		
		/**rotAroundAvatarUp = new Matrix4f().rotation(-.005f, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
		newRotation = oldRotation;
		newRotation.mul(rotAroundAvatarUp);
		av.setLocalRotation(newRotation);**/
		
		//upVector.rotateAxis(-0.01f, rightVector.x(), rightVector.y(), rightVector.z());
		//fwdVector.rotateAxis(-0.01f, rightVector.x(), rightVector.y(), rightVector.z());
		
	}
}
