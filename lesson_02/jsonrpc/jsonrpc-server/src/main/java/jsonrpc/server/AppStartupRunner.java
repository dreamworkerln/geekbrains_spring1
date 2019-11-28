package jsonrpc.server;

import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.service.RepositoryFakeFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final RepositoryFakeFiller repositoryFakeFiller;

    @Autowired
    public AppStartupRunner(RepositoryFakeFiller repositoryFakeFiller) {
        this.repositoryFakeFiller = repositoryFakeFiller;
    }


    @Override
    public void run(ApplicationArguments args) {

        repositoryFakeFiller.fillData();

    }
}