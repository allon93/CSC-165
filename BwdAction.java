package myGame;

//import a2.MyGame;
import tage.*;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;
import org.joml.*;

public class BwdAction extends AbstractInputAction
{
	private MyGame game;
	private GameObject av;
	//private Camera c;
	private Vector3f oldPosition, newPosition; //bwdDirection;
	private Vector4f bwdDirection;
	
	public BwdAction(MyGame g)
	{
		game = g;
	}
	
	@Override
	public void performAction(float time, Event e)
	{
		/*
		if(game.getSprint())
		{
			game.getEngine().getSceneGraph().getControllerNode(game.getActiveNode().getAvatar()).moveBackward(game.getSpeed() * 0.03f);
		}else{
			
			game.getEngine().getSceneGraph().getControllerNode(game.getActiveNode().getAvatar().moveBackward(game.getSpeed());
		}
		
		*/
		/*float keyValue = e.getValue();
		if(keyValue > -0.2 && keyValue < 0.2){
			return; //deadzone
		}*/
		
		//c = (game.getEngine().getRenderSystem()).getViewport("MAIN").getCamera();
		av = game.getAvatar();
		//oldPosition = c.getLocation();
		//bwdDirection = c.getN();
		oldPosition = av.getWorldLocation();
		bwdDirection = new Vector4f(0f,0f,-1f,-1f);
		bwdDirection.mul(av.getWorldRotation());
		bwdDirection.mul(-0.01f);
		newPosition = oldPosition.sub(bwdDirection.x(), bwdDirection.y(), bwdDirection.z());
		av.setLocalLocation(newPosition);
		//c.setLocation(newPosition);
	}


}