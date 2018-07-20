import java.io.IOException;
import java.util.concurrent.Callable;

public class CouchDocDownloadTask implements Callable<CouchDocDownloadResult> {

    private String docId;
    private String name;
    private int retryCount = 0;
    private static final int MAX_RETRY_LIMIT = 3;

    public CouchDocDownloadTask(String name, String docId) {
        this.docId = docId;
        this.name = name;
    }

    @Override
    public CouchDocDownloadResult call() throws Exception {

       return downloadCouchDocs();
    }

    private CouchDocDownloadResult downloadCouchDocs() throws IOException {
        retryCount++;

        System.out.println("Trying to download couch doc. Attempt : " + retryCount + " docId : " + this.docId +  " name :" + this.name);

        try {

            if( Math.random() > 0.5 ) {
                String message =  "IO Error Going to Occur docId : " + this.docId +  " name :" + this.name;
                System.out.println(message);
                throw new IOException(message); // timeout condition
            }

            return new CouchDocDownloadResult(this.name, this.docId, this.retryCount);
        } catch (Exception e) {
            System.err.printf( "IO Error Occurred docId (%s:) , name (%s:) %n", docId, name);
            if (retryCount < MAX_RETRY_LIMIT) {
                System.out.println("Going to Retry docId : " + docId +  " name :" + name);
                return downloadCouchDocs();
            } else {
                throw e;
            }
        }
    }
}
