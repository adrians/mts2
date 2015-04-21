package MTS2;

public class Job {
    protected long length;
    protected long startProcessingTimestamp;
    protected long finishProcessingTimestamp;

    public Job(long length){
        this.length = length;
    }

    public long getLength() {
        return length ;
    }

    public long getStartProcessingTimestamp() {
        return startProcessingTimestamp;
    }

    public long getFinishProcessingTimestamp() {
        return finishProcessingTimestamp;
    }

    public void setStartProcessingTimestamp(long startProcessingTimestamp) {
        this.startProcessingTimestamp = startProcessingTimestamp;
    }

    public void setFinishProcessingTimestamp(long finishProcessingTimestamp) {
        this.finishProcessingTimestamp = finishProcessingTimestamp;
    }

}
