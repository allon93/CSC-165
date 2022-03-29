package myGame;

//import a2.MyGame;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class YawAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	private Vector3f upVector, rightVector, fwdVector;
	private Vector4f oldUp;
	private Matrix4f rotAroundAvatarUp, oldRotation, newRotation;

	public YawAction(MyGame g)
	{
		game = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		float keyValue = e.getValue();
		if(keyValue > -.2 && keyValue < .2)
			return; // deadzone
		
		c = (game.getEngine().getRenderSystem()).getViewport("MAIN").getCamera();
		av = game.getAvatar();
		oldRotation = new Matrix4f(av.getWorldRotation());
		oldUp = new Vector4f(0f,1f,0f,1f).mul(oldRotation);
		rightVector = c.getU();
		upVector = c.getV();
		fwdVector = c.getN();
		
		c.setU(rightVector);
		c.setN(fwdVector);
		c.setV(upVector);
		rotAroundAvatarUp = new Matrix4f().rotation(-.005f, new Vector3f(oldUp.x(), oldUp.y(), oldUp.z()));
		newRotation = oldRotation;
		newRotation.mul(rotAroundAvatarUp);
		av.setLocalRotation(newRotation);
		upVector.rotateAxis(0.01f, rightVector.x(), rightVector.y(), rightVector.z());
		fwdVector.rotateAxis(0.01f, rightVector.x(), rightVector.y(), rightVector.z());
		
		
	}
}