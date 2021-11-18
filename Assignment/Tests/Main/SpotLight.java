/**
 * TODO LIST
 * sort out get and set methods
 * when you've introduced the lamp to the scene, you need to!
 *      1   - Turn off the light rotation
 *      2   - Make this light move - don't know how yet :D
 */
import java.util.ArrayList;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

/**
 * creating a subclass of Light class
 */
public class SpotLight extends Light{

    private Vec3 direction;
    private Float constant, linear, quadratic, cutOff, outerCutOff;

    public SpotLight(GL3 gl, float dimness){
        super(gl, dimness);
        this.direction = new Vec3(0f,-1f,0f); //Has to be negative in order to point down!
        this.constant = 1.0f;
        this.linear = 0.22f;
        this.quadratic = 0.20f;
        this.cutOff = 42f;
        this.outerCutOff = 68.6f;
    }    

    /**
     * 
     * Setting spotlight position
     * with Mat4 inputs of the current world point (base of scene graph) & rotation matrix applied to lamp cover
     * as we're calculating only X rotation, we only need to deal with diagonals + [1][2] & [2][1] (and [2][2] due to translation)
     *  this method is indeed not generalizable as I only needed it for this specific reason
     */
    public Vec3 calculateXRotation(Mat4 originalPoints, Mat4 rotationMatrix){
        float[] worldPoints = originalPoints.toFloatArrayForGLSL();
        float[] worldMatrix = rotationMatrix.toFloatArrayForGLSL();
        worldPoints[13]=worldPoints[13]+0.2f; // lowering the lamp by difference between lamp size & lamp cover size
        float newX = (worldPoints[12]*worldMatrix[0]) - 2.40f; // decreasing x point by lamptop 
        float newY = (worldPoints[13]*worldMatrix[5]) + (worldPoints[14]*worldMatrix[9]) + (worldMatrix[13]-1.5f);
        float newZ = ((worldPoints[13]*0.8f)*worldMatrix[6]) + (worldPoints[14]*worldMatrix[10]);
        return(new Vec3(newX, newY, newZ));
    }
    /**
     * Setting spotlight direction
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
    /**
     * Setting spotlight floats
     */
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
     * Getting spotlight direction
     */

    public Vec3 getDirection() {
        return direction;
    }
    /**
     * Getting spotlight floats
     */
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