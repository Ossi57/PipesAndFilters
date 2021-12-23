package at.fhv.sysarch.lab3.pipeline.stream;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.Utils;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

public class Sink implements IStreamPush<Pair<Face, Color>, PipelineData>, IStreamPull<Pair<Face, Color>, Pair<Face, Color>> {

    PipelineData pipelineData;
    IStreamPull<?, Pair<Face, Color>> predecessor;

    public Sink(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public void write(Pair<Face, Color> in) {
        process(in);
    }

    private void process(Pair<Face, Color> in){
        pipelineData.getGraphicsContext().setStroke(in.snd());
        pipelineData.getGraphicsContext().setFill(in.snd());
        switch(pipelineData.getRenderingMode()){
            case POINT:
                pipelineData.getGraphicsContext().strokeLine(in.fst().getV1().getX(), in.fst().getV1().getY(),
                        in.fst().getV1().getX(), in.fst().getV1().getY());
                pipelineData.getGraphicsContext().strokeLine(in.fst().getV2().getX(), in.fst().getV2().getY(),
                        in.fst().getV2().getX(), in.fst().getV2().getY());
                pipelineData.getGraphicsContext().strokeLine(in.fst().getV3().getX(), in.fst().getV3().getY(),
                        in.fst().getV3().getX(), in.fst().getV3().getY());
                break;
            case FILLED:
                pipelineData.getGraphicsContext().fillPolygon(
                        new double[]{in.fst().getV1().getX(), in.fst().getV2().getX(), in.fst().getV3().getX()},
                        new double[]{in.fst().getV1().getY(), in.fst().getV2().getY(), in.fst().getV3().getY()},
                        3
                );
                pipelineData.getGraphicsContext().strokePolygon(
                        new double[]{in.fst().getV1().getX(), in.fst().getV2().getX(), in.fst().getV3().getX()},
                        new double[]{in.fst().getV1().getY(), in.fst().getV2().getY(), in.fst().getV3().getY()},
                        3
                );
                break;
            case WIREFRAME:
                pipelineData.getGraphicsContext().strokePolygon(
                        new double[]{in.fst().getV1().getX(), in.fst().getV2().getX(), in.fst().getV3().getX()},
                        new double[]{in.fst().getV1().getY(), in.fst().getV2().getY(), in.fst().getV3().getY()},
                        3
                );
                break;

        }
    }

    @Override
    public Pair<Face, Color> read() {
        while(true){
            Pair<Face, Color> out = predecessor.read();
            if(out != null){
                if(Utils.checkIfDelimiterIsReached(out.fst())) {
                    return out;
                }
                else{
                    process(out);
                }
            }
        }
    }

    public void setPredecessor(IStreamPull<?, Pair<Face, Color>> predecessor) {
        this.predecessor = predecessor;
    }
}
