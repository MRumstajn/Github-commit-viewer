package rumstajn.githubcommitviewer.task;

import rumstajn.githubcommitviewer.GlobalConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {
    private static TaskManager instance;
    private final ExecutorService service;

    private TaskManager(){
        service = Executors.newFixedThreadPool(GlobalConfig.EXECUTOR_SERVICE_THREAD_COUNT);
    }

    public void runTaskLater(Runnable task){
        service.execute(task);
    }

    public static TaskManager getInstance() {
        if (instance == null){
            instance = new TaskManager();
        }
        return instance;
    }
}
