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
        this.linear = 0.09f;
        this.quadratic = 0.032f;
        this.cutOff = 12.5f;
        this.outerCutOff = 17.5f;
    }

    /**
     * 
     * Setting spotlight position
     * with a Mat4 input
     * as we're calculating X rotation, we only need to deal with diagonals + [1][2] & [2][1]
     */
    public Vec3 calculateXRotation(Mat4 rotation , float x, float y, float z){
        float[] MValues = rotation.toFloatArrayForGLSL();
        // Dealing with x coords
        float newX = (x*MValues[0]);
        float newY = (y*MValues[5]) + (z*MValues[9]) + y;
        float newZ = (y*MValues[6]) + (z*MValues[10]);
        return(new Vec3(newX, (newY-8f), (newZ*0.5f)));
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