package jsonrpc.server;

import jsonrpc.server.configuration.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final ConfigProperties configProperties;

    @Autowired
    public AppStartupRunner(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Override
    public void run(ApplicationArguments args)  {

        System.out.println(configProperties.getHostName());
    }
}