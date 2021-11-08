import java.util.ArrayList;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class Model {
  
  private Mesh mesh;
  private int[] textureId1; 
  private int[] textureId2; 
  private Material material;
  private Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private ArrayList<DirectionalLight> lights;
  private SpotLight spotLight;
  private PointLight pointLight;
  
  // this model would have both a diffuse texture (textureId1) and then also has a specular texture (textureId2
  public Model(GL3 gl, Camera camera, ArrayList<DirectionalLight> lights, PointLight pointLight, SpotLight spotLight, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1, int[] textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.lights = lights;
    this.spotLight = spotLight;
    this.pointLight = pointLight;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  // this textureId1 only counts as the diffuse texture for this model
  public Model(GL3 gl, Camera camera, ArrayList<DirectionalLight> lights, PointLight pointLight, SpotLight spotLight, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1) {
    this(gl, camera, lights, pointLight, spotLight, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  public Model(GL3 gl, Camera camera, ArrayList<DirectionalLight> lights,  PointLight pointLight,SpotLight spotLight, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, lights, pointLight, spotLight, shader, material, modelMatrix, mesh, null, null);
  }
  
  // add constructors without modelMatrix? and then set to identity as the default?
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  /**
   * Setting spotLight
   */
  public void setSpotLight(SpotLight spotLight) {
    this.spotLight = spotLight;
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    // Setting up Lighting!
    // Directional lighting
    //Iterating over the different lights and setting them up
    for(int index=0; index<(lights.size()); index++){
      shader.setVec3(gl, "dirLight["+index+"].position", lights.get(index).getPosition());
      shader.setVec3(gl, "dirLight["+index+"].ambient", lights.get(index).getMaterial().getAmbient());
      shader.setVec3(gl, "dirLight["+index+"].diffuse", lights.get(index).getMaterial().getDiffuse());
      shader.setVec3(gl, "dirLight["+index+"].specular", lights.get(index).getMaterial().getSpecular());
    }

    //TESTING - TESTING -TESTING - TESTING -TESTING - TESTING -TESTING - TESTING -TESTING - TESTING -TESTING - TESTING -
    //PointLight!!
    // Seems point light might be for the sun!!
    shader.setVec3(gl, "pointLight.position", pointLight.getPosition());
    shader.setVec3(gl, "pointLight.ambient", pointLight.getMaterial().getAmbient());
    shader.setVec3(gl, "pointLight.diffuse", pointLight.getMaterial().getDiffuse());
    shader.setVec3(gl, "pointLight.specular", pointLight.getMaterial().getSpecular());
    shader.setFloat(gl, "pointLight.constant", pointLight.getConstant());
    shader.setFloat(gl, "pointLight.linear", pointLight.getLinear());
    shader.setFloat(gl, "pointLight.quadratic", pointLight.getQuadratic());    
    //Testing ended

    //Spot Lighting!
    shader.setVec3(gl, "spotLight.position", spotLight.getPosition());
    shader.setVec3(gl, "spotLight.direction", spotLight.getDirection());
    shader.setVec3(gl, "spotLight.ambient", spotLight.getMaterial().getAmbient());
    shader.setVec3(gl, "spotLight.diffuse", spotLight.getMaterial().getDiffuse());
    shader.setVec3(gl, "spotLight.specular", spotLight.getMaterial().getSpecular());
    shader.setFloat(gl, "spotLight.constant", spotLight.getConstant());
    shader.setFloat(gl, "spotLight.linear", spotLight.getLinear());
    shader.setFloat(gl, "spotLight.quadratic", spotLight.getQuadratic());
    shader.setFloat(gl, "spotLight.cutOff", (float)Math.cos(Math.toRadians(spotLight.getCuttOff())));
    shader.setFloat(gl, "spotLight.outerCutOff", (float)Math.cos(Math.toRadians(spotLight.getOuterCuttOff()))); 

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    if (textureId1!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
    }
    mesh.render(gl);
  } 
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }
  
  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) gl.glDeleteBuffers(1, textureId1, 0);
    if (textureId2!=null) gl.glDeleteBuffers(1, textureId2, 0);
  }
  
}