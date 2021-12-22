package at.fhv.sysarch.lab3.pipeline.stream;

import at.fhv.sysarch.lab3.obj.Face;


import java.util.List;

public class Source extends IStream<List<Face>, Face> {

    IStream<Face, ?> successor;

    public Source() {
    }

    public void setSuccessor(IStream<Face, ?> successor) {
        this.successor = successor;
    }

    @Override
    public void write(List<Face> in) {
        if(successor == null)
            return;
        for (Face face: in
             ) {
            successor.write(face);
        }
    }

    @Override
    public Face read() {
        return null;
    }
}
