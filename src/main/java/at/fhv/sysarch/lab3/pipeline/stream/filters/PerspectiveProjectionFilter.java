package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.Utils;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import at.fhv.sysarch.lab3.pipeline.stream.IStreamPush;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class PerspectiveProjectionFilter extends Filter<Pair<Face, Color>, Pair<Face, Color>> {

    private PipelineData pipelineData;

    public PerspectiveProjectionFilter(PipelineData pd) {
        this.pipelineData = pd;
    }

    @Override
    public void write(Pair<Face, Color> in) {
        this.getSuccessor().write(process(in));
    }

    private Pair<Face, Color> process(Pair<Face, Color> in) {
        Mat4 projTransform = pipelineData.getProjTransform();
        Face transformedFace = new Face(projTransform.multiply(in.fst().getV1()),
                projTransform.multiply(in.fst().getV2()),
                projTransform.multiply(in.fst().getV3()), in.fst());
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
