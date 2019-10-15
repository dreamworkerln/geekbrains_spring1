package ru.geekbrains.spring.context;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfiguration {

    @Bean
    @Scope("prototype")
    public Color color(){
        return new Color();
    }


    @Bean
    @Scope("prototype")
    @Primary
    public Tiger tiger(){
        return new Tiger();
    }



    @Bean
    @Scope("prototype")
    public FactoryBean<Tiger> tiger2() {

        Tiger t = new Tiger();
        t.setColor(color());
        t.setName("Тигра");
        t.getColor().setBaseColor("синий");
        t.getColor().setTextureColor("оранжевый");


        // here override @Autowired dependency injection of t.color
        // (disable DI for autowired fields)
        return new FactoryBean<Tiger>()
        {
            @Override
            public Tiger getObject() {
                return t;
            }

            @Override
            public Class<?> getObjectType(){
                return Tiger.class;
            }

            @Override
            public boolean isSingleton(){
                return true;
            }
        };
    }


}
