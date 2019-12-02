package jsonrpc.server;


import jsonrpc.server.entities.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;
import java.lang.invoke.MethodHandles;
import java.util.Locale;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static Order orr;

    private static Logger log;

    public static void main(String[] args) {

        Locale.setDefault(Locale.ROOT);

        // SET DECIMAL SEPARATOR TO "."
        //Locale.setDefault(new Locale("en", "US"));




        setupLog4j();

        log.info("\n\n" +
                 "==========================================================================\n" +
                 "================================= STARTUP ================================\n" +
                 "==========================================================================\n");

        SpringApplication.run(Application.class, args);

    }


    @PreDestroy
    public void onDestroy(){

        log.info("\n\n" +
                 "**************************************************************************\n" +
                 "************************* APPLICATION TERMINATING ************************\n" +
                 "**************************************************************************\n");
    }

    @SuppressWarnings("Duplicates")
    private static void setupLog4j() {

        String path_tmp = System.getProperty("user.dir") + "/" + "log/";
        // DON'T CALL LOGGERS BEFORE log.name SET BELOW
        System.setProperty("trace.folder.path", path_tmp + "trace.log");
        System.setProperty("info.folder.path", path_tmp + "info.log");
        System.setProperty("error.folder.path", path_tmp + "error.log");

        log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    }
}
