/**
 * This whole class was adapted from Dr. Maddocks code from "M04_GLEventListener.java" class
 * I have created it in order to construct my scene graph for my robot, away from the Museum class
 * 
 * *********************TO-DO*********************
 * - Identify methods you've introduced
*/
import gmaths.*;
import java.nio.*;
import java.util.ArrayList;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
public class Robot{
    /*
    variables used in class
    */
    private SGNode robotRoot;

    // Declaring models variables
    private Model sphere, cube, cube2;

    //TEMP
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
    //
    private float xPosition = 0;
    private float zPosition = 0;
    private TransformNode robotMoveTranslate, robotTranslate, torsoRotateX, torsoRotateY, torsoRotateZ, headRotate, leftArmRotateX, leftArmRotateY, rightArmRotateX, rightArmRotateY;
    private int position;

    public Robot(GL3 gl, Camera camera, DirectionalLight sunLight, ArrayList<PointLight> ceilingLights, SpotLight lampLight,float xPosition, float zPosition) {
        this.camera=camera;
        this.sunLight = sunLight;
        this.ceilingLights = ceilingLights;
        this.lampLight = lampLight;
        this.xPosition = xPosition;
        this.zPosition = zPosition;
        sceneGraph(gl);
    }

    /*
    *Interaction
    */

    public void updateRightArm(float rotateAngle) {
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
      rightArmRotateX.update();
    }
    
    public void updateLeftArm(float rotateAngle) {
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
      leftArmRotateX.update();
    }
    public void updateTorso(double elapsedTime){
      float rotateAngle = 0f+45f*(float)Math.sin(elapsedTime);    
      torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
      torsoRotateZ.update();
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(-90));
      robotTranslate.update();
    }

    private void resetRotations(){
      // head rotations
      headRotate.setTransform(Mat4Transform.rotateAroundX(0));
      headRotate.update();
      // Torso rotations
      torsoRotateX.setTransform(Mat4Transform.rotateAroundX(0));
      torsoRotateX.update();
      torsoRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      torsoRotateY.update();
      torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(0));
      torsoRotateZ.update();
      // Right arm rotations
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(0));
      rightArmRotateX.update();
      rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      rightArmRotateY.update();
      // Left Arm rotations
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(0));
      leftArmRotateX.update();
      leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      leftArmRotateY.update();
    }
  
    
    public void poseOne() {
      resetRotations();
      xPosition = -7f;
      zPosition = -10f;
      updateMove();
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(180));
      rightArmRotateX.update();
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(180));
      leftArmRotateX.update();
    }

    public void poseTwo() {
      resetRotations();
      xPosition = 1f;
      zPosition = -7f;
      updateMove();
      torsoRotateX.setTransform(Mat4Transform.rotateAroundX(-25));
      torsoRotateX.update();
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(160));
      robotTranslate.update();
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
    public void poseThree() {
      resetRotations();
      xPosition = 6f;
      zPosition = 0f;
      updateMove();
      headRotate.setTransform(Mat4Transform.rotateAroundX(20));
      headRotate.update();
    }

    public void poseFour() {
      xPosition = -2f;
      zPosition = 6f;
      updateMove();
      torsoRotateX.setTransform(Mat4Transform.rotateAroundX(0));
      torsoRotateX.update();
      torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(0));
      torsoRotateZ.update();
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(180));
      robotTranslate.update();
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(45));
      rightArmRotateX.update();
      rightArmRotateY.setTransform(Mat4Transform.rotateAroundZ(45));
      rightArmRotateY.update();
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(45));
      leftArmRotateX.update();
      leftArmRotateY.setTransform(Mat4Transform.rotateAroundZ(-45));
      leftArmRotateY.update();
    }

    public void poseFive () {
      resetRotations();
      xPosition = -7f;
      zPosition = 0f;
      updateMove();
      torsoRotateX.setTransform(Mat4Transform.rotateAroundX(45));
      torsoRotateX.update();
      headRotate.setTransform(Mat4Transform.rotateAroundX(20));
      headRotate.update();
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(60));
      rightArmRotateX.update();
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(60));
      leftArmRotateX.update();
    }
  
    private void updateMove() {
      robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,0,zPosition));
      robotMoveTranslate.update();
    }

    private void sceneGraph(GL3 gl){
        // loading textures
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/snow.jpg");
        // int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/snow_spec.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");
        
        // loading models
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "vs_sphere.txt", "fs_sphere.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        sphere = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1); // removed the specular - need to add one!
        
        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId3, textureId4);
        
        cube2 = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId5, textureId6); 
        
        // robot - scene graph construction
        
        // variables
        float footRadius = 3f;
        float torsoRadius = 2f;
        float neckRadius = 0.2f;
        float headRadius = 1.5f;
        float armScale = 0.1f;
        float armLength = 1.5f;
        
        robotRoot = new NameNode("root");
        // moving whole of robot in x axis & z axis
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(xPosition,0,zPosition));
        //Translating whole body above world floor
        robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));
        
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
        torsoRotateX = new TransformNode("upper body rotate about X",Mat4Transform.rotateAroundX(0));
        torsoRotateY = new TransformNode("upper body rotate about Y",Mat4Transform.rotateAroundY(0));
        torsoRotateZ = new TransformNode("upper body rotate about Y",Mat4Transform.rotateAroundZ(0));
        m = new Mat4(1);
        //m = Mat4Transform.translate(0,footRadius,0);
        m = Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius);//Mat4.multiply(m, Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius));
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
        TransformNode torsoTransform = new TransformNode("torso transform", m);
            ModelNode torsoShape = new ModelNode("Sphere(torso)", sphere);

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
            ModelNode leftArmShape = new ModelNode("Cube(left arm)", cube2);
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
            ModelNode rightArmShape = new ModelNode("Cube(right arm)", cube2);

        // neck
        NameNode neck = new NameNode("neck");
        headRotate = new TransformNode("Head rotation", Mat4Transform.rotateAroundX(0));
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(neckRadius,neckRadius,neckRadius));
        m = Mat4.multiply(m, Mat4Transform.translate(0,(torsoRadius+(neckRadius*4f)),0));//Raising body a further .5 above the legs top!
        TransformNode neckTransform = new TransformNode("neck transform", m);
            ModelNode neckShape = new ModelNode("Sphere(neck)", sphere);

        //head
        NameNode head = new NameNode("head");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0,neckRadius,0));//Raising body a further .5 above the legs top!
        m = Mat4Transform.translate(0,((neckRadius*0.8f)+(torsoRadius*0.5f)),0);
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
                  torsoTranslate.addChild(torsoRotateZ);
                    torsoRotateZ.addChild(torsoRotateX);
                      torsoRotateX.addChild(torsoTransform);
                        torsoTransform.addChild(torsoShape);
                        torsoTransform.addChild(leftarm);
                          leftarm.addChild(leftArmTranslate);
                            leftArmTranslate.addChild(leftArmRotateX);
                              leftArmRotateX.addChild(leftArmRotateY);
                                leftArmRotateY.addChild(leftArmScale);
                                  leftArmScale.addChild(leftArmShape);
                        torsoTransform.addChild(rightarm);
                          rightarm.addChild(rightArmTranslate);
                            rightArmTranslate.addChild(rightArmRotateX);
                              rightArmRotateX.addChild(rightArmRotateY);
                                rightArmRotateY.addChild(rightArmScale);
                                  rightArmScale.addChild(rightArmShape);
                        torsoTransform.addChild(headRotate);
                          headRotate.addChild(neck);
                            neck.addChild(neckTransform);
                              neckTransform.addChild(neckShape);
                            neck.addChild(head);
                              head.addChild(headTransform);
                                headTransform.addChild(headShape);
        robotRoot.update();  // IMPORTANT - don't forget this
        //robotRoot.print(0, false);
        //System.exit(0);
    }

    public SGNode getSceneGraph(){
        return robotRoot;
    }
}