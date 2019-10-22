package ru.geekbrains.spring.context;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import ru.geekbrains.spring.context.beans.Color;
import ru.geekbrains.spring.context.beans.Tiger;

@Configuration
public class AppConfiguration {

    @Bean
    @Scope("prototype")
    public Color color(){
        return new Color();
    }



    @Bean
    @Scope("prototype")
    @Primary // for getBean by Tiger.class
    public Tiger tiger(){
        return new Tiger();
    }


    @Bean("mutatedTiger")
    @Scope("prototype")
    public FactoryBean<Tiger> tiger2() {

        Tiger tiger = new Tiger();
        tiger.setColor(color());
        tiger.setName("Тигра");
        tiger.getColor().setBaseColor("синий");
        tiger.getColor().setTextureColor("оранжевый");


        // Further we need to disable autowiring Tiger.color to new created tiger
        // or we will lost all data what we manually configured in it.
        //
        // Here we override @Autowired dependency injection of Tiger.color field
        // (disable DI for autowired fields) by using FactoryBean interface.
        //
        // So Spring container uses the object produced by the our FactoryBean
        // instead of itself for dependency injection.

        // How to use the Spring FactoryBean?
        // https://www.baeldung.com/spring-factorybean


        return new FactoryBean<Tiger>()
        {
            @Override
            public Tiger getObject() {
                return tiger;
            }

            @Override
            public Class<?> getObjectType(){
                return Tiger.class;
            }

            @Override
            public boolean isSingleton(){
                return false;
            }
        };
    }

}
