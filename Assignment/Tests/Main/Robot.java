import gmaths.*;

import java.nio.*;
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
    private Model floor, sphere, cube, cube2;

    //TEMP
    private Light light;
    private Camera camera;

    //Transform node stuff
    private TransformNode translateX, robotMoveTranslate;

    public Robot(GL3 gl, Light light, Camera camera) {
        this.light = light;
        this.camera=camera;

        sceneGraph(gl);
    }

    private void sceneGraph(GL3 gl){
        // variables used in construction
        float bodyHeight = 3f;
        float bodyWidth = 2f;// x
        float bodyDepth = 1f;//z
        float headScale = 2f;
        float armLength = 3.5f;
        float armScale = 0.5f;
        float legLength = 3.5f;
        float legScale = 0.67f;

        //Loading textures
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
        int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/wattBook.jpg");
        int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wattBook_specular.jpg");

        // shapes models
        // A Cube model
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId3, textureId4);

        cube2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId5, textureId6); 

        // Scenegraph nodes
        // robot - scene graph construction

        // Root node for Robot scene graph
        robotRoot = new NameNode("robot");
        // moving whole of robot in x axis
        robotMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,0,0));
        //Translating whole body above world floor
        TransformNode robotTranslate = new TransformNode("robot transform",Mat4Transform.translate(0,legLength,0));

        //Building the body
        NameNode body = new NameNode("body");
        Mat4 m = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
        m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
        TransformNode bodyTransform = new TransformNode("body transform", m);
        ModelNode bodyShape = new ModelNode("Cube(body)", cube);

        //Constructing scene graph
        robotRoot.addChild(robotMoveTranslate);
            robotMoveTranslate.addChild(robotTranslate);
                robotTranslate.addChild(body);
                    body.addChild(bodyTransform);
                        bodyTransform.addChild(bodyShape);
      
        robotRoot.update();  // IMPORTANT - don't forget this
        //robotRoot.print(0, false);
        //System.exit(0);        
    }

    public SGNode getSceneGraph(){
        return robotRoot;
    }
}