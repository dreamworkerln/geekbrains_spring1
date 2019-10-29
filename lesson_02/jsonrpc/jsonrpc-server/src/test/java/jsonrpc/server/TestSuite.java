package jsonrpc.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//@RunWith(Runner.class)

public enum TestSuite {

    INSTANCE;

    private Logger log;

    //private String jrpcHeader = "\"id\":\"22\", \"token\":\"f229fbea-a4b9-40a8-b8ee-e2b47bc1391d\"";


    TestSuite() {
        // Load log4j
        setupLog4j();
    }

    public void init() {}


    private void setupLog4j() {


        // UBER LAME
        // (Can't execute my code before junit start spring boot in tests)
        deleteFile("${sys:error.folder.path}");
        deleteFile("${sys:info.folder.path}");
        deleteFile("${sys:trace.folder.path}");
        // -------------------------------------------------------------------------
        

        String path_tmp = System.getProperty("user.dir") + "/" + "log_test/";

        // DON'T CALL LOGGERS BEFORE log.name SET BELOW
        System.setProperty("trace.folder.path", path_tmp + "trace.log");
        System.setProperty("info.folder.path", path_tmp + "info.log");
        System.setProperty("error.folder.path", path_tmp + "error.log");

        log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    }



    private void deleteFile(String fileName) {
        Path filePath = Paths.get(System.getProperty("user.dir") + "/" + fileName);

        if (Files.exists(filePath))  {
            try {
                Files.delete(filePath);
            }
            catch (IOException ignore) {}
        }
    }

//    public String getJrpcHeader() {
//        return jrpcHeader;
//    }


//    public ApplicationContext getContext() {
//        return context;
//    }
//
//    @Autowired
//    void setContext(ApplicationContext context) {
//        this.context = context;
//    }






}