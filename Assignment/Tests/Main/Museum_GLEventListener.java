/**
 * This whole class was adapted from Dr. Maddocks " M04_GLEventListener.java" class
 * modifications have been made to it
 * 
 * *********************TO-DO*********************
 * - Identify methods you've introduced
*/
import java.util.ArrayList;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
  
public class Museum_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
  private Mat4 perspective;
  private Model wall;
  private DirectionalLight sunLight;
  private PointLight lightBulb, lightBulb2;
  private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
  private SpotLight lampLight;

  // Scene graph stuff
  private Robot myRobot;
  private Room theRoom;
  private SGNode roomScene = new NameNode("Museum - Root node");

  // dimness setting for light
  private static float dimness[] = {0f,0.33f,0.66f,1f}; // different dimness settings for the lights
  private static int currentDimness = 1; // dimness used for the museum lights - they're fancy but expensive!
  // xPosition for rendering the robot
  private float xPosition = 0;
  private float yPosition = 0;
  private float zPosition = 0;
    
  public Museum_GLEventListener(Camera camera) {
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
    sunLight.dispose(gl);
    lightBulb.dispose(gl);
    lightBulb2.dispose(gl);
    lampLight.dispose(gl);
    wall.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
 
  public void incXPosition() {
    xPosition += 0.5f;
    if (xPosition>5f) xPosition = 5f;
    myRobot.updateMoveX(xPosition);
  }

  public void decXPosition() {
    xPosition -= 0.5f;
    if (xPosition<-5f) xPosition = -5f;
    myRobot.updateMoveX(xPosition);
  }
  public void incYPosition() {
    yPosition += 0.5f;
    if (yPosition>5f) yPosition = 5f;
    myRobot.updateMoveY(yPosition);
  }

  public void decYPosition() {
    yPosition -= 0.5f;
    if (yPosition<-5f) yPosition = -5f;
    myRobot.updateMoveY(yPosition);
  }

  public void incZPosition() {
    zPosition += 0.5f;
    if (zPosition>5f) zPosition = 5f;
    myRobot.updateMoveZ(zPosition);
  }

  public void decZPosition() {
    zPosition -= 0.5f;
    if (zPosition<-5f) zPosition = -5f;
    myRobot.updateMoveZ(zPosition);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  
  private void initialise(GL3 gl) {
    createRandomNumbers();
	// loading textures
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/Floor.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/brickWall.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/door.jpg");
    
    /**
     * Initialising all my lights!
     */
    // Setting the ceiling lights of the museum - these are directional lights (museum did this for maximum exhibition clarity :) )
    // code is wasteful, but it solves my need
    sunLight = new DirectionalLight(gl, 1f);
    lightBulb = new PointLight(gl, 1f);
    lightBulb2 = new PointLight(gl, 1f);
    // Messing with point lights
    lampLight = new SpotLight(gl, 0.5f);
    sunLight.setCamera(camera);
    lightBulb.setCamera(camera);
    lightBulb2.setCamera(camera);
    lampLight.setCamera(camera);
    // an array with the different ceiling lights
    ceilingLights.add(lightBulb);
    ceilingLights.add(lightBulb2);
    
  	// loading models
    Vec3 whiteLight = new Vec3(1.0f, 1.0f, 1.0f);
    float wallSize = 16f;

/*
    // Far wall section
    //right section
    //
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4Transform.scale(wallSize,1f,wallSize);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,wallSize*0.5f,-wallSize*0.5f), modelMatrix);
    wall = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId7);
    //
    //Door
    //
    mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4Transform.scale(doorSize,1f,doorSize*2);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-doorSize*doorPositioning,doorSize,-wallSize*0.499f), modelMatrix);
    door = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId8);
*/
    // Left wall Section
    // Left wall
    // IT's still to do - you've just set it as the far wall for now!!
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(wallSize,1f,wallSize);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,wallSize*0.5f,-wallSize*0.5f), modelMatrix);
    wall = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId7);

    myRobot = new Robot(gl,camera, sunLight, ceilingLights, lampLight,  xPosition, yPosition, zPosition);
    theRoom = new Room(gl, camera, sunLight, ceilingLights, lampLight);
    SGNode roomChild = theRoom.getSceneGraph();

    roomScene.addChild(roomChild);
      roomChild.addChild(myRobot.getSceneGraph());
    roomScene.update();
    //roomScene.print(0, false);
    //System.exit(0);      
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    //For now rendering the lights - won't need to render them soon!
    sunLight.setPosition(new Vec3(0f,10f,0f));
    sunLight.render(gl);
    lightBulb.setPosition(new Vec3(-2f,-4f,0f));
    lightBulb.render(gl);
    lightBulb2.setPosition(new Vec3(2f,-4f,0f));
    lightBulb2.render(gl);
    updateLightColour();
    lampLight.setPosition(new Vec3(0f,12f,0f));
    lampLight.render(gl);
    wall.render(gl);
    roomScene.draw(gl);
  }
  /*
  Updating light colour
  */
  public void toggleCeilingLights() {
    float newDimness=dimness[currentDimness];
    Vec3 lightColour = new Vec3();
    lightColour.x = 1.6f * newDimness;
    lightColour.y = 1.6f * newDimness;
    lightColour.z = 1.6f * newDimness;
    // changing lights
    Material m = ceilingLights.get(1).getMaterial();
    for(int i=0; i<(ceilingLights.size()); i++){
      m = ceilingLights.get(i).getMaterial();
      m.setDiffuse(Vec3.multiply(lightColour,0.5f));
      m.setAmbient(Vec3.multiply(m.getDiffuse(),0.62f));
      ceilingLights.get(i).setMaterial(m);
    }
    currentDimness+=1;
    if(currentDimness>3){
      currentDimness=0;
    }
  }
  
  /**
   * For now a method to make it easier to notice lights!
   */
  private void updateLightColour() {
    double elapsedTime = getSeconds()-startTime;
    Vec3 lightColour = new Vec3();
    lightColour.x = (float)Math.sin(elapsedTime * 2.0f);
    lightColour.y = (float)Math.sin(elapsedTime * 0.7f);
    lightColour.z = (float)Math.sin(elapsedTime * 1.3f);
    Material m = lightBulb.getMaterial();
    m.setDiffuse(Vec3.multiply(lightColour,0.5f));
    m.setAmbient(Vec3.multiply(m.getDiffuse(),0.2f));
    lightBulb.setMaterial(m);
  }
/*
  private void updateRightArm() {
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = 180f+90f*(-(float)Math.sin(elapsedTime));
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rightArmRotate.update();
  }
  
  private void updateLeftArm() {
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = 180f+90f*(float)Math.sin(elapsedTime);
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    leftArmRotate.update();
  }
*/
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    /*
    //STATIC LIGHT
    float x = -3.5f;
    float y = 3f;
    float z = 1.5f;*/
    /*
    //Dynamic Light*/
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