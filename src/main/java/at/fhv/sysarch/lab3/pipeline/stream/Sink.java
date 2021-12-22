package at.fhv.sysarch.lab3.pipeline.stream;

import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Sink extends IStream<Face, GraphicsContext> {

    GraphicsContext graphicsContext;

    public Sink(GraphicsContext successor) {
        this.graphicsContext = successor;
    }

    @Override
    public void write(Face in) {
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.strokeLine(in.getV1().getX() * 100, in.getV1().getY() * 100, in.getV2().getX() * 100, in.getV2().getY() * 100);
        //todo: rendering
    }

    @Override
    public GraphicsContext read() {
        //todo: kA
        return null;
    }
}
