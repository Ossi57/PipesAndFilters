package at.fhv.sysarch.lab3.pipeline.stream;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;


import java.util.List;

public class Source implements IStreamPush<List<Face>, Face>, IStreamPull<List<Face>, Face> {

    private IStreamPush<Face, ?> successor;
    private IStreamPull<?, Face> predecessor;
    private List<Face> faces;
    private static int readCount;

    public Source(List<Face> faces) {
        this.faces = faces;
        readCount = 0;
    }

    public void setSuccessor(IStreamPush<Face, ?> successor) {
        this.successor = successor;
    }

    public void setPredecessor(IStreamPull<?, Face> predecessor) {
        this.predecessor = predecessor;
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
        //if no faces left, return delimiter face
        if(faces.size() == readCount) {
            readCount = 0;
            return new Face(new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0),
                    new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0),
                    new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0));
        }
        return faces.get(readCount++);
    }
}
