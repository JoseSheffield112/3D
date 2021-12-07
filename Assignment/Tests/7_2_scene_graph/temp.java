// /**
//  * This whole class was adapted from Dr. Maddocks code from "M04_GLEventListener.java" class
//  * Much like "Robot.java", I made this class so I could make the scene graph for the Museum room away from the Museum class
//  * 
//  * *********************TO-DO*********************
//  * - Identify methods you've introduced
// */
// import gmaths.*;
// import java.nio.*;
// import java.util.ArrayList;
// import com.jogamp.common.nio.*;
// import com.jogamp.opengl.*;
// import com.jogamp.opengl.util.*;
// import com.jogamp.opengl.util.awt.*;
// import com.jogamp.opengl.util.glsl.*;
// public class Room{
//     /*
//     variables used in class
//     */
//     private SGNode museumRoot;

//     // Declaring models variables
//     private Model floor, wall, door, windowDay, windowNight;

//     //Transform node stuff
//     private TransformNode enlargen, leftWallNightTranslation;

//     //TEMP
//     private DirectionalLight sunLight;
//     private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
//     private SpotLight lampLight;
//     private Camera camera;
//     private int dayCycle = 1;
  
//     //Setting Values
//     private float wallSize = 16f;
//     private float doorSize = wallSize*0.35f;
//     private float doorPositioning = 0.75f;
//     private Vec3 whiteLight = new Vec3(1.0f, 1.0f, 1.0f);

//     public Room(GL3 gl, Camera camera, DirectionalLight sunLight, ArrayList<PointLight> ceilingLights, SpotLight lampLight){
//         this.camera=camera;
//         this.sunLight = sunLight;
//         this.ceilingLights = ceilingLights;
//         this.lampLight = lampLight;
//         sceneGraph(gl);
//     }

//     private void sceneGraph(GL3 gl){
//         //Texture
//         int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/Floor.jpg");
//         int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/brickWall.jpg");
//         int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/door.jpg");
//         int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/stockPhotos/dimitry-anikin-nSZlic4jLeo-unsplash_512.jpg");
//         int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/stockPhotos/dimitry-anikin-WnAYMHMcmYM-unsplash_2_512.jpg");
        
//         //Shapes models
//         //Floor model
//         Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
//         Shader shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
//         Material material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
//         Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
//         floor = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId0);

//         //Handling far wall
//         mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
//         shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
//         material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
//         wall = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId7);
        
//         //Door
//         mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
//         shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
//         material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
//         door = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId8);

//         //Handling Window wall
//         mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
//         shader = new Shader(gl, "vs_tt.txt", "fs_tt.txt");
//         material = new Material(whiteLight, whiteLight, new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
//         windowDay = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId9);
//         windowNight = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId10);


//         //Scene graph
//         //Root
//         museumRoot = new NameNode("Museum");
//         // Adding planes
//         NameNode XYZPlanes = new NameNode("Adding X,Y & Z planes");
//         NameNode ZYPlanes = new NameNode("Adding X & Y planes");
//         NameNode YPlane = new NameNode("Adding X plane");
//         // scaling planes
//         Mat4 m = Mat4Transform.scale(wallSize,1f,wallSize);
//         TransformNode enlargen = new TransformNode("Enlargening by "+wallSize+" in x & y", m);
//         // scaling walls by half
//         m = Mat4Transform.scale(wallSize,1f,(wallSize*0.7f));
//         TransformNode enlargenWall = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
//         TransformNode enlargenWallX = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
//         TransformNode enlargenWallDay = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
//         TransformNode enlargenWallNight = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
//         // Scaling door!
//         m = Mat4Transform.scale((wallSize*0.25f),1f,(wallSize*0.45f));
//         TransformNode scaleDoor = new TransformNode("Enlargening by "+wallSize+" in x & "+(wallSize*0.7f)+"y", m);
//         // Transforming about X
//         m = Mat4Transform.rotateAroundX(90);
//         TransformNode xAxisRotation = new TransformNode("Rotating about X Axis", m);
//         // Transforming about Y
//         m = Mat4Transform.rotateAroundZ(-90);
//         TransformNode zAxisRotation = new TransformNode("Rotating about Z Axis", m);
//         // Translation
        
//         // 2nd value - positive - moves it closer to us
//         // 3rd value - positive - moves it up the screen

//         // used for far wall
//         m = Mat4Transform.translate(0f, -(wallSize*0.5f), -(wallSize*0.35f));//x,y,z where z is right and y is to us
//         TransformNode farWallTranslation = new TransformNode("Translating about Y and Z axis", m);
//         // used for door
//         m = Mat4Transform.translate(-(wallSize*0.3f),0.11f,+(wallSize*0.125f));//x,y,z where z is right and y is to us
//         TransformNode doorTranslation = new TransformNode("Translating about X axis", m);
//         // used for left wall day texture
//         m = Mat4Transform.translate(0f, -(wallSize), -(wallSize*0.35f));//x,y,z where z is right and y is to us
//         TransformNode leftWallDayTranslation = new TransformNode("Translating Day texture about X axis", m);
//         // used for left wall night texture
//         m = Mat4Transform.translate(0f,-(0.1f), 0);//x,y,z where z is right and y is to us
//         leftWallNightTranslation = new TransformNode("Translating Night about X axis", m);


//         // Texturing the the floor
//         NameNode flooring = new NameNode("floor");      
//             ModelNode floorTexture = new ModelNode("Flooring", floor);
//         // Texturing the wall
//         NameNode farWall = new NameNode("far wall");      
//             ModelNode wallTexture = new ModelNode("Wall", wall);
//         // Texturing the door:D
//         NameNode doorNode = new NameNode("Door");  
//             ModelNode doorTexture = new ModelNode("Door", door);
//         // Texturing the left wall
//         NameNode windowWallDay = new NameNode("Window wall - Day");    
//             ModelNode windowWallTextureDay = new ModelNode("left wall view - Day", windowDay);
//         NameNode windowWallNight = new NameNode("Window wall - Night"); 
//             ModelNode windowWallTextureNight = new ModelNode("left wall view - Day", windowNight);

        
//         // Constructing scene graph
//         museumRoot.addChild(XYZPlanes);
//             XYZPlanes.addChild(flooring);
//                 flooring.addChild(enlargen);
//                     enlargen.addChild(floorTexture);
//             XYZPlanes.addChild(xAxisRotation);
//                 xAxisRotation.addChild(ZYPlanes);
//                     ZYPlanes.addChild(farWallTranslation);
//                         farWallTranslation.addChild(enlargenWallX);
//                             enlargenWallX.addChild(farWall);
//                                     farWall.addChild(wallTexture);
//                         farWallTranslation.addChild(doorTranslation);
//                             doorTranslation.addChild(scaleDoor);
//                                 scaleDoor.addChild(doorNode);
//                                     doorNode.addChild(doorTexture);
//                     ZYPlanes.addChild(zAxisRotation);
//                         zAxisRotation.addChild(YPlane);
//                             YPlane.addChild(leftWallDayTranslation);
//                                 leftWallDayTranslation.addChild(enlargenWallDay);
//                                     enlargenWallDay.addChild(windowWallDay);
//                                         windowWallDay.addChild(windowWallTextureDay);
//                                 leftWallDayTranslation.addChild(leftWallNightTranslation);
//                                     leftWallNightTranslation.addChild(enlargenWallNight);
//                                         enlargenWallNight.addChild(windowWallNight);
//                                             windowWallNight.addChild(windowWallTextureNight);
//         museumRoot.update();  // IMPORTANT - don't forget this
//         // museumRoot.print(0, false);
//         // System.exit(0);        
//     }

//     public SGNode updateView(GL3 gl, int cycle){
//         this.dayCycle=cycle;
//         Mat4 m = (dayCycle==1) ? 
//             (Mat4Transform.translate(0f,-(0.1f), 0)) :
//             (Mat4Transform.translate(0f,(0.1f), 0));
//         leftWallNightTranslation = new TransformNode("Translating about X axis", m);
//             // (dayCycle==1) ? 
//             // (leftWallNightTranslation = new TransformNode("Translating about X axis", Mat4Transform.translate(0f,-(0.1f), 0));)
//             // :(leftWallNightTranslation = new TransformNode("Translating about X axis", Mat4Transform.translate(0f,(0.1f), 0)););

//         // sceneGraph(gl);
//         return museumRoot;
//     }

//     public SGNode getSceneGraph(){
//         return museumRoot;
//     }
// }