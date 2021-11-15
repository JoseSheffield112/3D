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
  private DirectionalLight sunLight;
  private PointLight lightBulb, lightBulb2, lightBulb3, lightBulb4, lightBulb5, lightBulb6;
  private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
  private SpotLight lampLight;

  // Scene graph stuff
  private Robot myRobot;
  private Room theRoom;
  private Exhibition theExhibition;
  private Lamp theLamp;
  private SGNode roomScene = new NameNode("Museum - Root node");
  private SGNode roomChild = new NameNode("Museum - child node");

  // dimness setting for lights
  // Ceiling lights
  private static float ceilingLightsDimness[] = {0f,0.33f,0.66f,1f}; // different dimness settings for the lights
  private static int currentDimness = 1; // dimness used for the museum lights - they're fancy but expensive!
  // sunLight
  private static float dayLight[] = {0.1f, 0.6f};
  private static int currentCycle, oldCycle;
  // 3D positions for robot render
  private float xPosition = -5f;
  private float yPosition = 0f;
  private float zPosition = -6f;
  // 3D positions for lamp!
  private float defaultLampX = 4.6f;
  private float defaultLampY = 8.1f;  // There's a difference of 0.2f between both the lamp and lamp cover. 8.2f lamp flush with cover; 8.3f = lamp has 0.1f overlap with cover
  private float defaultLampZ = 0f;
  private float startAngle = 16, currentAngle=startAngle;
  // toggling lamp swinging speed
  private float speedToggle = 0.6f;
  
  
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
    currentCycle = oldCycle = 1;// Has there been a change?

    
    /**
     * Initialising all my lights!
     */
    // Setting the ceiling lights of the museum - these are directional lights (museum did this for maximum exhibition clarity :) )
    // code is wasteful, but it solves my need
    sunLight = new DirectionalLight(gl, dayLight[currentCycle]);
    lightBulb = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb2 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb3 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb4 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb5 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb6 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    // Messing with point lights
    lampLight = new SpotLight(gl, 0.5f);
    sunLight.setCamera(camera);
    lightBulb.setCamera(camera);
    lightBulb2.setCamera(camera);
    lightBulb3.setCamera(camera);
    lightBulb4.setCamera(camera);
    lightBulb5.setCamera(camera);
    lightBulb6.setCamera(camera);
    lampLight.setCamera(camera);
    // an array with the different ceiling lights
    ceilingLights.add(lightBulb);
    ceilingLights.add(lightBulb2);
    ceilingLights.add(lightBulb3);
    ceilingLights.add(lightBulb4);
    ceilingLights.add(lightBulb5);
    ceilingLights.add(lightBulb6);   
    
    //Loading up scene graphs!
    theRoom = new Room(gl, camera, sunLight, ceilingLights, lampLight);
    myRobot = new Robot(gl,camera, sunLight, ceilingLights, lampLight,  xPosition, yPosition, zPosition);
    theExhibition = new Exhibition(gl,camera, sunLight, ceilingLights, lampLight);
    theLamp = new Lamp(gl,camera, sunLight, ceilingLights, lampLight, startAngle);
   
    drawRoomScene(gl);

    // resetting values
    currentDimness+=1;
  }

  private void drawRoomScene(GL3 gl){
    // Constructing scene graph
    roomChild = theRoom.updateView(gl, currentCycle);
    roomScene.addChild(roomChild);
      roomChild.addChild(myRobot.getSceneGraph());// think you should put robot outside of this?
      roomChild.addChild(theExhibition.getSceneGraph());
      roomChild.addChild(theLamp.getSceneGraph());  
    roomScene.update();
    //roomScene.print(0, false);
    //System.exit(0);   
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    //For now rendering the lights - won't need to render them soon!
    sunLight.setPosition(new Vec3(-12f,16f,0f));
    sunLight.render(gl);
    /* Setting ceiling lights*/
    lightBulb.setPosition(new Vec3(-6f,11f,-6f));
    lightBulb.render(gl);
    lightBulb2.setPosition(new Vec3(0f,11f,-6f));
    lightBulb2.render(gl);
    lightBulb3.setPosition(new Vec3(6f,11f,-6f));
    lightBulb3.render(gl);
    lightBulb4.setPosition(new Vec3(-6f,11f,4f));
    lightBulb4.render(gl);
    lightBulb5.setPosition(new Vec3(0f,11f,4f));
    lightBulb5.render(gl);
    lightBulb6.setPosition(new Vec3(6f,11f,4f));
    lightBulb6.render(gl);
    updateLampPosition();
    if(oldCycle!=currentCycle){
      drawRoomScene(gl);
      oldCycle=currentCycle;
    }
    roomScene.draw(gl);
  }
  /*
  Updating Ceiling lights
  */
  public void toggleCeilingLights() {
    float newDimness=ceilingLightsDimness[currentDimness];
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
    currentDimness = (currentDimness==3) ? 0 : (currentDimness+1);
  }
  /**
   * Updating day/night light
   */
  
   public void toggleSunLight(){
     currentCycle=(currentCycle==1) ? 0 : 1;
     sunLight.setDefaultAmbient(dayLight[currentCycle]);
     sunLight.setDefaultDiffuseSpecular(dayLight[currentCycle]);
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
    Material m = lampLight.getMaterial();
    m.setDiffuse(Vec3.multiply(lightColour,0.5f));
    m.setAmbient(Vec3.multiply(m.getDiffuse(),0.2f));
    lampLight.setMaterial(m);
  }

  private void updateLampPosition(){
    // calculating angle
    double elapsedTime = getSeconds()-startTime;
    float newAngle = (startAngle*(float)Math.sin(elapsedTime*speedToggle))+180;
    // Fetching rotation matrix
    Mat4 rotationMatrix = theLamp.getRotationMatrix(newAngle);
    // Getting new Vec3 postion & setting it
    Vec3 newPosition = lampLight.calculateXRotation(rotationMatrix, defaultLampX, defaultLampY, defaultLampZ);
    System.out.println(newPosition);
    lampLight.setPosition(newPosition);
    // rotating lamp scene graph
    theLamp.updateAngle(rotationMatrix);
    roomScene.update();
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