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
    animation = false;
    xPosition = 0f;
    zPosition = 0f;
    updateMove();
    torsoRotateX.setTransform(Mat4Transform.rotateAroundX(0));
    torsoRotateX.update();
    torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(0));
    torsoRotateZ.update();
    headRotate.setTransform(Mat4Transform.rotateAroundX(0));
    headRotate.update();
    rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(180));
    rightArmRotateX.update();
    rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
    rightArmRotateY.update();
    leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(180));
    leftArmRotateX.update();
    leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
    leftArmRotateY.update();
  }

  public void poseTwo() {
    animation = false;
    xPosition = -8f;
    zPosition = -8f;
    updateMove();
    torsoRotateX.setTransform(Mat4Transform.rotateAroundX(45));
    torsoRotateX.update();
    torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(0));
    torsoRotateZ.update();
    headRotate.setTransform(Mat4Transform.rotateAroundX(20));
    headRotate.update();
    rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(60));
    rightArmRotateX.update();
    rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
    rightArmRotateY.update();
    leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(60));
    leftArmRotateX.update();
    leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
    leftArmRotateY.update();
  }
    
  public void poseThree() {
    animation = false;
    xPosition = -8f;
    zPosition = 8f;
    updateMove();
    torsoRotateX.setTransform(Mat4Transform.rotateAroundX(-45));
    torsoRotateX.update();
    torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(0));
    torsoRotateZ.update();
    headRotate.setTransform(Mat4Transform.rotateAroundX(-20));
    headRotate.update();
    rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(45));
    rightArmRotateX.update();
    rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(90));
    rightArmRotateY.update();
    leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(45));
    leftArmRotateX.update();
    leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(90));
    leftArmRotateY.update();
  }
     
  public void poseFour() {
    animation = false;
    xPosition = 8f;
    zPosition = -8f;
    updateMove();
    torsoRotateX.setTransform(Mat4Transform.rotateAroundX(0));
    torsoRotateX.update();
    torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(-45));
    torsoRotateZ.update();
    rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(0));
    rightArmRotateX.update();
    rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(90));
    rightArmRotateY.update();
    leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(0));
    leftArmRotateX.update();
    leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(90));
    leftArmRotateY.update();
  }
     
  public void poseFive() {
    animation = true;
    xPosition = 8f;
    zPosition = 8f;
    updateMove();
    torsoRotateX.setTransform(Mat4Transform.rotateAroundX(0));
    torsoRotateX.update();
  }

  private void updateMove() {
    robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,0,zPosition));
    robotMoveTranslate.update();
  }
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model floor, sphere, eyeball, cube, cube2;
  private Light light;
  private SGNode robotRoot;
  
  private float xPosition = 0;
  private float zPosition = 0;
  private TransformNode robotMoveTranslate, torsoRotateX, torsoRotateZ, headRotate, leftArmRotateX, leftArmRotateY, rightArmRotateX, rightArmRotateY, leftEyeTransform, rightEyeTransform, hatRotate, noseRotate;
  private int position;

  private void initialise(GL3 gl) {
    createRandomNumbers();
	// loading textures
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/snow.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/snow_spec.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
        
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
    sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId1, textureId2); // removed the specular - need to add one!
    eyeball = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId7, textureId8); // removed the specular - need to add one!
    

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
    float neckRadius = 0.2f;
    float headRadius = 1.5f;
    float armScale = 0.1f;
    float armLength = 1.5f;
    float eyesRadius = 0.2f;
    float hatSize = 1f;
    float noseSize = 0.25f;
    
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
        ModelNode footTexture = new ModelNode("Sphere(Foot)", sphere);

    // torso
    NameNode torso = new NameNode("torso");
      TransformNode torsoTranslate = new TransformNode("torso translation",
                                        Mat4Transform.translate(0,footRadius,0));
      torsoRotateX = new TransformNode("upper body rotate about X",Mat4Transform.rotateAroundX(0));
      torsoRotateZ = new TransformNode("upper body rotate about Y",Mat4Transform.rotateAroundY(0));
      m = new Mat4(1);
      //m = Mat4Transform.translate(0,footRadius,0);
      m = Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius);//Mat4.multiply(m, Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
      TransformNode torsoTransform = new TransformNode("torso transform", m);
        ModelNode torsoTexture = new ModelNode("Sphere(torso)", sphere);

    // left arm
    NameNode leftarm = new NameNode("left arm");
      TransformNode leftArmTranslate = new TransformNode("leftarm translate", 
                                           Mat4Transform.translate((torsoRadius*0.27f),0,0));
      leftArmRotateX = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));
      leftArmRotateY = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundY(0));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode leftArmScale = new TransformNode("leftarm scale", m);
        ModelNode leftArmTexture = new ModelNode("Cube(left arm)", cube2);
    // right arm
    NameNode rightarm = new NameNode("right arm");
      TransformNode rightArmTranslate = new TransformNode("rightarm translate", 
                                            Mat4Transform.translate(-(torsoRadius*0.27f),0,0));
      rightArmRotateX = new TransformNode("rightarm rotate",Mat4Transform.rotateAroundX(180));
      rightArmRotateY = new TransformNode("rightarm rotate",Mat4Transform.rotateAroundY(0));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode rightArmScale = new TransformNode("rightarm scale", m);
        ModelNode rightArmTexture = new ModelNode("Cube(right arm)", cube2);

    // neck
    NameNode neck = new NameNode("neck");
      headRotate = new TransformNode("Head rotation", Mat4Transform.rotateAroundX(0));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(neckRadius,neckRadius,neckRadius));
      m = Mat4.multiply(m, Mat4Transform.translate(0,(torsoRadius+(neckRadius*4f)),0));//Raising body a further .5 above the legs top!
      TransformNode neckTransform = new TransformNode("neck transform", m);
        ModelNode neckTexture = new ModelNode("Sphere(neck)", sphere);

    //head
    NameNode head = new NameNode("head");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,(neckRadius+(torsoRadius*0.5f)),0));//Raising body a further .5 above the legs top!
      TransformNode headTranslate = new TransformNode("head transform", m);
      m = new Mat4(1);
      m = Mat4Transform.translate(0,(neckRadius*0.8f),0);
      TransformNode headTransform = new TransformNode("head transform", m);
        ModelNode headTexture = new ModelNode("Sphere(head)", sphere);
 
    // Hat
    // Hat base
    NameNode hatBase = new NameNode("hat");
      hatRotate = new TransformNode("Hat stuff", Mat4Transform.rotateAroundX(0));
      m = new Mat4(1);
      m = Mat4Transform.translate(0,(headRadius*0.5f),0);
      m = Mat4.multiply(m, Mat4Transform.scale((hatSize*1.5f),(hatSize*0.5f),hatSize));
      TransformNode hatTransform = new TransformNode("hat scaled", m);
      ModelNode hatTexture = new ModelNode("hat (cube)", cube2);
    // Hat top
    NameNode hatTop = new NameNode("hat");
      m = new Mat4(1);
      m = Mat4Transform.translate(0,1.5f,0);
      m = Mat4.multiply(m, Mat4Transform.scale(hatSize,(hatSize*1.5f),hatSize));
      TransformNode hatTopTransform = new TransformNode("hat scaled", m);
      ModelNode hatTopTexture = new ModelNode("hat (cube)", sphere);

    // Nose
    NameNode nose = new NameNode("Nose");
      m = new Mat4(1);
      m = Mat4Transform.translate(0,0,(headRadius));
      TransformNode noseTranslate = new TransformNode("Nose scaled", m);
      noseRotate = new TransformNode("Nose rotation", Mat4Transform.rotateAroundX(-90));
      m = Mat4Transform.scale(noseSize,(noseSize*1.5f),(noseSize*0.5f));
      TransformNode noseScale = new TransformNode("Nose scaled", m);
      ModelNode noseTexture = new ModelNode("Nose (sphere)", sphere);

    // Two Eyes - LEFT + RIGHT
    NameNode eyes = new NameNode("Eyes");
    // left
    NameNode leftEye = new NameNode("Left eye");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate((headRadius*0.15f),(headRadius*0.2f),(headRadius*0.3f)));//Raising body a further .5 above the legs top!
      TransformNode leftEyeTranslate = new TransformNode("Translating eyes", m);
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale((eyesRadius),(eyesRadius),(eyesRadius*0.6f)));
      TransformNode leftEyeScale = new TransformNode("Left eye Scale", m);
      leftEyeTransform = new TransformNode("Left eye transform", Mat4Transform.rotateAroundX(0));
      ModelNode leftEyeTexture = new ModelNode("Sphere(eye)", eyeball);
    // right
    NameNode rightEye = new NameNode("Right eye");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(-(headRadius*0.15f),(headRadius*0.2f),(headRadius*0.3f)));//Raising body a further .5 above the legs top!
      TransformNode rightEyeTranslate = new TransformNode("Translating eyes", m);
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale((eyesRadius),(eyesRadius),(eyesRadius*0.6f)));
      TransformNode rightEyeScale = new TransformNode("Right eye Scale", m);
      rightEyeTransform = new TransformNode("Right eye transform", Mat4Transform.rotateAroundX(0));
      ModelNode rightEyeTexture = new ModelNode("Sphere(eye)", eyeball);
    
    // scene  graph
    robotRoot.addChild(robotMoveTranslate);
      robotMoveTranslate.addChild(robotTranslate);
        robotTranslate.addChild(foot);
          foot.addChild(footTransform);
            footTransform.addChild(footTexture);
          foot.addChild(torso);
            torso.addChild(torsoTranslate);
              torsoTranslate.addChild(torsoRotateZ);
                torsoRotateZ.addChild(torsoRotateX);
                  torsoRotateX.addChild(torsoTransform);
                    torsoTransform.addChild(torsoTexture);
                    torsoTransform.addChild(leftarm);
                      leftarm.addChild(leftArmTranslate);
                        leftArmTranslate.addChild(leftArmRotateX);
                          leftArmRotateX.addChild(leftArmRotateY);
                            leftArmRotateY.addChild(leftArmScale);
                              leftArmScale.addChild(leftArmTexture);
                    torsoTransform.addChild(rightarm);
                      rightarm.addChild(rightArmTranslate);
                        rightArmTranslate.addChild(rightArmRotateX);
                          rightArmRotateX.addChild(rightArmRotateY);
                            rightArmRotateY.addChild(rightArmScale);
                              rightArmScale.addChild(rightArmTexture);
                    torsoTransform.addChild(headRotate);
                      headRotate.addChild(neck);
                        neck.addChild(neckTransform);
                          neckTransform.addChild(neckTexture);
                        neck.addChild(head);
                          head.addChild(headTranslate);
                            headTranslate.addChild(headTransform);
                              headTransform.addChild(headTexture);
                            headTranslate.addChild(nose);
                              nose.addChild(noseTranslate);
                                noseTranslate.addChild(noseRotate);
                                  noseRotate.addChild(noseScale);
                                    noseScale.addChild(noseTexture);
                            headTranslate.addChild(hatBase);
                              hatBase.addChild(hatRotate);
                                hatRotate.addChild(hatTransform);
                                  hatTransform.addChild(hatTexture);
                                hatRotate.addChild(hatTop);
                                  hatTop.addChild(hatTopTransform);
                                    hatTopTransform.addChild(hatTopTexture);
                            headTranslate.addChild(eyes);
                              eyes.addChild(leftEyeTranslate);
                                leftEyeTranslate.addChild(leftEye);
                                    leftEye.addChild(leftEyeScale);
                                      leftEyeScale.addChild(leftEyeTransform);
                                        leftEyeTransform.addChild(leftEyeTexture);
                              eyes.addChild(rightEyeTranslate);
                                rightEyeTranslate.addChild(rightEye);
                                  rightEye.addChild(rightEyeScale);
                                    rightEyeScale.addChild(rightEyeTransform);
                                      rightEyeTransform.addChild(rightEyeTexture);

    robotRoot.update();  // IMPORTANT - don't forget this
    // robotRoot.print(0, false);
    // System.exit(0);
  }

  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(getLightPosition());  // changing light position each frame
    light.render(gl);
    floor.render(gl); 
    robotRoot.draw(gl);
    // with cos it rotates arms in a circle
    if(animation){ updateRightArm(); updateLeftArm(); updateTorso();}
  }

  private void updateRightArm() {
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = (60f-Math.abs(22.5f*(float)Math.sin(elapsedTime)));
    rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rightArmRotateX.update();
  }
  
  private void updateLeftArm() {
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = (60f-Math.abs(22.5f*(float)Math.sin(elapsedTime)));
    System.out.println(rotateAngle);
    leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    leftArmRotateX.update();
  }

  private void updateTorso(){
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = 0f+45f*(float)Math.sin(elapsedTime);    
    torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
    torsoRotateZ.update();
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