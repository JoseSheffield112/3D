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
    private SGNode exhibitionRoot;

    // Declaring models variables
    private Model cube;

    //Transform node stuff
    private TransformNode enlargen;

    //TEMP
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
    //
    private float cubeSize = 4.0f;

    public Exhibition(GL3 gl,Camera camera, DirectionalLight sunLight, ArrayList<PointLight> ceilingLights, SpotLight lampLight){
        this.camera=camera;
        this.sunLight = sunLight;
        this.ceilingLights = ceilingLights;
        this.lampLight = lampLight;
        sceneGraph(gl);
    }

    private void sceneGraph(GL3 gl){
        //Texture
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        //Models
        //Setting up model for the cube used in the scene!
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1, textureId2);

        //Graph
        // Making graph stuff
        exhibitionRoot = new NameNode("Exhibition root node");

        //cube
        NameNode addingCube = new NameNode("Adding Cube");
        Mat4 m = Mat4Transform.scale(cubeSize,cubeSize,cubeSize);
        TransformNode enlargenCube = new TransformNode("Enlargening cube by ", m);
        m = Mat4Transform.translate(cubeSize, cubeSize, 0f);//x,y,z where z is right and y is to us
        TransformNode movingCube = new TransformNode("Translating about X and Y axis", m);

        // Texturing the the cube
        NameNode theCube = new NameNode("cube");      
        ModelNode cubeTexture = new ModelNode("Cube texture", cube);
        
        //Constructing scene graph
        exhibitionRoot.addChild(addingCube);
            addingCube.addChild(theCube);
                theCube.addChild(movingCube);
                    movingCube.addChild(enlargenCube);
                        enlargenCube.addChild(cubeTexture);
        exhibitionRoot.update();  // IMPORTANT - don't forget this
        //exhibitionRoot.print(0, false);
        //System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return exhibitionRoot;
    }
}