package at.fhv.sysarch.lab3.pipeline.stream;

public interface IStreamPull<I, O> {
    O read();
}
