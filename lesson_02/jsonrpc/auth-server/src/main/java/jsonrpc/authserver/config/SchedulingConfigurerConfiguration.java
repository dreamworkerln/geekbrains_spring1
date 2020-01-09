package jsonrpc.authserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulingConfigurerConfiguration implements SchedulingConfigurer {

    // By default all @Scheduled methods share a single thread (of same TaskPool).
    // https://stackoverflow.com/questions/29796651/what-is-the-default-scheduler-pool-size-in-spring-boot
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    // Use this in scheduling tasks:
    //
    // Augmented CompletableFuture
    // https://github.com/vsilaev/tascalate-concurrent
    // https://mvnrepository.com/artifact/net.tascalate.concurrent/net.tascalate.concurrent.lib/0.7.1
    //
    // For java8, contains:
    // .supplyAsync( () -> ... , myExecutor).orTimeout(Duration.ofSeconds(...));
    // That WILL INTERRUPT (unlike vanilla CompletableFuture in 9) thread that hang on I/O on timeout.
    // Friendly new ThreadPoolExecutor(...) wrapper (named constructor args)

}