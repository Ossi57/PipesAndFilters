package at.fhv.sysarch.lab3.pipeline.stream;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Sink extends IStream<Pair<Face, Color>, GraphicsContext> {

    GraphicsContext graphicsContext;

    public Sink(GraphicsContext successor) {
        this.graphicsContext = successor;
    }

    @Override
    public void write(Pair<Face, Color> in) {
        int multi = 100;
        graphicsContext.setStroke(in.snd());
        graphicsContext.strokeLine(in.fst().getV1().getX() * multi,
                in.fst().getV1().getY() * multi,
                in.fst().getV2().getX() * multi, in.fst().getV2().getY() * multi);
        //todo: rendering
    }

    @Override
    public GraphicsContext read() {
        //todo: kA
        return null;
    }
}
