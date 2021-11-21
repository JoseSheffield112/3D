import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
  
public class M04_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public M04_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,12f,18f));
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
    sphere.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
   
  private boolean animation = false;
  private double savedTime = 0;
   
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }
   
  public void poseOne() {
    xPosition = 0f;
    zPosition = 0f;
    updateMove();
    torsoRotate.setTransform(Mat4Transform.rotateAroundX(0));
    torsoRotate.update();
  }

  public void poseTwo() {
    xPosition = -8f;
    zPosition = -8f;
    updateMove();
    torsoRotate.setTransform(Mat4Transform.rotateAroundX(45));
    torsoRotate.update();
  }
    
  public void poseThree() {
    xPosition = -8f;
    zPosition = 8f;
    updateMove();
    torsoRotate.setTransform(Mat4Transform.rotateAroundX(-45));
    torsoRotate.update();
  }
     
  public void poseFour() {
    xPosition = 8f;
    zPosition = -8f;
    updateMove();
  }
     
  public void poseFive() {
    xPosition = 8f;
    zPosition = 8f;
    updateMove();
  }

  private void updateMove() {
    robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,0,zPosition));
    robotMoveTranslate.update();
  }
/*  
  public void loweredArms() {
    stopAnimation();
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(180));
    leftArmRotate.update();
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(180));
    rightArmRotate.update();
  }
   
  public void raisedArms() {
    stopAnimation();
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(0));
    leftArmRotate.update();
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(0));
    rightArmRotate.update();
  }
 */ 
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model floor, sphere, cube, cube2;
  private Light light;
  private SGNode robotRoot;
  
  private float xPosition = 0;
  private float zPosition = 0;
  private TransformNode robotMoveTranslate, torsoRotate, headRotate;
  
  private void initialise(GL3 gl) {
    createRandomNumbers();
	// loading textures
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
        
    light = new Light(gl);
    light.setCamera(camera);
    
	// loading models
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId0);
    
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId1, textureId2);
    
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3, textureId4);
    
    cube2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId5, textureId6); 
    
    // robot - scene graph construction
	
    // variables
    float footRadius = 3f;
    float torsoRadius = 2f;
    float neckRadius = 0.3f;
    float headRadius = 1.5f;
    
    robotRoot = new NameNode("root");
	  // moving whole of robot in x axis & z axis
    robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(xPosition,0,zPosition));
    //Translating whole body above world floor
    TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));
    
	//Building the body
    //Foot
    NameNode foot = new NameNode("Foot");
      Mat4 m = Mat4Transform.scale(footRadius,footRadius,footRadius);
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
      TransformNode footTransform = new TransformNode("foot transform", m);
        ModelNode footShape = new ModelNode("Sphere(Foot)", sphere);

    // torso
    NameNode torso = new NameNode("torso");
      TransformNode torsoTranslate = new TransformNode("torso translation",
                                        Mat4Transform.translate(0,footRadius,0));
      torsoRotate = new TransformNode("upper body rotate",Mat4Transform.rotateAroundX(0));
      m = new Mat4(1);
      //m = Mat4Transform.translate(0,footRadius,0);
      m = Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius);//Mat4.multiply(m, Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
      TransformNode torsoTransform = new TransformNode("torso transform", m);
        ModelNode torsoShape = new ModelNode("Sphere(torso)", sphere);

    // neck
    NameNode neck = new NameNode("neck");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(neckRadius,neckRadius,neckRadius));
      m = Mat4.multiply(m, Mat4Transform.translate(0,(torsoRadius+(neckRadius*0.5f)),0));//Raising body a further .5 above the legs top!
      TransformNode neckTransform = new TransformNode("neck transform", m);
        ModelNode neckShape = new ModelNode("Sphere(neck)", sphere);

    //head
    NameNode head = new NameNode("head");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,neckRadius,0));//Raising body a further .5 above the legs top!
      m = Mat4Transform.translate(0,((neckRadius)+(torsoRadius*0.5f)),0);
      TransformNode headTransform = new TransformNode("head transform", m);
        ModelNode headShape = new ModelNode("Sphere(head)", sphere);
    
    // scene  graph
    robotRoot.addChild(robotMoveTranslate);
      robotMoveTranslate.addChild(robotTranslate);
        robotTranslate.addChild(foot);
          foot.addChild(footTransform);
            footTransform.addChild(footShape);
          foot.addChild(torso);
            torso.addChild(torsoTranslate);
              torsoTranslate.addChild(torsoRotate);
                torsoRotate.addChild(torsoTransform);
                  torsoTransform.addChild(torsoShape);
                  torsoTransform.addChild(neck);
                    neck.addChild(neckTransform);
                      neckTransform.addChild(neckShape);
                    neck.addChild(head);
                      head.addChild(headTransform);
                        headTransform.addChild(headShape);
    robotRoot.update();  // IMPORTANT - don't forget this
    //robotRoot.print(0, false);
    //System.exit(0);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(getLightPosition());  // changing light position each frame
    light.render(gl);
    floor.render(gl); 
    robotRoot.draw(gl);
  }
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
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