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
public class Room{
    
    // variables used in class
    private SGNode museumRoot;

    // Declaring models variables
    private Model floor, wall, door, window, window2;

    //Transform node stuff
    private TransformNode enlargen;

    // Lights
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
    private int dayCycle = 1;
  
    //Setting Size values
    private float wallSize = 24f;
    private float leftWallPortion = 8f;
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
        int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/day_view.jpg");
        int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/evening_view.jpg");
        
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
        
        //Door
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
        material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        door = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId8);

        //Handling Window wall
        mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
        material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        window = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId9);
        window2 = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId10);


        //Scene graph
        //Root
        museumRoot = new NameNode("Museum");
        // Adding planes
        NameNode XYZPlanes = new NameNode("Adding X,Y & Z planes");
        NameNode ZYPlanes = new NameNode("Adding X & Y planes");
        NameNode YPlane = new NameNode("Adding X plane");
        // scaling planes
        Mat4 m = Mat4Transform.scale(wallSize,1f,wallSize);
        TransformNode enlargen = new TransformNode("Scaling", m);
        // scaling walls by half
        m = Mat4Transform.scale(wallSize,1f,(wallSize*0.7f));
        TransformNode enlargenWall = new TransformNode("Enlargening ", m);
        TransformNode enlargenWallX = new TransformNode("Enlargening ", m);
        TransformNode enlargenWallX2 = new TransformNode("Enlargening ", m);
        // Scaling door!
        m = Mat4Transform.scale((wallSize*0.25f),1f,(wallSize*0.45f));
        TransformNode scaleDoor = new TransformNode("scaling door", m);
        // Transforming about X
        m = Mat4Transform.rotateAroundX(90);
        TransformNode xAxisRotation = new TransformNode("Rotating about X Axis", m);
        // Transforming about Y
        m = Mat4Transform.rotateAroundZ(-90);
        TransformNode zAxisRotation = new TransformNode("Rotating about Z Axis", m);
        // Translation

        // used for far wall
        m = Mat4Transform.translate(0f, -(wallSize*0.5f), -(wallSize*0.35f));
        TransformNode farWallTranslation = new TransformNode("Translating about Y and Z axis", m);
        // used for door
        m = Mat4Transform.translate(-(wallSize*0.3f),0.11f,+(wallSize*0.125f));
        TransformNode doorTranslation = new TransformNode("Translating about X axis", m);  
        // used for left wall view
        m = Mat4Transform.translate(0f, -(wallSize*0.8f), -(wallSize*0.35f));
        TransformNode leftWallView = new TransformNode("Translating about X axis", m);

        // Building left wall
        m = Mat4Transform.translate(0f, -(wallSize*0.5f), -(leftWallPortion*0.5f));
        TransformNode leftWallTranslation = new TransformNode("Translating to left wall", m); 
        // S1 - Bottom left 
        NameNode S1 = new NameNode("far wall - S1");    
        m = new Mat4(1);
        m = Mat4Transform.translate(-leftWallPortion, 0,0);
        TransformNode bottomLeftTranslation = new TransformNode("Translated to bottom left ", m); 
        m = Mat4Transform.scale(leftWallPortion,1f,leftWallPortion);  
        TransformNode enlargenWallS1 = new TransformNode("Enlargened S1 ", m);  
        ModelNode S1Texture = new ModelNode("Far wall S1 texture", wall);
        // S4 - Middle Left
        NameNode S4 = new NameNode("far wall - S4");    
        m = new Mat4(1);
        m = Mat4Transform.translate(0, 0, -leftWallPortion);  
        m = Mat4.multiply(m, Mat4Transform.scale(leftWallPortion,1f,(leftWallPortion+1.56f)));  
        TransformNode wallS4 = new TransformNode("Enlargened S4 ", m);  
        ModelNode S4Texture = new ModelNode("Far wall S4 texture", wall);
        // S2 - Bottom Middle 
        NameNode S2 = new NameNode("far wall - S2");    
        m = new Mat4(1);
        m = Mat4Transform.translate(-(leftWallPortion*0.25f), 0,(leftWallPortion*0.25f));
        TransformNode s2Translation = new TransformNode("Translated to bottom left ", m);
        m = Mat4Transform.scale((leftWallPortion*0.5f),1f,(leftWallPortion*0.5f));  
        TransformNode enlargenWallS2 = new TransformNode("Enlargened S2 ", m);  
        ModelNode S2Texture = new ModelNode("Far wall S2 texture", wall);
        // S2.5 - Bottom Middle 
        NameNode S2_5 = new NameNode("far wall - S2.5");    
        m = new Mat4(1);
        m = Mat4Transform.translate(+(leftWallPortion*0.25f), 0,(leftWallPortion*0.25f));
        TransformNode s2_5Translation = new TransformNode("Translated to bottom left ", m);
        m = Mat4Transform.scale((leftWallPortion*0.5f),1f,(leftWallPortion*0.5f));  
        TransformNode enlargenWallS2_5 = new TransformNode("Enlargened S2.5 ", m);  
        ModelNode S2_5Texture = new ModelNode("Far wall S2.5 texture", wall);
        // S5 - Top Middle 
        NameNode S5 = new NameNode("far wall - S2");    
        m = new Mat4(1);
        m = Mat4Transform.translate(0, 0,-((leftWallPortion*1.8f)-1.56f));
        m = Mat4.multiply(m, Mat4Transform.scale((leftWallPortion*0.5f),1f,(leftWallPortion*0.5f)));  
        TransformNode wallS5 = new TransformNode("Enlargened S2 ", m);  
        ModelNode S5Texture = new ModelNode("Far wall S2 texture", wall);
        // S5.5 - Top Middle 
        NameNode S5_5 = new NameNode("far wall - S2.5");    
        m = new Mat4(1);
        m = Mat4Transform.translate(0, 0,-((leftWallPortion*1.8f)-1.56f));
        m = Mat4.multiply(m, Mat4Transform.scale((leftWallPortion*0.5f),1f,(leftWallPortion*0.5f)));  
        TransformNode wallS5_5 = new TransformNode("Enlargened S2.5 ", m);  
        ModelNode S5_5Texture = new ModelNode("Far wall S2.5 texture", wall);
        // S3 - Bottom Right 
        NameNode S3 = new NameNode("far wall - S3");    
        m = new Mat4(1);
        m = Mat4Transform.translate(leftWallPortion, 0, 0);
        TransformNode bottomRightTranslation = new TransformNode("Enlargened S3 ", m); 
        m = Mat4Transform.scale(leftWallPortion,1f,leftWallPortion);  
        TransformNode enlargenWallS3 = new TransformNode("Enlargened S3 ", m);  
        ModelNode S3Texture = new ModelNode("Far wall S3 texture", wall);
        // S6 - Middle Right
        NameNode S6 = new NameNode("far wall - S6");    
        m = new Mat4(1);
        m = Mat4Transform.translate(0, 0, -leftWallPortion);
        m = Mat4.multiply(m, Mat4Transform.scale(leftWallPortion,1f,(leftWallPortion+1.56f)));  
        TransformNode wallS6 = new TransformNode("Enlargened S3 ", m);  
        ModelNode S6Texture = new ModelNode("Far wall S3 texture", wall);
          

        // Texturing the the floor
        NameNode flooring = new NameNode("floor");      
            ModelNode floorTexture = new ModelNode("Flooring", floor);
        // Texturing the wall
        NameNode farWall = new NameNode("far wall");      
            ModelNode wallTexture = new ModelNode("Wall", wall);
        // Texturing the door:D
        NameNode doorNode = new NameNode("Door");  
            ModelNode doorTexture = new ModelNode("Door", door);
        // Texturing the left wall view
        NameNode windowView = new NameNode("Window wall");    
            ModelNode windowViewTexture = new ModelNode("left wall view = day", window);

        if(dayCycle!=1){
            windowViewTexture = new ModelNode("left wall view = night", window2);
        }

        
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
                        farWallTranslation.addChild(doorTranslation);
                            doorTranslation.addChild(scaleDoor);
                                scaleDoor.addChild(doorNode);
                                    doorNode.addChild(doorTexture);
                    ZYPlanes.addChild(zAxisRotation);
                        zAxisRotation.addChild(YPlane);
                            YPlane.addChild(leftWallView);
                                leftWallView.addChild(enlargenWallX2);
                                    enlargenWallX2.addChild(windowView);
                                        windowView.addChild(windowViewTexture);
                            YPlane.addChild(leftWallTranslation);
                                leftWallTranslation.addChild(bottomLeftTranslation);
                                    bottomLeftTranslation.addChild(S1);
                                        S1.addChild(enlargenWallS1);
                                            enlargenWallS1.addChild(S1Texture);
                                    bottomLeftTranslation.addChild(S4);
                                        S4.addChild(wallS4);
                                            wallS4.addChild(S4Texture);
                                leftWallTranslation.addChild(s2Translation);
                                    s2Translation.addChild(S2);
                                        S2.addChild(enlargenWallS2);
                                            enlargenWallS2.addChild(S2Texture);
                                    s2Translation.addChild(wallS5);
                                        wallS5.addChild(S5Texture);
                                leftWallTranslation.addChild(s2_5Translation);
                                    s2_5Translation.addChild(S2_5);
                                        S2_5.addChild(enlargenWallS2_5);
                                            enlargenWallS2_5.addChild(S2_5Texture);
                                    s2_5Translation.addChild(wallS5_5);
                                        wallS5_5.addChild(S5_5Texture);
                                leftWallTranslation.addChild(bottomRightTranslation);
                                    bottomRightTranslation.addChild(S3);
                                        S3.addChild(enlargenWallS3);
                                            enlargenWallS3.addChild(S3Texture);
                                    bottomRightTranslation.addChild(S6);
                                        S6.addChild(wallS6);
                                            wallS6.addChild(S6Texture);

        museumRoot.update();  // IMPORTANT - don't forget this
        // museumRoot.print(0, false);
        // System.exit(0);        
    }

    public SGNode updateView(GL3 gl, int cycle){
        this.dayCycle=cycle;
        sceneGraph(gl);
        return museumRoot;
    }

    public SGNode getSceneGraph(){
        return museumRoot;
    }
}