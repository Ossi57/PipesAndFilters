package at.fhv.sysarch.lab3.pipeline.stream;



public abstract class Filter<I, O> extends IStream<I, O> {

    private IStream<O, ?> successor;
    private IStream<I, ?> predecessor;


    public Filter() {}

    public void setPredecessor(IStream<I, ?> predecessor){
        this.predecessor = predecessor;
    }

    public void setSuccessor(IStream<O, ?> successor){
        this.successor = successor;
    }


}
