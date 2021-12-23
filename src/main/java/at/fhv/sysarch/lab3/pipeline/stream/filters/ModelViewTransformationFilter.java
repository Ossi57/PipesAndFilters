package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import at.fhv.sysarch.lab3.pipeline.stream.IStream;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ModelViewTransformationFilter extends Filter<Face, Face> {

    PipelineData pipelineData;
    private Mat4 rotationMatrix;

    public ModelViewTransformationFilter(PipelineData pd){
        pipelineData = pd;
    }

    @Override
    public void setSuccessor(IStream<Face, ?> successor) {
        super.setSuccessor(successor);
    }

    public void setRotationMatrix(Mat4 rotate) {
        rotationMatrix = rotate;
    }

    @Override
    public void write(Face in) {
        super.getSuccessor().write(process(in));
    }

    private Face process(Face input){
        Mat4 mT = pipelineData.getModelTranslation();
        Mat4 vT = pipelineData.getViewTransform();
        Mat4 transformation = vT.multiply(mT).multiply(rotationMatrix);
        Vec4 v1 = transformation.multiply(input.getV1());
        Vec4 v2 = transformation.multiply(input.getV2());
        Vec4 v3 = transformation.multiply(input.getV3());
        Vec4 n1 = transformation.multiply(input.getN1());
        Vec4 n2 = transformation.multiply(input.getN2());
        Vec4 n3 = transformation.multiply(input.getN3());

        return new Face(v1, v2, v3, n1, n2, n3);
    }



    @Override
    public Face read() {
        return null;
    }
}
