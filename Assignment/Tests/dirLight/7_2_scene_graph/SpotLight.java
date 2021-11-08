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
        this.linear = 0.07f;//0.09f;
        this.quadratic = 0.017f;//0.032f;
        this.cutOff = 7.0f;//50f;
        this.outerCutOff = 10.0f;//50f;
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