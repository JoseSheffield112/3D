//Building the body
NameNode body = new NameNode("body");
Mat4 m = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));//Raising body a further .5 above the legs top!
TransformNode bodyTransform = new TransformNode("body transform", m);
  ModelNode bodyShape = new ModelNode("Cube(body)", cube);

NameNode head = new NameNode("head"); 
m = new Mat4(1);
m = Mat4.multiply(m, Mat4Transform.translate(0,bodyHeight,0));
m = Mat4.multiply(m, Mat4Transform.scale(headScale,headScale,headScale));
m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
TransformNode headTransform = new TransformNode("head transform", m);
  ModelNode headShape = new ModelNode("Sphere(head)", sphere);

NameNode leftarm = new NameNode("left arm");
TransformNode leftArmTranslate = new TransformNode("leftarm translate", 
                                     Mat4Transform.translate((bodyWidth*0.5f)+(armScale*0.5f),bodyHeight,0));
leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundX(180));
m = new Mat4(1);
m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
TransformNode leftArmScale = new TransformNode("leftarm scale", m);
  ModelNode leftArmShape = new ModelNode("Cube(left arm)", cube2);

NameNode rightarm = new NameNode("right arm");
TransformNode rightArmTranslate = new TransformNode("rightarm translate", 
                                      Mat4Transform.translate(-(bodyWidth*0.5f)-(armScale*0.5f),bodyHeight,0));
rightArmRotate = new TransformNode("rightarm rotate",Mat4Transform.rotateAroundX(180));
m = new Mat4(1);
m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
TransformNode rightArmScale = new TransformNode("rightarm scale", m);
  ModelNode rightArmShape = new ModelNode("Cube(right arm)", cube2);
  
NameNode leftleg = new NameNode("left leg");
m = new Mat4(1);
m = Mat4.multiply(m, Mat4Transform.translate((bodyWidth*0.5f)-(legScale*0.5f),0,0));
m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
TransformNode leftlegTransform = new TransformNode("leftleg transform", m);
  ModelNode leftLegShape = new ModelNode("Cube(leftleg)", cube);

NameNode rightleg = new NameNode("right leg");
m = new Mat4(1);
m = Mat4.multiply(m, Mat4Transform.translate(-(bodyWidth*0.5f)+(legScale*0.5f),0,0));
m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
TransformNode rightlegTransform = new TransformNode("rightleg transform", m);
  ModelNode rightLegShape = new ModelNode("Cube(rightleg)", cube);
  
robotRoot.addChild(robotMoveTranslate);
robotMoveTranslate.addChild(robotTranslate);
  robotTranslate.addChild(body);
    body.addChild(bodyTransform);
      bodyTransform.addChild(bodyShape);
    body.addChild(head);
      head.addChild(headTransform);
      headTransform.addChild(headShape);
    body.addChild(leftarm);
      leftarm.addChild(leftArmTranslate);
      leftArmTranslate.addChild(leftArmRotate);
      leftArmRotate.addChild(leftArmScale);
      leftArmScale.addChild(leftArmShape);
    body.addChild(rightarm);
      rightarm.addChild(rightArmTranslate);
      rightArmTranslate.addChild(rightArmRotate);
      rightArmRotate.addChild(rightArmScale);
      rightArmScale.addChild(rightArmShape);
    body.addChild(leftleg);
      leftleg.addChild(leftlegTransform);
      leftlegTransform.addChild(leftLegShape);
    body.addChild(rightleg);
      rightleg.addChild(rightlegTransform);
      rightlegTransform.addChild(rightLegShape);