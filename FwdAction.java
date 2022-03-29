package myGame;

//import a2.MyGame;/
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class FwdAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	private Camera c;
	//private float keyValue;
	//private Vector3f oldPosition, newPosition, fwdDirection;
	private Vector3f oldPosition, newPosition;
	private Vector4f fwdDirection;
	
	public FwdAction(MyGame g)
	{
		game = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		c = (game.getEngine().getRenderSystem()).getViewport("MAIN").getCamera();
		av = game.getAvatar();
		//float keyValue = e.getValue();
		float fwdAmount;
		if(e.getValue() < -0.2){
			fwdAmount = -0.2f;
			oldPosition = av.getWorldLocation();
			fwdDirection = new Vector4f(0f,0f,-1f,-1f);
			fwdDirection.mul(av.getWorldRotation());
			fwdDirection.mul(-0.01f);
			newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
		
		}else if(e.getValue() > 0.2){
			fwdAmount = 0.2f;
			oldPosition = av.getWorldLocation();
			fwdDirection = new Vector4f(0f,0f,1f,1f);
			fwdDirection.mul(av.getWorldRotation());
			fwdDirection.mul(0.01f);
			newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());
		}else{
			return;
		}
		/**if(keyValue > -0.2 && keyValue < 0.2){
		
			return; //deadzone
		}**/
		/**c = (game.getEngine().getRenderSystem()).getViewport("MAIN").getCamera();
		av = game.getAvatar();
		//oldPosition = c.getLocation();
		//fwdDirection = c.getN();
		/**oldPosition = av.getWorldLocation();
		fwdDirection = new Vector4f(0f,0f,1f,1f);
		fwdDirection.mul(av.getWorldRotation());
		fwdDirection.mul(0.01f);
		newPosition = oldPosition.add(fwdDirection.x(), fwdDirection.y(), fwdDirection.z());**/
		av.setLocalLocation(newPosition);
		c.setLocation(newPosition);
	}
}