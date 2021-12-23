package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import javafx.scene.paint.Color;

public class ColoringFilter extends Filter<Face, Pair<Face, Color>> {

    PipelineData pd;

    public ColoringFilter(PipelineData pd){
        this.pd = pd;
    }

    @Override
    public void write(Face in) {
        getSuccessor().write(process(in));
    }

    private Pair<Face, Color> process(Face in){
        return new Pair<>(in, pd.getModelColor());
    }

    @Override
    public Pair<Face, Color> read() {
        return null;
    }
}
