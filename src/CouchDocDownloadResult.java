public class CouchDocDownloadResult {

    private String name;
    private String docId;
    private int retryCount;

    public CouchDocDownloadResult(String name, String docId, int retryCount) {
        this.name = name;
        this.docId = docId;
        this.retryCount = retryCount;
    }

    public String getName() {
        return name;
    }

    public String getDocId() {
        return docId;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
