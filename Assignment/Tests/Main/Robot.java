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
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
    private float xPosition, yPosition, zPosition;

    public Robot(GL3 gl, Camera camera, DirectionalLight sunLight, ArrayList<PointLight> ceilingLights, SpotLight lampLight,float xPosition, float yPosition, float zPosition) {
        this.camera=camera;
        this.sunLight = sunLight;
        this.ceilingLights = ceilingLights;
        this.lampLight = lampLight;
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
        Shader shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId3, textureId4);
        // same cube model, but different textures
        cube2 = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId5, textureId6); 

        //Sphere model
        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "vs_sphere.txt", "fs_sphere.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        sphere = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1, textureId2);

        // Scenegraph construction - Robot nodes
        // Root node for Robot scene graph
        robotRoot = new NameNode("robot root");
        // moving whole of robot in x axis
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(xPosition,yPosition,zPosition));
        //Translating whole body above world floor
        TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,legLength,0));


        // actual SG graph construction
        robotRoot.addChild(robotMoveTranslate);
        robotRoot.update();        
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