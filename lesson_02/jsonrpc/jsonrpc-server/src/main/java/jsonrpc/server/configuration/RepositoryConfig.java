package jsonrpc.server.configuration;

import jsonrpc.server.repository.base.CustomRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)

//@EnableJpaRepositories(basePackages = "jsonrpc.server.repository.base",
//        repositoryBaseClass = CustomRepositoryImpl.class)

// jsonrpc.server.repository.base;
public class RepositoryConfig {}
