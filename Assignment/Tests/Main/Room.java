/**
 * This whole class was adapted from Dr. Maddocks code from "M04_GLEventListener.java" class
 * Much like "Robot.java", I made this class so I could make the scene graph for the Museum room away from the Museum class
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
public class Room{
    /*
    variables used in class
    */
    private SGNode museumRoot;

    // Declaring models variables
    private Model floor, wall;

    //Transform node stuff
    private TransformNode enlargen;

    //TEMP
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
  
    //Setting Values
    private float wallSize = 16f;
    private float doorSize = wallSize*0.35f;
    private float doorPositioning = 0.75f;
    private Vec3 whiteLight = new Vec3(1.0f, 1.0f, 1.0f);

    public Room(GL3 gl, Camera camera, DirectionalLight sunLight, ArrayList<PointLight> ceilingLights, SpotLight lampLight){
        this.camera=camera;
        this.sunLight = sunLight;
        this.ceilingLights = ceilingLights;
        this.lampLight = lampLight;
        sceneGraph(gl);
    }

    private void sceneGraph(GL3 gl){
        //Texture
        int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/Floor.jpg");

        //Shapes models
        //Floor model
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
        Material material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
        floor = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0);

        //Scene graph
        //Root
        museumRoot = new NameNode("Room");
        //Building the fllor
        NameNode flooring = new NameNode("floor");      
            Mat4 m = Mat4Transform.scale(wallSize,1f,wallSize);
            TransformNode enlargen = new TransformNode("Enlargen the flooring", m);
            ModelNode flooringType = new ModelNode("Flooring", floor);

        //Constructing scene graph
        museumRoot.addChild(enlargen);
            enlargen.addChild(flooring);
                flooring.addChild(flooringType);
        museumRoot.update();  // IMPORTANT - don't forget this
        //museumRoot.print(0, false);
        //System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return museumRoot;
    }
}