package myGame;

import tage.ObjShape.*;
import tage.SceneGraph.*;
import org.joml.*;
import java.util.*;
import tage.*;
import tage.shapes.*;


public class Plane extends ObjShape{
	
	
	
	 private  static float [] vertices = new float [] { -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, //front
												1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, //right
												1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 0.0f, //back
												-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, //left
												-1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, //LF
												1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f //RR	// top left
												};

	private static float[]  texCoords = new float[] {	0.9f,0.5f,0.3f,1,		0,1,0,1,	0.9f,0.5f,0.3f,1,	// brown, green, brown
													0.9f,0.5f,0.3f,1,		0,1,0,1,	0,1,0,1,		// brown, green, green
													0,1,0,1,			0,1,0,1,	0,1,0,1,		// green, green, green
													0,1,0,1,			0,1,0,1,	0,1,0,1
												};


	private static float[] normals = new float[] {0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
											1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
											0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, -1.0f,
											-1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f,
											0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
											0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f
											};
	private static int[] triangles = new int[] {0,1,2, 2,1,3, 3,1,4, 3,4,5};
	
	public  Plane(){
		super();
		
		
		setNumVertices(6);
		setVertices(vertices);
		setTexCoords(texCoords);
		setNormals(normals);
		
		setMatAmb(Utils.silverAmbient());
		setMatDif(Utils.silverDiffuse());
		setMatSpe(Utils.silverSpecular());
		setMatShi(Utils.silverShininess());
	}

}