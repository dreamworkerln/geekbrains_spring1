package jsonrpc.server;

import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.server.service.RepositoryFakeFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final RepositoryFakeFiller repositoryFakeFiller;

    private final JrpcRequest jrpcRequest;



    @Autowired
    public AppStartupRunner(RepositoryFakeFiller repositoryFakeFiller, JrpcRequest jrpcRequest) {
        this.repositoryFakeFiller = repositoryFakeFiller;
        this.jrpcRequest = jrpcRequest;
    }


    @Override
    public void run(ApplicationArguments args) {

        repositoryFakeFiller.fillData();

    }
}