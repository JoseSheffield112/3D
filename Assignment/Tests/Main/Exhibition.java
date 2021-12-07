/* I declare that this code is my own work */
/**
 * Author: Jose Alves
 * Email : jalves1@sheffield.ac.uk
 * Student # : 170163532
 */
/**
 * This whole class used code from various of Dr.Maddocks 3D tutorials
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

    // variables used in class
    private SGNode exhibitionRoot;

    // Declaring models variables
    private Model cube, sphere, phone;

    //Lights
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
        //Textures
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/wood_texture.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/wood_spec.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/egg.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/egg_spec.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/phone_texture.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/phone_spec.jpg");

        // Objects sizes
        // plinth variables
        final float plinthWidth, plinthHeight, plinthDepth;
        plinthWidth = 5.5f;
        plinthHeight = 2f;
        plinthDepth = 1.2f;
        // phone
        final float phoneWidth, phoneHeight, phoneDepth;
        phoneWidth = plinthWidth-1f;
        phoneHeight = plinthHeight*5f;
        phoneDepth = plinthDepth*0.7f;
        // Egg + Platform variables
        final float platformSize = 2f;
        final float eggRadius = 2f;

        //Models
        // Cube model
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1, textureId2);

        // phone model
        mesh = new Mesh(gl, Phone.vertices.clone(), Phone.indices.clone());
        shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        modelMatrix = Mat4Transform.scale(1f,1f,1f);
        phone = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId5);


        // Sphere model
        mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "vs_sphere.txt", "fs_sphere.txt");
        material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        sphere = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId3, textureId4);

        //Graph
        // Making graph stuff
        exhibitionRoot = new NameNode("Exhibition root node");

        // Nodes(objects)
        NameNode attractions = new NameNode("Building attractions");

        // First object

        // Plinth
        NameNode plinthPhone = new NameNode("Phone Plinth");
        // Centering the phone attraction
        Mat4 m = Mat4Transform.translate(1f, (plinthHeight*0.5f), -10f);
        TransformNode centeringPlinth = new TransformNode("Translating plinth", m);
        // Dealing with plinth
        m = Mat4Transform.scale(plinthWidth, plinthHeight, plinthDepth);
        TransformNode renderingPlinth = new TransformNode("Scaling plinth", m);
        
        // Phone
        m = Mat4Transform.translate(0f, ((phoneHeight*0.5f)+(plinthHeight*0.5f)), 0f); // Gotta place the phone ON TOP of the plinth
        m = Mat4.multiply(m, Mat4Transform.scale(phoneWidth, phoneHeight, phoneDepth));
        TransformNode renderingPhone = new TransformNode("Translated, then Scaled", m);

        // Second build
        // Platform
        NameNode platformEgg = new NameNode("Egg Platform");
        // Centering the egg attraction
        m = Mat4Transform.translate(-2f, (platformSize*0.25f), 2f);
        TransformNode centeringPlatform = new TransformNode("Translated platform", m);
        // Dealing with platform
        m = Mat4Transform.scale(platformSize, (platformSize*0.5f), platformSize);
        TransformNode renderingPlatform = new TransformNode("Scaled platform", m);

        // Egg
        m = Mat4Transform.translate(0f, ((platformSize*0.25f)+(eggRadius*1.25f)), 0f); // Gotta place the phone ON TOP of the plinth
        m = Mat4.multiply(m, Mat4Transform.scale(eggRadius, (eggRadius*2.5f), eggRadius));
        TransformNode renderingEgg = new TransformNode("Translated, then Scaled", m);


        // Textures
        // Texturing the the plinth 
        ModelNode plinthTexture = new ModelNode("Plinth texture", cube);
        // Texturing the the Phone 
        ModelNode phoneTexture = new ModelNode("Phone texture", phone);

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