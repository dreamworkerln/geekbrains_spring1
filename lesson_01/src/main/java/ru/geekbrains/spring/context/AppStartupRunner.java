package ru.geekbrains.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.geekbrains.spring.context.beans.Jaguar;
import ru.geekbrains.spring.context.beans.Tiger;
import ru.geekbrains.spring.context.beans.push.AlarmSender;


@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);

    private final ApplicationContext context;

    private final Jaguar jaguar;

    private final AlarmSender alarmSender;


    // @Autowired
    // field injection is not recommended, using here
    private Jaguar jaguar2;

    // field injection is not recommended, using constructor param here
    @Autowired
    public AppStartupRunner(ApplicationContext context, Jaguar jaguar,
                            @Qualifier("alarmSenderInternet") AlarmSender alarmSender) {

        this.context = context;
        this.jaguar = jaguar;
        this.alarmSender = alarmSender;
    }



    @Autowired
    public void setJaguar2(Jaguar jaguar2) {
        this.jaguar2 = jaguar2;
    }




    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Application started with option args names : {}", args.getOptionNames());

        System.out.println("\n\n---------------------------------------------");
        System.out.println("Tiger scope is prototype");


        System.out.println("---------------------------------------------");
        System.out.println("tiger vs tiger2");
        System.out.println("-------------------\n");
        Tiger tiger = context.getBean("tiger", Tiger.class);
        System.out.println(tiger.toString());
        Tiger tiger2 = context.getBean("factorizedTiger", Tiger.class);
        System.out.println(tiger2.toString());
        System.out.println(String.format("tiger == tiger2: %1$b",tiger == tiger2));
        System.out.println("tiger.equals(tiger2): " + tiger.equals(tiger2));


        System.out.println("\n\n---------------------------------------------");
        System.out.println("tiger2 vs tiger2 ");
        System.out.println("-------------------\n");
        Tiger tiger22 = context.getBean("factorizedTiger", Tiger.class);
        System.out.println(String.format("tiger2 == tiger22: %1$b", tiger2 == tiger22));
        System.out.println("tiger2.equals(tiger22): " + tiger2.equals(tiger22));
        

        System.out.println("\n\n---------------------------------------------");
        System.out.println("getBean by Tiger.class");
        System.out.println("-------------------\n");
        Tiger tiger3 = context.getBean(Tiger.class);
        System.out.println(tiger3.toString());


        System.out.println("\n\n---------------------------------------------");
        System.out.println("Jaguar is singleton");
        System.out.println("-------------------\n");
        jaguar.getColor().setBaseColor("red");
        jaguar.getColor().setTextureColor("green");
        System.out.println(jaguar.toString());
        System.out.println(jaguar2.toString());

        System.out.println(String.format("jaguar == jaguar2: %1$b", jaguar == jaguar2));
        System.out.println("jaguar.equals(jaguar2): " + jaguar.equals(jaguar2));



        System.out.println("\n\n---------------------------------------------");
        System.out.println("AlarmSender");
        System.out.println("-------------------\n");
        alarmSender.sendAlarm("Alarm!!!");



        AlarmSender alarmSender2 = context.getBean("alarmSenderGSM", AlarmSender.class);
        alarmSender2.sendAlarm("new alarm!");
        System.out.println("---------------------------------------------\n");

    }
}
