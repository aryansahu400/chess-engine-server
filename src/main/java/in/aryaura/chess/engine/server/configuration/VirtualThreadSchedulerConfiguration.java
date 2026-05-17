package in.aryaura.chess.engine.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadSchedulerConfiguration {

    private Scheduler virtualThreadScheduler;
    private ExecutorService virtualThreadExecutorService;

    public static final Scheduler vtScheduler = Schedulers.fromExecutor(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("s vt-", 0).factory()));

    @Bean
    public ExecutorService getVirtualThreadExecutor() {
        ExecutorService virtualThreadExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("vt-", 0).factory());
        virtualThreadExecutorService = virtualThreadExecutor;
        return virtualThreadExecutor;
    }

    @Bean
    public Scheduler virtualThreadScheduler() {
        virtualThreadScheduler = Schedulers.fromExecutor(virtualThreadExecutorService);
        return virtualThreadScheduler;
    }


}