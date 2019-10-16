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
    @Primary // for getBean by Tiger.class
    public Tiger tiger(){
        return new Tiger();
    }


    @Bean
    @Scope("prototype")
    public FactoryBean<Tiger> tiger2() {

        Tiger tiger = new Tiger();
        tiger.setColor(color());
        tiger.setName("Тигра");
        tiger.getColor().setBaseColor("синий");
        tiger.getColor().setTextureColor("оранжевый");
        // Further we need to disable autowiring Tiger.color to tiger
        // or we will lost all data what we manually configured in tiger.


        // Here override @Autowired dependency injection of Tiger.color field
        // (disable DI for autowired fields (Is this disable all DI for tiger object ?) )
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

// ( it does not matter here ?)
//
//            @Override
//            public boolean isSingleton(){
//                return true;
//            }

        };
    }

}
