import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {

        int TASKS_COUNT = 5;

        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService<CouchDocDownloadResult> completionService = new ExecutorCompletionService<>(executorService);
        final ArrayList<String> completedList = new ArrayList();
        final ArrayList<String> failedList = new ArrayList();
        int failedCounter = 0;

        for (int i = 1; i <= TASKS_COUNT; i++) {
            completionService.submit(new CouchDocDownloadTask("Task_"+i, "Doc_"+i));
        }

        for (int i = 1; i <= TASKS_COUNT; i++) {
            Future<CouchDocDownloadResult> result;
            try {
                result = completionService.take();
                System.out.println("Completed : docId : " + result.get().getDocId()
                        +  " name :" + result.get().getName() + "after  " + result.get().getRetryCount()+ " attempts.");
                completedList.add(result.get().getName());
            } catch (InterruptedException e) {
                System.out.println("InterruptedException : "+ e.getMessage());
                Thread.currentThread().interrupt();
                failedCounter++;
            } catch (ExecutionException e) {
                System.out.println("ExecutionException : "+ e.getMessage());
                if (e.getCause() instanceof IOException) {
                    // TODO Do something if required.
                }
                failedList.add(e.getMessage());
                failedCounter++;
            }
        }

        executorService.shutdown();

        System.out.println("All tasks are completed.");
        System.out.println("Success List Size : " + completedList.size());
        System.out.println("Failed List Size : " + failedList.size());
        System.out.println("failedCounter value : " + failedCounter);
    }
}
