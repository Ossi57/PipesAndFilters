package at.fhv.sysarch.lab3.pipeline.stream;

public abstract class IStream<I, O> {
    public abstract void write(I in);
    public abstract O read();
}