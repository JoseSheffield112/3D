/**
 * This whole class was adapted from Dr. Maddocks code
 * 
 * Unlike previous files, I am using code from various sources to build these objects.. Each model will identify it's source 
 * 
 * *********************TO-DO*********************
 * - Identify methods you've introduced
 * IDENTIFY EACH MODELS SOURCE :P
*/
import gmaths.*;
import java.nio.*;
import java.util.ArrayList;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
public class Exhibition{
    /*
    variables used in class
    */
    private SGNode roomRoot;

    // Declaring models variables
    private Model floor, wall;

    //Transform node stuff
    private TransformNode enlargen;

    //TEMP
    private ArrayList<Light> lights = new ArrayList<Light>();  
    private Camera camera;
  
    //Setting Values
    private float wallSize = 16f;
    private float doorSize = wallSize*0.35f;
    private float doorPositioning = 0.75f;
    private Vec3 whiteLight = new Vec3(1.0f, 1.0f, 1.0f);

    public Exhibition(GL3 gl, ArrayList<Light> lights, Camera camera){
        this.lights = lights;
        this.camera=camera;
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
        floor = new Model(gl, camera, lights, shader, material, modelMatrix, mesh, textureId0);

        //Scene graph
        //Root
        roomRoot = new NameNode("Room");
        //Building the fllor
        NameNode flooring = new NameNode("floor");      
            Mat4 m = Mat4Transform.scale(wallSize,1f,wallSize);
            TransformNode enlargen = new TransformNode("Enlargen the flooring", m);
            ModelNode flooringType = new ModelNode("Flooring", floor);

        //Constructing scene graph
        roomRoot.addChild(enlargen);
            enlargen.addChild(flooring);
                flooring.addChild(flooringType);
        roomRoot.update();  // IMPORTANT - don't forget this
        //roomRoot.print(0, false);
        //System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return roomRoot;
    }
}