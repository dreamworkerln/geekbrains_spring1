package ru.geekbrains.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);

    private final ApplicationContext context;

    @Autowired
    public AppStartupRunner(ApplicationContext context) {
        this.context = context;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Application started with option args names : {}", args.getOptionNames());


        Tiger tiger = context.getBean("tiger", Tiger.class);
        System.out.println(tiger.toString());

        Tiger tiger2 = context.getBean("tiger2", Tiger.class);
        System.out.println(tiger2.toString());

        System.out.println(tiger.getColor() == tiger2.getColor());
        System.out.println(tiger.equals(tiger2));


        Tiger tiger3 = context.getBean(Tiger.class);
        System.out.println(tiger3.toString());
    }
}
