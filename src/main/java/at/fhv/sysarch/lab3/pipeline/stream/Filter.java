package at.fhv.sysarch.lab3.pipeline.stream;



public abstract class Filter<I, O> implements IStreamPush<I, O>, IStreamPull<I, O> {

    private IStreamPush<O, ?> successor;
    private IStreamPull<?, I> predecessor;


    public Filter() {}

    public void setPredecessor(IStreamPull<?, I> predecessor){
        this.predecessor = predecessor;
    }

    public void setSuccessor(IStreamPush<O, ?> successor){
        this.successor = successor;
    }

    public IStreamPush<O, ?> getSuccessor() {
        return successor;
    }

    public IStreamPull<?, I> getPredecessor() {
        return predecessor;
    }
}
