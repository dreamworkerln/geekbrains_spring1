package jsonrpc.server;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.invoke.MethodHandles;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public enum TestSuite {

    INSTANCE;

    private Logger log;

    private String jrpcHeader = "\"id\":\"22\", \"token\":\"f229fbea-a4b9-40a8-b8ee-e2b47bc1391d\"";


    TestSuite() {
        // Load log4j
        setupLog4j();
    }




    private void setupLog4j() {

        String path_tmp = System.getProperty("user.dir") + "/" + "log_test/";
        // DON'T CALL LOGGERS BEFORE log.name SET BELOW
        System.setProperty("trace.folder.path", path_tmp + "trace.log");
        System.setProperty("info.folder.path", path_tmp + "info.log");
        System.setProperty("error.folder.path", path_tmp + "error.log");

        log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    }


    public void init() {}

    public String getJrpcHeader() {
        return jrpcHeader;
    }


//    public ApplicationContext getContext() {
//        return context;
//    }
//
//    @Autowired
//    void setContext(ApplicationContext context) {
//        this.context = context;
//    }






}