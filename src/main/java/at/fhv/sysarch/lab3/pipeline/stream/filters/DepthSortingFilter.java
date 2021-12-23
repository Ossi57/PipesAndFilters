package at.fhv.sysarch.lab3.pipeline.stream.filters;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Utils;
import at.fhv.sysarch.lab3.pipeline.stream.Filter;
import com.hackoeur.jglm.Vec4;

import java.util.Comparator;
import java.util.PriorityQueue;

public class DepthSortingFilter extends Filter<Face, Face> {

    private boolean isWaiting;
    private PriorityQueue<Face> queue;

    public DepthSortingFilter() {
        queue = new PriorityQueue<>(new Comparator<Face>() {
            @Override
            public int compare(Face o1, Face o2) {
                double o1ZAvg = (o1.getV1().getZ() + o1.getV2().getZ() + o1.getV3().getZ()) / 3d;
                double o2ZAvg = (o2.getV1().getZ() + o2.getV2().getZ() + o2.getV3().getZ()) / 3d;
                return o1ZAvg < o2ZAvg ? -1 : 1;
            }
        });
        isWaiting = true;
    }

    @Override
    public void write(Face in) {
        //not possible, do nothing
        getSuccessor().write(in);
    }

    @Override
    public Face read() {
        while(isWaiting){
            Face out = getPredecessor().read();
            if(out != null){
                isWaiting = !Utils.checkIfDelimiterIsReached(out);
                if(isWaiting)
                    queue.add(out);
            }
        }
        return process();
    }

    public Face process(){
        if(queue.size() == 0){
            //reset isWaiting for next frame
            isWaiting = true;
            return new Face(new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0),
                new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0),
                new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0));
        }
        return queue.poll();
    }
}
