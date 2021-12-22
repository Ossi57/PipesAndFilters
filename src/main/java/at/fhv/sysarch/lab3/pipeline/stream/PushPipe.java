package at.fhv.sysarch.lab3.pipeline.stream;

public class PushPipe<I> extends IStream<I, I> {

    IStream<I, I> successor;

    public PushPipe(IStream<I, I> target) {
        this.successor = target;
    }

    @Override
    public void write(I in) {
        successor.write(in);
    }

    @Override
    public I read() {
        return null;
    }
}
