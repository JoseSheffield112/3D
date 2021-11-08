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

    //Transform node stuff
    private TransformNode translateX, robotMoveTranslate, leftArmRotate, rightArmRotate;

    //TEMP
    private ArrayList<Light> lights = new ArrayList<Light>();  
    private Camera camera;
    private float xPosition, yPosition, zPosition;

    public Robot(GL3 gl, ArrayList<Light> lights, Camera camera, float xPosition, float yPosition, float zPosition) {
        this.lights = lights;
        this.camera=camera;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
        sceneGraph(gl);
    }

    private void sceneGraph(GL3 gl){
        // variables used in body construction
        float bodyHeight = 1.5f;
        float bodyWidth = 1f;// x
        float bodyDepth = 0.5f;//z
        float headScale = 1f;
        float armLength = 1.75f;
        float armScale = 0.25f;
        float legLength = 1.75f;
        float legScale = 0.335f;

        //Loading all textures
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");

        // Shapes Models
        // A Cube model
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        cube = new Model(gl, camera, lights, shader, material, modelMatrix, mesh, textureId3, textureId4);
        // same cube model, but different textures
        cube2 = new Model(gl, camera, lights, shader, material, modelMatrix, mesh, textureId5, textureId6); 

        //Sphere model
        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        sphere = new Model(gl, camera, lights, shader, material, modelMatrix, mesh, textureId1, textureId2);

        // Scenegraph nodes
        // robot - scene graph construction
        // Root node for Robot scene graph
        robotRoot = new NameNode("robot");
        // moving whole of robot in x axis
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(xPosition,yPosition,zPosition));
        //Translating whole body above world floor
        TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,legLength,0));

        //Building the body
        NameNode body = new NameNode("body");
            Mat4 m = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
            TransformNode bodyTransform = new TransformNode("body transform", m);
            ModelNode bodyShape = new ModelNode("Cube(body)", cube);

        NameNode head = new NameNode("head"); 
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.translate(0,bodyHeight,0));
            m = Mat4.multiply(m, Mat4Transform.scale(headScale,headScale,headScale));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode headTransform = new TransformNode("head transform", m);
            ModelNode headShape = new ModelNode("Sphere(head)", sphere);
      
        NameNode leftarm = new NameNode("left arm");
            TransformNode leftArmTranslate = new TransformNode("leftarm translate", 
                                                Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
            leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode leftArmScale = new TransformNode("leftarm scale", m);
            ModelNode leftArmShape = new ModelNode("Cube(left arm)", cube2);
        
        NameNode rightarm = new NameNode("right arm");
            TransformNode rightArmTranslate = new TransformNode("rightarm translate", 
                                                Mat4Transform.translate(-(bodyWidth*0.5f)-(armScale*0.5f),bodyHeight,0));
            rightArmRotate = new TransformNode("rightarm rotate",Mat4Transform.rotateAroundX(180));
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode rightArmScale = new TransformNode("rightarm scale", m);
            ModelNode rightArmShape = new ModelNode("Cube(right arm)", cube2);
            
        NameNode leftleg = new NameNode("left leg");
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.translate((bodyWidth*0.5f)-(legScale*0.5f),0,0));
            m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
            m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode leftlegTransform = new TransformNode("leftleg transform", m);
            ModelNode leftLegShape = new ModelNode("Cube(leftleg)", cube);
        
        NameNode rightleg = new NameNode("right leg");
            m = new Mat4(1);
            m = Mat4.multiply(m, Mat4Transform.translate(-(bodyWidth*0.5f)+(legScale*0.5f),0,0));
            m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
            m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
            m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
            TransformNode rightlegTransform = new TransformNode("rightleg transform", m);
            ModelNode rightLegShape = new ModelNode("Cube(rightleg)", cube);

        //Constructing scene graph
        robotRoot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(robotTranslate);
                robotTranslate.addChild(body);
                    body.addChild(bodyTransform);
                        bodyTransform.addChild(bodyShape);
                    body.addChild(head);
                        head.addChild(headTransform);
                            headTransform.addChild(headShape);
                    body.addChild(leftarm);
                        leftarm.addChild(leftArmTranslate);
                            leftArmTranslate.addChild(leftArmRotate);
                                leftArmRotate.addChild(leftArmScale);
                                    leftArmScale.addChild(leftArmShape);
                    body.addChild(rightarm);
                        rightarm.addChild(rightArmTranslate);
                            rightArmTranslate.addChild(rightArmRotate);
                                rightArmRotate.addChild(rightArmScale);
                                    rightArmScale.addChild(rightArmShape);
                    body.addChild(leftleg);
                        leftleg.addChild(leftlegTransform);
                            leftlegTransform.addChild(leftLegShape);
                    body.addChild(rightleg);
                        rightleg.addChild(rightlegTransform);
                            rightlegTransform.addChild(rightLegShape);
      
        robotRoot.update();  // IMPORTANT - don't forget this
        //robotRoot.print(0, false);
        //System.exit(0);        
    }

    public void updateMoveX(float newxPosition) {
        xPosition = newxPosition;
        robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,yPosition,zPosition));
        robotMoveTranslate.update();
    }

    public void updateMoveY(float newYPosition) {
        yPosition = newYPosition;
        robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,yPosition,zPosition));
        robotMoveTranslate.update();
    }

    public void updateMoveZ(float newZPosition) {
        zPosition = newZPosition;
        robotMoveTranslate.setTransform(Mat4Transform.translate(xPosition,yPosition,zPosition));
        robotMoveTranslate.update();
    }

    public SGNode getSceneGraph(){
        return robotRoot;
    }
}