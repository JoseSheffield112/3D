/* I declare that this code is my own work */
/**
 * Author: Jose Alves
 * Email : jalves1@sheffield.ac.uk
 * Student # : 170163532
 */
/**
 * This whole class was adapted from Dr. Maddocks "M04_GLEventListener.java" 
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
  
  // Some initialisers
  private Camera camera;
  private Mat4 perspective;

  // Scene graph
  private Robot myRobot;
  private Room theRoom;
  private Exhibition theExhibition;
  private Lamp theLamp;
  private SGNode roomScene = new NameNode("Museum - Root node");
  private SGNode roomChild = new NameNode("Museum - child node");

  // Lights
  private DirectionalLight sunLight;
  private PointLight lightBulb, lightBulb2, lightBulb3, lightBulb4, lightBulb5, lightBulb6;
  private SpotLight lampLight;
  // dimness setting for lights
  // Sun light
  private static float dayLight[] = {0.1f, 0.6f};
  private static int currentCycle, oldCycle;
  // Ceiling lights
  private static float ceilingLightsDimness[] = {0f,0.33f,0.66f,1f};
  private static int currentDimness = 1;
  private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
  // Spot light (lamp)
  private static float spotlight[] = {0.0f, 0.6f};
  private static int currentSpotLight = 1;

  // Lamp
  // angle of swing
  private float startAngle = 12, currentAngle=startAngle;
  // toggling lamp swinging speed
  private float speedToggle = 0.6f;
  //TransformNode lampTopRotation
  TransformNode lampTopRotation, lampTopPoints;
  
  
  public Museum_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(18f,20f,18f));
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
   * I have added methods here
   * Used for the robot poses & toggling of lights
   */

  /**
   * Robot movements
   */
  private boolean animation = false;
  private double elapsedTime = 0;

  public void startAnimation() {
    animation = true;
  }
  public void stopAnimation() {
    animation = false;
  }

  public void poseOne() {
    stopAnimation();
    myRobot.poseOne();
  }

  public void poseTwo() {
    stopAnimation();
    myRobot.poseTwo();
  }

  public void poseThree() {
    startAnimation();
    myRobot.poseThree();
  }

  public void poseFour() {
    stopAnimation();
    myRobot.poseFour();
  }

  public void poseFive() {
    stopAnimation();
    myRobot.poseFive();
  }

  /**
   * Lights
   */

  /**
   * Toggling ceiling lights
   */
  public void toggleCeilingLights() {
    // Calculating light values
    float newDimness=ceilingLightsDimness[currentDimness];
    Vec3 lightColour = new Vec3();
    lightColour.x = 1.6f * newDimness;
    lightColour.y = 1.6f * newDimness;
    lightColour.z = 1.6f * newDimness;
    // Iterating over lights
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
   * Toggling day/night light
   */
  public void toggleSunLight(){
    currentCycle=(currentCycle==1) ? 0 : 1;
    sunLight.setDefaultAmbient(dayLight[currentCycle]);
    sunLight.setDefaultDiffuseSpecular(dayLight[currentCycle]);
  }

  /**
   * Toggling lamp light
   */
  public void toggleSpotlight(){
    // Calculating light values
    float newDimness=spotlight[currentSpotLight];
    Vec3 lightColour = new Vec3();
    lightColour.x = 1.6f * newDimness;
    lightColour.y = 1.6f * newDimness;
    lightColour.z = 1.6f * newDimness;
    // Setting values
    Material m = lampLight.getMaterial();
    m.setDiffuse(Vec3.multiply(lightColour,0.5f));
    m.setAmbient(Vec3.multiply(m.getDiffuse(),0.62f));
    lampLight.setMaterial(m);
    currentSpotLight = (currentSpotLight==1) ? 0 : (currentSpotLight+1);
  }  


  // ***************************************************
  /* THE SCENE
   */

  private void initialise(GL3 gl) {
    currentCycle = oldCycle = 1;// synchronising day cycle 
    /**
     * Initialising lights
     */
    // Initialising different light types
    sunLight = new DirectionalLight(gl, dayLight[currentCycle]);
    lightBulb = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb2 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb3 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb4 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb5 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lightBulb6 = new PointLight(gl, ceilingLightsDimness[currentDimness]);
    lampLight = new SpotLight(gl, spotlight[currentSpotLight]);
    // Setting camera
    sunLight.setCamera(camera);
    lightBulb.setCamera(camera);
    lightBulb2.setCamera(camera);
    lightBulb3.setCamera(camera);
    lightBulb4.setCamera(camera);
    lightBulb5.setCamera(camera);
    lightBulb6.setCamera(camera);
    lampLight.setCamera(camera);
    // Array list for celing lights
    ceilingLights.add(lightBulb);
    ceilingLights.add(lightBulb2);
    ceilingLights.add(lightBulb3);
    ceilingLights.add(lightBulb4);
    ceilingLights.add(lightBulb5);
    ceilingLights.add(lightBulb6);   
    
    //Loading up different scene graphs
    theRoom = new Room(gl, camera, sunLight, ceilingLights, lampLight);
    myRobot = new Robot(gl,camera, sunLight, ceilingLights, lampLight, -7f, -10f);
    theExhibition = new Exhibition(gl,camera, sunLight, ceilingLights, lampLight);
    theLamp = new Lamp(gl,camera, sunLight, ceilingLights, lampLight, startAngle);
   
    drawRoomScene(gl);

    // resetting light values
    currentDimness+=1;
    currentSpotLight=0;
  }

  /**
   * Draw scene graph - used to update texture of view
   */
  private void drawRoomScene(GL3 gl){
    // updating window wall 
    roomScene = theRoom.updateView(gl, currentCycle);
    // Constructing scene graph of whole museum
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
    // Positioning lights
    sunLight.setPosition(new Vec3(-24f,20f,0f));
    sunLight.render(gl);
    lightBulb.setPosition(new Vec3(-6f,11f,-6f));
    lightBulb2.setPosition(new Vec3(0f,11f,-6f));
    lightBulb3.setPosition(new Vec3(6f,11f,-6f));
    lightBulb4.setPosition(new Vec3(-6f,11f,4f));
    lightBulb5.setPosition(new Vec3(0f,11f,4f));
    lightBulb6.setPosition(new Vec3(6f,11f,4f));
    updateLampPosition();
    lampLight.render(gl);

    // Day change
    if(oldCycle!=currentCycle){
      drawRoomScene(gl);
      oldCycle=currentCycle;
    }

    // Robot animation
    if(animation){ 
      float rotateAngle = (60f-Math.abs(40f*(float)Math.sin(elapsedTime)));
      myRobot.updateRightArm(-1*rotateAngle); 
      myRobot.updateLeftArm(-1*rotateAngle); 
      myRobot.updateNose(-1*rotateAngle); 
      myRobot.updateTorso(elapsedTime);
    }
    roomScene.draw(gl);
  }

  /**
   * Method used to calculate & set lamp light position based on swing of the lamp
   */
  private void updateLampPosition(){
    elapsedTime = getSeconds()-startTime;
    float newAngle = (startAngle*(float)Math.sin(elapsedTime))+180;
    // Fetching rotation matrix
    lampTopRotation = theLamp.getRotationMatrix(newAngle);
    lampTopPoints = theLamp.getTopPoints();
    // Fetching new Vec3 postion & setting it
    lampLight.setPosition(lampLight.calculateXRotation(lampTopPoints.getTransformPoints(), lampTopRotation.getTransformMatrix()));
    // Setting (rotation) model in light class - makes lamp edges rotate
    lampLight.setModel2(lampTopRotation.getTransformMatrix());
    roomScene.update();
  }
  
  // ***************************************************
  /* TIME
   */

  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }  
}