package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import at.fhv.sysarch.lab3.utils.MatrixUtils;

public class BackfaceCullingFilter extends Filter<Face, Face> {

    @Override
    public void write(Face in) {
        if(!process(in))
            return;
        else
            getSuccessor().write(in);
    }

    private boolean process(Face input){
        return input.getV1().dot(input.getN1()) < 0
                && input.getV2().dot(input.getN2()) < 0
                && input.getV3().dot(input.getN3()) < 0;
    }

    @Override
    public Face read() {
        return null;
    }
}
