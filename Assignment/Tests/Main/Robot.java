/* I declare that this code is my own work */
/**
 * Author: Jose Alves
 * Email : jalves1@sheffield.ac.uk
 * Student # : 170163532
 */
/**
 * This whole class used code from various of Dr.Maddocks 3D tutorials
 * It was mainly inspired from Dr.Maddocks Lab 7/7_2 tutorial "Robot.java"
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
    
    //variables used in class
    private SGNode robotRoot;

    // Declaring model variables
    private Model sphere, eyeball, snowBall, carrot, cube, cube2;

    // Lights
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
    
    // Position variables
    private float xPosition = 0;
    private float zPosition = 0;

    // Transformations
    private TransformNode robotMoveTranslate, robotTranslate, torsoRotateX, torsoRotateY, torsoRotateZ, headRotateX, headRotateY, leftArmRotateX, leftArmRotateY, rightArmRotateX, rightArmRotateY, leftEyeTransform, rightEyeTransform, hatRotate, noseRotate;

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
    * Interaction Methods
    */

    // Animation methods
    public void updateRightArm(float rotateAngle) {
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
      rightArmRotateX.update();
    }
    
    public void updateLeftArm(float rotateAngle) {
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
      leftArmRotateX.update();
    }

    public void updateNose(float rotateAngle){
      noseRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
      noseRotate.update();
    }

    public void updateTorso(double elapsedTime){
      float rotateAngle = 0f+45f*(float)Math.sin(elapsedTime);    
      torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
      torsoRotateZ.update();
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(-90));
      robotTranslate.update();
    }

    // Clearing rotations
    private void resetRotations(){
      // whole robot rotations
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(0));
      robotTranslate.update();
      // Torso rotations
      torsoRotateX.setTransform(Mat4Transform.rotateAroundX(0));
      torsoRotateX.update();
      torsoRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      torsoRotateY.update();
      torsoRotateZ.setTransform(Mat4Transform.rotateAroundZ(0));
      torsoRotateZ.update();
      // Right arm rotations
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(180));
      rightArmRotateX.update();
      rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      rightArmRotateY.update();
      // Left Arm rotations
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(180));
      leftArmRotateX.update();
      leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      leftArmRotateY.update();
      // head rotations
      headRotateX.setTransform(Mat4Transform.rotateAroundX(0));
      headRotateX.update();
      headRotateY.setTransform(Mat4Transform.rotateAroundY(0));
      headRotateY.update();
      // eyes
      leftEyeTransform.setTransform(Mat4Transform.rotateAroundX(0));
      leftEyeTransform.update();
      leftEyeTransform.setTransform(Mat4Transform.rotateAroundZ(0));
      leftEyeTransform.update();
      rightEyeTransform.setTransform(Mat4Transform.rotateAroundX(0));
      rightEyeTransform.update();
      rightEyeTransform.setTransform(Mat4Transform.rotateAroundZ(0));
      rightEyeTransform.update();
      // nose
      noseRotate.setTransform(Mat4Transform.rotateAroundX(-180));
      noseRotate.update();
      noseRotate.setTransform(Mat4Transform.rotateAroundZ(0));
      noseRotate.update();
      // hat
      hatRotate.setTransform(Mat4Transform.rotateAroundX(0));
      hatRotate.update();
      hatRotate.setTransform(Mat4Transform.rotateAroundY(0));
      hatRotate.update();
    }

    // Different poses
    public void poseOne() {
      resetRotations();
      xPosition = -7f;
      zPosition = -10f;
      updateMove();
    }

    public void poseTwo() {
      resetRotations();
      xPosition = 1f;
      zPosition = -7f;
      updateMove();
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(160));
      robotTranslate.update();
      torsoRotateX.setTransform(Mat4Transform.rotateAroundX(-25));
      torsoRotateX.update();
      headRotateY.setTransform(Mat4Transform.rotateAroundX(-20));
      headRotateY.update();
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(45));
      rightArmRotateX.update();
      rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(90));
      rightArmRotateY.update();
      leftArmRotateX.setTransform(Mat4Transform.rotateAroundX(45));
      leftArmRotateX.update();
      leftArmRotateY.setTransform(Mat4Transform.rotateAroundY(90));
      leftArmRotateY.update();
      leftEyeTransform.setTransform(Mat4Transform.rotateAroundX(-45));
      leftEyeTransform.update();
      rightEyeTransform.setTransform(Mat4Transform.rotateAroundX(-45));
      rightEyeTransform.update();
      hatRotate.setTransform(Mat4Transform.rotateAroundX(-60));
      hatRotate.update();
    }

    public void poseThree() {
      resetRotations();
      xPosition = 6f;
      zPosition = 0f;
      updateMove();
      headRotateY.setTransform(Mat4Transform.rotateAroundY(180));
      headRotateY.update();
      headRotateX.setTransform(Mat4Transform.rotateAroundX(20));
      headRotateX.update();
    }

    public void poseFour() {
      resetRotations();
      xPosition = -2f;
      zPosition = 6f;
      updateMove();
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
      leftEyeTransform.setTransform(Mat4Transform.rotateAroundX(45));
      leftEyeTransform.update();
      rightEyeTransform.setTransform(Mat4Transform.rotateAroundX(45));
      rightEyeTransform.update();
      noseRotate.setTransform(Mat4Transform.rotateAroundZ(-40));
      noseRotate.update();
      hatRotate.setTransform(Mat4.multiply(Mat4Transform.rotateAroundZ(-30), Mat4Transform.rotateAroundX(-30)));
      hatRotate.update();
    }

    public void poseFive () {
      resetRotations();
      xPosition = -7f;
      zPosition = 2f;
      updateMove();      
      robotTranslate.setTransform(Mat4Transform.rotateAroundY(-90));
      robotTranslate.update();
      torsoRotateX.setTransform(Mat4Transform.rotateAroundY(-35));
      torsoRotateX.update();
      rightArmRotateX.setTransform(Mat4Transform.rotateAroundX(60));
      rightArmRotateX.update();      
      rightArmRotateY.setTransform(Mat4Transform.rotateAroundY(45));
      rightArmRotateY.update();
      headRotateX.setTransform(Mat4Transform.rotateAroundX(-15));
      headRotateX.update();
      hatRotate.setTransform(Mat4Transform.rotateAroundX(-30));
      hatRotate.update();
    }
  
    private void updateMove() {
      robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,0,zPosition));
      robotMoveTranslate.update();
    }

    private void sceneGraph(GL3 gl){
        // loading textures
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/hat_nose_texture.jpg");
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/hat_nose_spec.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/snow.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/snow_spec.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/wood_texture.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wood_spec.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/nose_texture.jpg");
        int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/nose_spec.jpg");
    
        // loading models
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader = new Shader(gl, "vs_sphere.txt", "fs_sphere.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        sphere = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0, textureId1); // removed the specular - need to add one!
        eyeball = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0, textureId1); // removed the specular - need to add one!
        carrot = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId6, textureId7); // removed the specular - need to add one!
        snowBall = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId2, textureId3); // removed the specular - need to add one!
        
        mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0, textureId1);
        cube2 = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId4, textureId5); 
        
        // robot - scene graph construction
        
        // Size variables
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
        robotMoveTranslate = new TransformNode("Translation robot",Mat4Transform.translate(xPosition,0,zPosition));
        // Transform node used by interaction methods
        robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));
        
        //Building the body
        //Foot
        NameNode foot = new NameNode("Foot");
          Mat4 m = Mat4Transform.scale(footRadius,footRadius,footRadius);
          m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          TransformNode footTransform = new TransformNode("Scaled then translated", m);
          ModelNode footShape = new ModelNode("Sphere(Foot)", snowBall);

        // torso
        NameNode torso = new NameNode("torso");
          TransformNode torsoTranslate = new TransformNode("torso translation",
                                              Mat4Transform.translate(0,footRadius,0));
          torsoRotateX = new TransformNode("upper body rotate about X",Mat4Transform.rotateAroundX(0));
          torsoRotateY = new TransformNode("upper body rotate about Y",Mat4Transform.rotateAroundY(0));
          torsoRotateZ = new TransformNode("upper body rotate about Y",Mat4Transform.rotateAroundZ(0));
          m = new Mat4(1);
          m = Mat4Transform.scale(torsoRadius,torsoRadius,torsoRadius);
          m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          TransformNode torsoTransform = new TransformNode("Scaled then translated", m);
          ModelNode torsoShape = new ModelNode("Sphere(torso)", snowBall);

        // left arm
        NameNode leftarm = new NameNode("left arm");
          TransformNode leftArmTranslate = new TransformNode("leftarm translate", 
                                              Mat4Transform.translate((torsoRadius*0.27f),0,0));
          leftArmRotateX = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));
          leftArmRotateY = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundY(0));
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
          m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
          TransformNode leftArmScale = new TransformNode("Scaled then translated", m);
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
          TransformNode rightArmScale = new TransformNode("Scaled then translated", m);
          ModelNode rightArmShape = new ModelNode("Cube(right arm)", cube2);

        // neck
        NameNode neck = new NameNode("neck");
          headRotateY = new TransformNode("Head rotation about Y", Mat4Transform.rotateAroundY(0));
          headRotateX = new TransformNode("Head rotation about X", Mat4Transform.rotateAroundX(0));
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.scale(neckRadius,neckRadius,neckRadius));
          m = Mat4.multiply(m, Mat4Transform.translate(0,(torsoRadius+(neckRadius*4f)),0));
          TransformNode neckTransform = new TransformNode("Scaled then translated", m);
          ModelNode neckShape = new ModelNode("Sphere(neck)", snowBall);

        //head
        NameNode head = new NameNode("head");
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.translate(0,((neckRadius*0.8f)+(torsoRadius*0.5f)),0));
          TransformNode headTranslate = new TransformNode("head translated", m);
          ModelNode headShape = new ModelNode("Sphere(head)", snowBall);
        
        // Hat
        // Hat base
        NameNode hatBase = new NameNode("hat");
          hatRotate = new TransformNode("Hat stuff", Mat4Transform.rotateAroundX(0));
          m = new Mat4(1);
          m = Mat4Transform.translate(0,(headRadius*0.5f),0);
          m = Mat4.multiply(m, Mat4Transform.scale((hatSize*1.5f),(hatSize*0.5f),hatSize));
          TransformNode hatTransform = new TransformNode("translated then scaled", m);
          ModelNode hatTexture = new ModelNode("hat (cube)", cube);

        // Hat top
        NameNode hatTop = new NameNode("hat");
          m = new Mat4(1);
          m = Mat4Transform.translate(0,1.5f,0);
          m = Mat4.multiply(m, Mat4Transform.scale(hatSize,(hatSize*1.5f),hatSize));
          TransformNode hatTopTransform = new TransformNode("translated then scaled", m);
          ModelNode hatTopTexture = new ModelNode("hat (cube)", sphere);

        // Nose
        NameNode nose = new NameNode("Nose");
          m = new Mat4(1);
          m = Mat4Transform.translate(0,(headRadius*0.05f),(headRadius*0.35f));
          TransformNode noseTranslate = new TransformNode("Nose Translated", m);
          noseRotate = new TransformNode("Nose rotation", Mat4Transform.rotateAroundX(-180));
          m = Mat4Transform.scale(noseSize,(noseSize*1.5f),(noseSize*0.5f));
          TransformNode noseScale = new TransformNode("Nose scaled", m);
          ModelNode noseTexture = new ModelNode("Nose (sphere)", carrot);

        // Two Eyes - LEFT + RIGHT
        NameNode eyes = new NameNode("Eyes");
        // left
        NameNode leftEye = new NameNode("Left eye");
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.translate((headRadius*0.15f),(headRadius*0.2f),(headRadius*0.3f)));//Raising body a further .5 above the legs top!
          TransformNode leftEyeTranslate = new TransformNode("Translating left eye", m);
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.scale((eyesRadius),(eyesRadius),(eyesRadius*0.6f)));
          TransformNode leftEyeScale = new TransformNode("Left eye Scale", m);
          leftEyeTransform = new TransformNode("Left eye transform", Mat4Transform.rotateAroundX(0));
          ModelNode leftEyeTexture = new ModelNode("Sphere(eye)", eyeball);
        // right
        NameNode rightEye = new NameNode("Right eye");
          m = new Mat4(1);
          m = Mat4.multiply(m, Mat4Transform.translate(-(headRadius*0.15f),(headRadius*0.2f),(headRadius*0.3f)));//Raising body a further .5 above the legs top!
          TransformNode rightEyeTranslate = new TransformNode("Translating right eye", m);
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
                        torsoTransform.addChild(headRotateX);
                          headRotateX.addChild(headRotateY);
                            headRotateY.addChild(neck);
                              neck.addChild(neckTransform);
                                neckTransform.addChild(neckShape);
                              neck.addChild(head);
                                head.addChild(headTranslate);
                                  headTranslate.addChild(headShape);
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
                                          leftEye.addChild(leftEyeTransform);
                                            leftEyeTransform.addChild(leftEyeScale);
                                              leftEyeScale.addChild(leftEyeTexture);
                                    eyes.addChild(rightEyeTranslate);
                                      rightEyeTranslate.addChild(rightEye);
                                        rightEye.addChild(rightEyeTransform);
                                          rightEyeTransform.addChild(rightEyeScale);
                                            rightEyeScale.addChild(rightEyeTexture);
        robotRoot.update();  // IMPORTANT - don't forget this
        //robotRoot.print(0, false);
        //System.exit(0);
    }

    public SGNode getSceneGraph(){
        return robotRoot;
    }
}