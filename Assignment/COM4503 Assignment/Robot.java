import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public final class Robot {

	/**
	 * @author acd17ja	
	 * Class adapted from Dr.Maddocks Mesh class.
	 */
	 
	/*
		Class variables
	*/
	private static float bodyHeight;
	private static float headScale;
  
  public Robot(GL3 gl, float bodyHeight, float headScale) {
    this.bodyHeight = bodyHeight;
    this.headScale = headScale;
  }
  
  public static String headNode() {
		String temp = "THIS IS TEMPORARY";
		return temp;
	}
	
	public static buildHead(NameNode body){
		NameNode head = new NameNode("head"); 
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.translate(0,bodyHeight,0));
		m = Mat4.multiply(m, Mat4Transform.scale(headScale,headScale,headScale));
		m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode headTransform = new TransformNode("head transform", m);
		ModelNode headShape = new ModelNode("Sphere(head)", sphere); 

		body.addChild(head);
			head.addChild(headTransform);
			headTransform.addChild(headShape);
	}
}
