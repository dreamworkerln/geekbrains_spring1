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

    private final Jaguar jaguar;

    // @Autowired
    // field injection is not recommended, use constructor param
    private Jaguar jaguar2;

    @Autowired
    public AppStartupRunner(ApplicationContext context, Jaguar jaguar) {
        this.context = context;
        this.jaguar = jaguar;
    }


    public Jaguar getJaguar2() {
        return jaguar2;
    }

    @Autowired
    public void setJaguar2(Jaguar jaguar2) {
        this.jaguar2 = jaguar2;
    }
    

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Application started with option args names : {}", args.getOptionNames());


        Tiger tiger = context.getBean("tiger", Tiger.class);
        System.out.println(tiger.toString());

        Tiger tiger2 = context.getBean("tiger2", Tiger.class);
        System.out.println(tiger2.toString());

        System.out.println(tiger == tiger2);
        System.out.println(tiger.equals(tiger2));


        Tiger tiger3 = context.getBean(Tiger.class);
        System.out.println(tiger3.toString());


        System.out.println("\n---------------------------------------------\n");
        System.out.println("Jaguar is singleton");
        System.out.println("-------------------\n");

        jaguar.getColor().setBaseColor("red");
        jaguar.getColor().setTextureColor("green");
        System.out.println(jaguar.toString());
        System.out.println(jaguar2.toString());

        System.out.println(jaguar == jaguar2);
        System.out.println(jaguar.equals(jaguar2));
    }
}
