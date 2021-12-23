package at.fhv.sysarch.lab3.pipeline.stream;

public class PushPipe<I> implements IStreamPush<I, I> {

    IStreamPush<I, ?> successor;

    public PushPipe(IStreamPush<I, ?> target) {
        this.successor = target;
    }

    @Override
    public void write(I in) {
        successor.write(in);
    }
}
