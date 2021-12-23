package at.fhv.sysarch.lab3.pipeline.stream;

public interface IStreamPush<I, O> {
    void write(I in);
}