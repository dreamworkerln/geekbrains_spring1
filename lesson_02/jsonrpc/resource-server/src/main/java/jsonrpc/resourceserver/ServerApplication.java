package jsonrpc.resourceserver;


import jsonrpc.resourceserver.repository.base.CustomRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.annotation.PreDestroy;
import java.lang.invoke.MethodHandles;
import java.util.Locale;

@SpringBootApplication
// делаем нашу реализацию репо-интерфейса базовым для Spring Data JPA,
// Spring будет генерить для него методы доступа(Find.One.By.Parent.Address и т.д.)
// См мануал по @NoRepositoryBean
// (providing an extended base interface for all repositories
// in combination with a custom repository base class)
//
// + в CustomRepositoryImpl сделан метод refresh()
// (идет вызов entityManager.refresh(entity)), позволяющий обновить кеш hibernate.
// Это нужно при сохранении сущности с @OneToMany(cascade = CascadeType.ALL)
//
// Когда сохраняется сущность, где для ее дочерних объектов указаны только id,
// (хотя эти дочерние объекты уже существуют в базе и все их поля полностью заполнены)
// то при обращении к этой сущности после сохранения в текущей транзакции
// у ее детей все поля будут null, кроме, соответственно, id.
//
// Это происходит потому, что идет обращение к кешу hibernate.
// И пока не завершишь транзакцию, то
// через сущность до полностью подгруженных ее детей не достучишься.
// Зато если после save() сделать refresh(), то пойдет запрос к базе и все данные детей подтянутся
// и можно будет нормально бродить по полноценному графу объектов.
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EnableScheduling
public class ServerApplication {

    public static boolean SLEEP_IN_TRANSACTION = false;

    //public static Order orr;

    private static Logger log = null;


    public static void main(String[] args) {

        Locale.setDefault(Locale.ROOT);

        // SET DECIMAL SEPARATOR TO "."
        //Locale.setDefault(new Locale("en", "US"));

        setupLog4j();

        SpringApplication.run(ServerApplication.class, args);

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
