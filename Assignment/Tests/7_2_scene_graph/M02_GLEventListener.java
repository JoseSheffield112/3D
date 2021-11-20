import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
  
public class M02_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public M02_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,8f,18f));
    this.camera.setTarget(new Vec3(0f,2f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light.dispose(gl);
    floor.dispose(gl);
    cube.dispose(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model floor, cube;
  private Light light;
  private SGNode twoBranchRoot;// :O THIS IS DIFFERENT! Used for the scene graph m8. "This is the root of the scene graph we shall construct"
  
  private void initialise(GL3 gl) {
    createRandomNumbers();
	
	// loading tectures!
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
	
    //Initialising the light source!
    light = new Light(gl);
    light.setCamera(camera);
	
    //Setting up model for our wall!
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId0);
	
	//Setting up model for the cube used in the scene!
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
	// This matrix transform is irrelevant for this program. 
	// This is because the model matrix for the object
	// is ignored when a scene graph is used.
	// Instead, the scene graph supplies the transformations.
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId1, textureId2);
	
	// Using variables to store transformtion values!
	float OriginMovement=0.5f;
	float Height1=4.0f;
	float Height2=3.9f;
	float Height3=2.5f;
	float Girth1=2f;
	float Girth2=0.7f;
	float Girth3=5f;

    // New stuff -- explore it :d
    twoBranchRoot = new NameNode("two-branch structure");//The two-branch structure, twoBranchRoot, is an instance of SGNode, declared as an attribute of the class
    // Lower branch stuff
	//Naming the lower branch
	NameNode lowerBranch = new NameNode("lower branch");//NameNode is used solely to make the scene graph hierarchy clearer by allowing nodes that contain nothing but a String to represent their name
    //Applying transformation to lower branch
	Mat4 m = Mat4Transform.scale(Girth1,Height1,Girth1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,OriginMovement,0));
    TransformNode lowerBranchTransform = new TransformNode("scale("+Girth1+","+Height1+","+Girth1+"); translate(0,"+OriginMovement+",0)", m);//TransformNode is used to represent a transformation (a Mat4 instance) to be applied to its children in the scene graph
    //Reference to model instance for the lower branch
	ModelNode lowerBranchShape = new ModelNode("Cube(0)", cube);//TransformNode is used to represent a transformation (a Mat4 instance) to be applied to its children in the scene graph
	// Translating the current branch to the top
	// when drawing an object, it will be transformed by all the transformations in its ancestry all the way up to the root node
	TransformNode translateToTop = new TransformNode("translate(0,"+Height1+",0)",Mat4Transform.translate(0,Height1,0));// translating shape to top of current
    // upper branch operations
	NameNode upperBranch = new NameNode("upper branch");
    m = Mat4Transform.scale(Girth2,Height2,Girth2);
    m = Mat4.multiply(m, Mat4Transform.translate(0,OriginMovement,0));
    TransformNode upperBranchTransform = new TransformNode("scale("+Girth2+","+Height2+","+Girth2+");translate(0,"+OriginMovement+",0)", m);
    ModelNode upperBranchShape = new ModelNode("Cube(1)", cube);
	// Translating the current branch to the top*2!
	TransformNode translateToTop2 = new TransformNode("translatetoverytop(0,"+Height2+",0)",Mat4Transform.translate(0,Height2,0));// translating shape to top of current
    // upper branch operations
	NameNode upperBranch2 = new NameNode("upper branch - very top!");
    m = Mat4Transform.scale(Girth3,Height3,Girth3);
    m = Mat4.multiply(m, Mat4Transform.translate(0,OriginMovement,0));
    TransformNode upperBranchTransform2 = new TransformNode("scale("+Girth3+","+Height3+","+Girth3+");translate(0,"+OriginMovement+",0)", m);
    ModelNode upperBranchShape2 = new ModelNode("Cube(2)", cube);
        
    twoBranchRoot.addChild(lowerBranch);//SGNode contains a method called addChild() which is used to build the scene graph
      lowerBranch.addChild(lowerBranchTransform);// branch 1 - bottom (we've not added a translation transformation
        lowerBranchTransform.addChild(lowerBranchShape);
      lowerBranch.addChild(translateToTop);// branch2 - 2nd
        translateToTop.addChild(upperBranch);
					upperBranch.addChild(upperBranchTransform);
						upperBranchTransform.addChild(upperBranchShape);
							translateToTop.addChild(translateToTop2); // branch3 - my test
								translateToTop2.addChild(upperBranch2);
									upperBranch2.addChild(upperBranchTransform2);
										upperBranchTransform2.addChild(upperBranchShape2);
    twoBranchRoot.update();  // IMPORTANT – must be done every time any part of the scene graph changes
    // Following two lines can be used to check scene graph construction is correct
    //twoBranchRoot.print(0, false);
    //System.exit(0);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(getLightPosition());  // changing light position each frame
    light.render(gl);
    floor.render(gl);
    twoBranchRoot.draw(gl);
  }
  
  // The light's position is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 2.7f;
    float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }
  
  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}