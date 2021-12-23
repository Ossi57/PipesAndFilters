package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.Utils;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import at.fhv.sysarch.lab3.pipeline.stream.IStreamPush;
import javafx.scene.paint.Color;

public class LightingFilter extends Filter<Pair<Face, Color>, Pair<Face, Color>> {

    private PipelineData pipelineData;

    public LightingFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }


    private Pair<Face, Color> process(Pair<Face, Color> in) {
        double dotProduct = in.fst().getN1().toVec3().dot(pipelineData.getLightPos().getUnitVector());

        if (dotProduct <= 0) {
            return (new Pair<>(in.fst(), Color.BLACK));
        }
        return new Pair<>(in.fst(), in.snd().deriveColor(0, 1, dotProduct, 1));
    }

    @Override
    public void write(Pair<Face, Color> in) {
        this.getSuccessor().write(process(in));
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
