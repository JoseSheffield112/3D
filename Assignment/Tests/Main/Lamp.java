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
public class Lamp{
    /*
    variables used in class
    */
    private SGNode lampRoot;

    // Declaring models variables
    private Model cube, sphere;

    //TEMP
    private DirectionalLight sunLight;
    private static ArrayList<PointLight> ceilingLights = new ArrayList<PointLight>();
    private SpotLight lampLight;
    private Camera camera;
    private float rotationAngle;

    //angle
    private TransformNode rotateTop, renderingCover, renderingTop;
    private Mat4 translateToTop;

    public Lamp(GL3 gl,Camera camera, DirectionalLight sunLight, ArrayList<PointLight> ceilingLights, SpotLight lampLight, float startAngle){
        this.camera=camera;
        this.sunLight = sunLight;
        this.ceilingLights = ceilingLights;
        this.lampLight = lampLight;
        this.rotationAngle = startAngle;
        sceneGraph(gl);
    }

    private void sceneGraph(GL3 gl){
        //Texture
        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        //Sizes
        // plinth variables
        final float size = 1f;
        final float poleGirth, poleHeight;
        poleGirth = 0.5f;
        poleHeight = 8f;
        final float topWidth = 3f;
        final float lampSize = 0.5f;

        //Models
        //Setting up model for the cube used in the scene!
        Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        Shader shader = new Shader(gl, "vs_cube.txt", "fs_cube.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f); // CHANGE THIS TO METAL VALUES
        Mat4 modelMatrix = Mat4Transform.scale(1f,1f,1f);
        cube = new Model(gl, camera, sunLight, ceilingLights, lampLight, shader, material, modelMatrix, mesh, textureId1, textureId2);

        //Graph
        // Making graph stuff
        lampRoot = new NameNode("Lamp root node");

        // Scene Graph stuff
        // Setting the base
        Mat4 m = Mat4Transform.translate(10f, (size*0.5f), 0f);
        TransformNode centeringBase = new TransformNode("Centering the lamp", m);
        // base
        NameNode lampBase = new NameNode("Lamp - base");
        m = Mat4Transform.scale(size, size, size);
        TransformNode renderingBase = new TransformNode("Scaled", m);
        // Pole
        NameNode lampPole = new NameNode("Lamp - pole");
        m = Mat4Transform.translate(0f, ((size*0.5f)+(poleHeight*0.5f)), 0f); // Gotta place the phone ON TOP of the plinth
        TransformNode translatingPole = new TransformNode("Translated, then scaled", m);
        m = Mat4Transform.scale(poleGirth, poleHeight, poleGirth);
        TransformNode scalingPole = new TransformNode("Translated, then scaled", m);
        //Top
        NameNode lampTop = new NameNode("Lamp - top");
        m = Mat4Transform.translate(-(topWidth*0.4f), (poleHeight*0.5f), 0f);
        m = Mat4.multiply(m, Mat4Transform.scale(topWidth, (size*0.5f), (size*0.55f)));
        renderingTop = new TransformNode("Translated, then scaled", m);
        //lamp
        NameNode lampCover = new NameNode("Lamp - top");
        rotateTop = new TransformNode("rotateAroundX("+rotationAngle+")", (Mat4Transform.rotateAroundX(rotationAngle)));
        m = Mat4Transform.translate(-(topWidth*0.8f), (poleHeight*0.49f), 0f);//Here is where we'll change the rotation values!
        m = Mat4.multiply(m, Mat4Transform.scale(lampSize, lampSize, lampSize));
        renderingCover = new TransformNode("Translated, then scaled", m);

        //Translating a point to top
        translateToTop = Mat4Transform.translate(0f, (poleHeight-lampSize), 0f);

        // Textures
        // Texturing the the base
        ModelNode baseTexture = new ModelNode("Base texture", cube);
        // Texturing the pole
        ModelNode poleTexture = new ModelNode("Pole texture", cube);
        // Texturing the top
        ModelNode topTexture = new ModelNode("Top texture", cube);
        // Texturing the lamp Cover
        ModelNode coverTexture = new ModelNode("Cover texture", cube);

        
        //Constructing scene graph
        lampRoot.addChild(centeringBase);
            centeringBase.addChild(lampBase);
                lampBase.addChild(renderingBase);
                    renderingBase.addChild(baseTexture);
                    renderingBase.addChild(lampPole);
                        lampPole.addChild(translatingPole);
                            translatingPole.addChild(scalingPole);
                                scalingPole.addChild(poleTexture);
                            translatingPole.addChild(lampTop);
                                lampTop.addChild(renderingTop);
                                    renderingTop.addChild(topTexture);
                                lampTop.addChild(rotateTop);
                                    rotateTop.addChild(lampCover);
                                        lampCover.addChild(renderingCover);
                                            renderingCover.addChild(coverTexture);
        lampRoot.update();  // IMPORTANT - don't forget this
        // lampRoot.print(0, false);
        // System.exit(0);        
    }

    public TransformNode getRotationMatrix(float newAngle){
        this.rotationAngle=newAngle;
        Mat4 rotationMatrix = new Mat4(Mat4.multiply(translateToTop, Mat4Transform.rotateAroundX(rotationAngle)));
        rotateTop.setTransform(rotationMatrix);
        lampRoot.update();
        return(rotateTop);
    }
    
    public TransformNode getTopPoints(){
        return(renderingTop);
    }

    public SGNode getSceneGraph(){
        return lampRoot;
    }
}