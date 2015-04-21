package MTS2;

public class Job {
    protected long length;
    protected long startProcessingTimestamp;
    protected long finishProcessingTimestamp;
    protected long generatedTimestamp;

    public Job(long length, long generatedTimestamp){
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

    public long getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setStartProcessingTimestamp(long startProcessingTimestamp) {
        this.startProcessingTimestamp = startProcessingTimestamp;
    }

    public void setFinishProcessingTimestamp(long finishProcessingTimestamp) {
        this.finishProcessingTimestamp = finishProcessingTimestamp;
    }

    public void setGeneratedTimestamp(long generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }
}
