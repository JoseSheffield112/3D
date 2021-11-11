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
        int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/brickWall.jpg");
        int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/door.jpg");

        // Variables for room construction
        // loading models
        Vec3 whiteLight = new Vec3(1.0f, 1.0f, 1.0f);
        float wallSize = 16f;

        //Shapes models
        //Floor model
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
        Material material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
        floor = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0);

        //Handling far wall
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
        material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        wall = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId7);
/*        
        //Door
        //
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
        material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        modelMatrix = Mat4Transform.scale(doorSize,1f,doorSize*2);
        modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
        modelMatrix = Mat4.multiply(Mat4Transform.translate(-doorSize*doorPositioning,doorSize,-wallSize*0.499f), modelMatrix);
        door = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId8);
*/
        //Scene graph
        //Root
        museumRoot = new NameNode("Museum");
        // Adding planes
        NameNode XYZPlanes = new NameNode("Adding X,Y & Z planes");
        NameNode ZYPlanes = new NameNode("Adding X & Y planes");
        NameNode YPlane = new NameNode("Adding X plane");
        // scaling planes
        Mat4 m = Mat4Transform.scale(wallSize,1f,wallSize);
        TransformNode enlargen = new TransformNode("Enlargening by "+wallSize+" in x & y", m);
        // scaling walls by half
        m = Mat4Transform.scale(wallSize,1f,(wallSize*0.7f));
        TransformNode enlargenWall = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
        TransformNode enlargenWallX = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
        TransformNode enlargenWallX2 = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
        // Transforming about X
        m = Mat4Transform.rotateAroundX(90);
        TransformNode xAxisRotation = new TransformNode("Rotating about X Axis", m);
        // Transforming about Y
        m = Mat4Transform.rotateAroundZ(-90);
        TransformNode zAxisRotation = new TransformNode("Rotating about Z Axis", m);
        // Translation
        m = Mat4Transform.translate(0f, -(wallSize*0.5f), -(wallSize*0.35f));//x,y,z where z is right and y is to us
        TransformNode farWallTranslation = new TransformNode("Translating about Y and Z axis", m);
        m = Mat4Transform.translate(0f, -(wallSize*0.5f), -(wallSize*0.35f));//x,y,z where z is right and y is to us
        TransformNode XAxisTranslation = new TransformNode("Translating about X axis", m);

        // Texturing the the floor
        NameNode flooring = new NameNode("floor");      
            ModelNode floorTexture = new ModelNode("Flooring", floor);

        // Texturing the wall
        NameNode farWall = new NameNode("far wall");      
            ModelNode wallTexture = new ModelNode("Wall", wall);
        //
        NameNode leftWall = new NameNode("left wall");    
            ModelNode leftWallTexture = new ModelNode("left wall view", wall);

        
        // Constructing scene graph
        museumRoot.addChild(XYZPlanes);
            XYZPlanes.addChild(flooring);
                flooring.addChild(enlargen);
                    enlargen.addChild(floorTexture);
            XYZPlanes.addChild(xAxisRotation);
                xAxisRotation.addChild(ZYPlanes);
                    ZYPlanes.addChild(farWallTranslation);
                        farWallTranslation.addChild(enlargenWallX);
                            enlargenWallX.addChild(farWall);
                                farWall.addChild(wallTexture);
                    ZYPlanes.addChild(zAxisRotation);
                        zAxisRotation.addChild(YPlane);
                            YPlane.addChild(XAxisTranslation);
                                XAxisTranslation.addChild(enlargenWallX2);
                                    enlargenWallX2.addChild(leftWall);
                                        leftWall.addChild(leftWallTexture);
        museumRoot.update();  // IMPORTANT - don't forget this
        // museumRoot.print(0, false);
        // System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return museumRoot;
    }
}