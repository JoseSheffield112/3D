/**
 * TODO LIST
 * Need to add directional light class
 * need to change all other classes that depend on that
 * then you also need to make sure the fragment shaders are fine!
 */

import java.util.ArrayList;
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
    sunLight.dispose(gl);
    floor.dispose(gl);
    lightBulb.dispose(gl);
    lightBulb2.dispose(gl);
    lampLight.dispose(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  private Mat4 perspective;
  private Model floor, cube;
  private DirectionalLight sunLight;
  private PointLight lightBulb, lightBulb2;
  private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
  private SpotLight lampLight;

  private void initialise(GL3 gl) {
    createRandomNumbers();
	
	// loading tectures!
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    
    sunLight = new DirectionalLight(gl, 1f);
    lightBulb = new PointLight(gl, 1f);
    lightBulb2 = new PointLight(gl, 1f);
    // Messing with point lights
    lampLight = new SpotLight(gl, 0.5f);
    sunLight.setCamera(camera);
    lightBulb.setCamera(camera);
    lightBulb2.setCamera(camera);
    lampLight.setCamera(camera);
    // an array with the light
    ceilingLights.add(lightBulb);
    ceilingLights.add(lightBulb2);
	
    //Setting up model for our wall!
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    floor = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0);
    /***
     * cube test i guess
     */
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2,2,2), Mat4Transform.translate(0,0.5f,0));
    cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1, textureId2);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    sunLight.setPosition(new Vec3(0f,10f,0f));
    sunLight.render(gl);
    lightBulb.setPosition(new Vec3(-2f,-4f,0f));
    lightBulb.render(gl);
    lightBulb2.setPosition(new Vec3(2f,-4f,0f));
    lightBulb2.render(gl);
    updateLightColour();
    lampLight.setPosition(new Vec3(0f,4f,0f));
    lampLight.render(gl);
    floor.render(gl);
    cube.render(gl);
  }

  private void updatePointLightColour(){
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

  private void updateLightColour() {
    Material m = lampLight.getMaterial();
    m.setDiffuse(new Vec3(0.1f, 0.8f, 0.1f));
    m.setAmbient(new Vec3(0.1f, 0.1f, 0.1f));
    lampLight.setMaterial(m);
  }
  
  // The light's position is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition(int i) {
    double elapsedTime = getSeconds()-startTime;
    float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)))*i;
    float y = 2.7f;
    float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)))*i;
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