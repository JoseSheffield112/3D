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
        final float width, depth, height;
        width = 3.4f;
        depth = 1.4f;
        height = 0.8f;
        // Egg + Platform variables
        final float size = 3f;
        final float radius = 2.2f;

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
        Mat4 m = Mat4Transform.scale(width, height, depth);
        m = Mat4.multiply(m, Mat4Transform.translate(1f, 0.5f, -4f));
        TransformNode renderingPlinth = new TransformNode("Scaled, then translated", m);
        // Phone
        m = Mat4Transform.scale((width*0.8f), (height*7f), (depth*0.6f));
        m = Mat4.multiply(m, Mat4Transform.translate((width*0.37f), (height*0.8f), -(depth*4.75f)));
        TransformNode renderingPhone = new TransformNode("Scaled, then translated", m);

        // Second build
        // Platform
        NameNode platformEgg = new NameNode("Platform, then Egg");
        m = Mat4Transform.scale(size, (size/3f), size);
        m = Mat4.multiply(m, Mat4Transform.translate(-0.5f, 0.5f, 1f));
        TransformNode renderingPlatform = new TransformNode("Scaled, then translated", m);
        // Egg!
        m = Mat4Transform.scale(radius, (radius*2.5f), radius);
        m = Mat4.multiply(m, Mat4Transform.translate(-0.5f, 0.68f, 1f));
        TransformNode renderingEgg = new TransformNode("Scaled, then translated", m);


        // Textures
        // Texturing the the plith 
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
                plinthPhone.addChild(renderingPlinth);
                    renderingPlinth.addChild(plinthTexture);
                plinthPhone.addChild(renderingPhone);
                    renderingPhone.addChild(phoneTexture);
            attractions.addChild(platformEgg);
                platformEgg.addChild(renderingPlatform);
                    renderingPlatform.addChild(platformTexture);
                platformEgg.addChild(renderingEgg);
                    renderingEgg.addChild(eggTexture);
        exhibitionRoot.update();  // IMPORTANT - don't forget this
        //exhibitionRoot.print(0, false);
        //System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return exhibitionRoot;
    }
}