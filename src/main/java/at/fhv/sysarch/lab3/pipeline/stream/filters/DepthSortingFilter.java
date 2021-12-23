package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;

public class DepthSortingFilter extends Filter<Face, Face> {
    @Override
    public void write(Face in) {
        //not possible
        return;
    }

    @Override
    public Face read() {
        return null;
    }
}
