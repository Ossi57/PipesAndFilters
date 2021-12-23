package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.Utils;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import at.fhv.sysarch.lab3.pipeline.stream.IStreamPush;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class ScreenSpaceTransformationFilter extends Filter<Pair<Face, Color>, Pair<Face, Color>> {

    private PipelineData pipelineData;

    public ScreenSpaceTransformationFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public void write(Pair<Face, Color> in) {
        this.getSuccessor().write(process(in));
    }

    private Pair<Face, Color> process(Pair<Face, Color> in) {
        Vec4 v1 = in.fst().getV1();
        Vec4 v2 = in.fst().getV2();
        Vec4 v3 = in.fst().getV3();

        Mat4 viewPortTransformation = pipelineData.getViewportTransform();

        Face transformedFace = new Face(viewPortTransformation.multiply(v1.multiply(1f / v1.getW())),
                viewPortTransformation.multiply(v2.multiply(1f / v2.getW())),
                viewPortTransformation.multiply(v3.multiply(1f / v3.getW())),
                in.fst());
        return new Pair<>(transformedFace, in.snd());
    }

    @Override
    public Pair<Face, Color> read() {
        Pair<Face, Color> out = getPredecessor().read();
        if(out == null)
            return null;
        if(Utils.checkIfDelimiterIsReached(out.fst()))
            return out;
        return process(out);
    }

}
