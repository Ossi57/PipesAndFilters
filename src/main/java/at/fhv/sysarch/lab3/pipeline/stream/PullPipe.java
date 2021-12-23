package at.fhv.sysarch.lab3.pipeline.stream;

public class PullPipe<O> implements IStreamPull<O, O> {

    IStreamPull<?, O> predecessor;
    public PullPipe(IStreamPull<?, O> target) {
        this.predecessor = target;
    }

    @Override
    public O read() {
        return predecessor.read();
    }
}
