/**
 * This whole class was adapted from Dr. Maddocks code
 * 
 * Unlike previous files, I am using code from various sources to build these objects.. Each model will identify it's source 
 * 
 * *********************TO-DO*********************
 * - Build the lamp
 * - swing the lamp about
 * - build the robot
 * - add 5 distinct poses
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
    private Model cube, sphere;

    //TEMP
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;

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
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");

        //Sizes
        // plinth variables
        final float plinthWidth, plinthHeight, plinthDepth;
        plinthWidth = 4f;
        plinthHeight = 2f;
        plinthDepth = 1f;
        // phone
        final float phoneWidth, phoneHeight, phoneDepth;
        phoneWidth = 3f;
        phoneHeight = 6f;
        phoneDepth = 0.8f;
        // Egg + Platform variables
        final float platformSize = 2f;
        final float eggRadius = 2f;

        //Models
        //Setting up model for the cube used in the scene!
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1, textureId2);
        //Setting up model for the sphere used in the scenes
        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "vs_sphere.txt", "fs_sphere.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        sphere = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId3, textureId4);

        //Graph
        // Making graph stuff
        exhibitionRoot = new NameNode("Exhibition root node");

        // Nodes(objects)
        NameNode attractions = new NameNode("Building attractions");

        // First Build
        // Plinth
        NameNode plinthPhone = new NameNode("Plinth, then phone");
        // Centering the phone attraction
        Mat4 m = Mat4Transform.translate(4f, (plinthHeight*0.5f), -6f);
        TransformNode centeringPlinth = new TransformNode("Centering plinth & Phone", m);
        // Dealing with plinth
        m = Mat4Transform.scale(plinthWidth, plinthHeight, plinthDepth);
        TransformNode renderingPlinth = new TransformNode("Scaled", m);
        // Phone
        m = Mat4Transform.translate(0f, ((phoneHeight*0.5f)+(plinthHeight*0.5f)), 0f); // Gotta place the phone ON TOP of the plinth
        m = Mat4.multiply(m, Mat4Transform.scale(phoneWidth, phoneHeight, phoneDepth));
        TransformNode renderingPhone = new TransformNode("Scaled, then translated", m);

        // Second build
        // Platform
        NameNode platformEgg = new NameNode("Platform, then Egg");
        // Centering the egg attraction
        m = Mat4Transform.translate(-2f, (platformSize*0.25f), 2f);
        TransformNode centeringPlatform = new TransformNode("Centering Platform & Egg", m);
        // Dealing with platform
        m = Mat4Transform.scale(platformSize, (platformSize*0.5f), platformSize);
        TransformNode renderingPlatform = new TransformNode("Scaled", m);
        // Egg!
        m = Mat4Transform.translate(0f, ((platformSize*0.25f)+(eggRadius*1.25f)), 0f); // Gotta place the phone ON TOP of the plinth
        m = Mat4.multiply(m, Mat4Transform.scale(eggRadius, (eggRadius*2.5f), eggRadius));
        TransformNode renderingEgg = new TransformNode("Scaled, then translated", m);


        // Textures
        // Texturing the the plinth 
        ModelNode plinthTexture = new ModelNode("Plinth texture", cube);
        // Texturing the the Phone 
        ModelNode phoneTexture = new ModelNode("Phone texture", cube);

        // Texturing the the platform
        ModelNode platformTexture = new ModelNode("Platform texture", cube);
        // Texturing the egg!
        ModelNode eggTexture = new ModelNode("Egg texture", sphere);

        
        //Constructing scene graph
        exhibitionRoot.addChild(attractions);
            attractions.addChild(plinthPhone);
                plinthPhone.addChild(centeringPlinth);
                    centeringPlinth.addChild(renderingPlinth);
                        renderingPlinth.addChild(plinthTexture);
                    centeringPlinth.addChild(renderingPhone);
                        renderingPhone.addChild(phoneTexture);
            attractions.addChild(platformEgg);
                platformEgg.addChild(centeringPlatform);
                    centeringPlatform.addChild(renderingPlatform);
                        renderingPlatform.addChild(platformTexture);
                    centeringPlatform.addChild(renderingEgg);
                        renderingEgg.addChild(eggTexture);
        exhibitionRoot.update();  // IMPORTANT - don't forget this
        //exhibitionRoot.print(0, false);
        //System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return exhibitionRoot;
    }
}