/* I declare that this code is my own work */
/**
 * Author: Jose Alves
 * Email : jalves1@sheffield.ac.uk
 * Student # : 170163532
 */
/**
 * This whole class extends & was adapted from Dr.Maddocks "Light" class
 */
import java.util.ArrayList;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

/**
 * creating a subclass of the Light class
 */
public class SpotLight extends Light{

    // Declaring variables
    private Vec3 direction;
    private Float constant, linear, quadratic, cutOff, outerCutOff;

    //constructor
    public SpotLight(GL3 gl, float dimness){
        super(gl, dimness);
        this.direction = new Vec3(0f,-1f,0f); 
        this.constant = 1.0f;
        this.linear = 0.22f;
        this.quadratic = 0.20f;
        this.cutOff = 42f;
        this.outerCutOff = 68.6f;
    }    

    /**
     * Matric multiplication for setting the spotlight position
     * - Mat4 input of the current world point (base of lamp scene graph) 
     * - Mat4 input of rotation matrix applied to lamp cover
     * - Returns a Vec3 representing current lamp bulb position
     *  this method is not generalisable to other rotations - focused just on X rotation
     */
    public Vec3 calculateXRotation(Mat4 originalPoints, Mat4 rotationMatrix){
        float[] worldPoints = originalPoints.toFloatArrayForGLSL();
        float[] worldMatrix = rotationMatrix.toFloatArrayForGLSL();
        worldPoints[13]=worldPoints[13]+0.2f; // lowering the lamp by difference between lamp size & lamp cover size
        float newX = (worldPoints[12]*worldMatrix[0]) - 2.4f; // aligning bulb with lamp cover
        float newY = (worldPoints[13]*worldMatrix[5]) + (worldPoints[14]*worldMatrix[9]) + (worldMatrix[13]-1.5f);
        float newZ = ((worldPoints[13]*0.8f)*worldMatrix[6]) + (worldPoints[14]*worldMatrix[10]);
        return(new Vec3(newX, newY, newZ));
    }

    /**
     * Set methods
     */
    public void setDirection(Vec3 v) {
        direction.x = v.x;
        direction.y = v.y;
        direction.z = v.z;
    }  
    public void setDirection(float x, float y, float z) {
        direction.x = x;
        direction.y = y;
        direction.z = z;
    }
    public void setConstant(float constant){
        constant = constant;
    }
    public void setLinear(float linear){
        linear = linear;
    }
    public void setQuadratic(float quadratic){
        quadratic = quadratic;
    }
    public void setCuttOff(float cuttOff){
        cuttOff = cuttOff;
    }
    public void setOuterCuttOff(float outerCuttOff){
        outerCuttOff = outerCuttOff;
    }

    /**
     * Get methods
     */
    public Vec3 getDirection() {
        return direction;
    }
    public float getConstant(){
        return(constant);
    }
    public float getLinear(){
        return(linear);
    }
    public float getQuadratic(){
        return(quadratic);
    }
    public float getCuttOff(){
        return(cutOff);
    }
    public float getOuterCuttOff(){
        return(outerCutOff);
    }
    
}