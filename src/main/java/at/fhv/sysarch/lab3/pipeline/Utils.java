package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec4;

public class Utils {
    public static boolean checkIfDelimiterIsReached(Face out){
        return (out.getV1().getX() == 0 && out.getV1().getY() == 0 && out.getV1().getZ() == 0
                && out.getV2().getX() == 0 && out.getV2().getY() == 0 && out.getV2().getZ() == 0
                && out.getV3().getX() == 0 && out.getV3().getY() == 0 && out.getV3().getZ() == 0);
    }

    public static Face getDelimiterFace() {
        return new Face(new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0),
                new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0),
                new Vec4(0, 0, 0, 0), new Vec4(0, 0, 0, 0));
    }
}
