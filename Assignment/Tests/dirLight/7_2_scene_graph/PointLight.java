import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

/**
 * creating a subclass of Light class
 */
public class PointLight extends Light{

    private Float constant, linear, quadratic, cutOff, outerCutOff;

    public PointLight(GL3 gl, float dimness){
        super(gl, dimness);
        this.getMaterial().setAmbient(new Vec3(0.05f, 0.05f, 0.05f));
        this.getMaterial().setDiffuse(new Vec3(0.8f, 0.8f, 0.8f));
        this.getMaterial().setSpecular(new Vec3(1.0f, 1.0f, 1.0f));
        this.constant = 1.0f;
        this.linear = 0.09f;
        this.quadratic = 0.032f;
    }

    /**
     * Set methods
     */
    /**
     * Setting pointLight floats
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
     * Get methods
     */

    /**
     * Getting pointLight floats
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
}