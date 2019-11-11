package jsonrpc.server.entities.base.mapper;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.base.AbstractEntity;
import jsonrpc.server.entities.order.Order;
import org.modelmapper.Converter;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;


// ModelMapper: путешествие туда и обратно
// https://habr.com/ru/post/438808/
//
// https://github.com/promoscow/modelmapper-demo
public abstract class AbstractMapper<E extends AbstractEntity, D extends AbstractDto> implements Mapper<E, D> {

    private static ReentrantLock lock = new ReentrantLock();
    private static boolean baseMapInitialized = false;

    protected ModelMapper mapper;

    private Class<E> entityClass;
    private Class<D> dtoClass;

    //private TypeMap<AbstractEntity, AbstractDto> baseTypeMap;


    @SuppressWarnings("SpringJavaAutowiredMembersInspection") // Не нужно предупреждать, это абстрактный класс
    @Autowired
    public final void setMapper(ModelMapper mapper) {
        this.mapper = mapper;

        try {

            lock.lock();
            if (baseMapInitialized) {
                return;
            }
            setupBaseTypeMap();
        }
        finally {
            baseMapInitialized = true;
            lock.unlock();
        }
    }


    /**
     * Will map AbstractEntity <-> AbstractDto only 1 time at startup
     */
    private void setupBaseTypeMap() {


        System.out.println(this.getClass());
        mapper.getTypeMaps().forEach(System.out::println);
        mapper.createTypeMap(AbstractEntity.class, AbstractDto.class).addMappings(
                mapper -> {

                    mapper.skip(AbstractDto::setCreated);
                    mapper.skip(AbstractDto::setUpdated);
                });

                /* baseClass setPostConverter у меня не работает
                .setPostConverter(
                context -> {
                    System.out.println("ОЛОЛОООО!!!!!!");

                    AbstractEntity source = context.getSource();
                    AbstractDto destination = context.getDestination();

                    destination.setCreated(source.getCreated().getEpochSecond());
                    destination.setUpdated(source.getUpdated().getEpochSecond());
                    return context.getDestination();
                }
                */


        mapper.createTypeMap(AbstractDto.class, AbstractEntity.class).addMappings(
                mapper -> {

                    mapper.skip(AbstractEntity::setCreated);
                    mapper.skip(AbstractEntity::setUpdated);
                });

        /* baseClass setPostConverter у меня не работает
        .setPostConverter(
                context -> {

                    AbstractDto source = context.getSource();
                    AbstractEntity destination = context.getDestination();

                    destination.setCreated(Instant.ofEpochSecond(source.getCreated()));
                    destination.setUpdated(Instant.ofEpochSecond(source.getUpdated()));
                    return context.getDestination();
                }
        );
        */
    }




    // ----------------------------------------------------------------------



    protected AbstractMapper(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }


    /**
     * Вызывать в наследниках после создания TypeMap (в конце setupMapper())<br>
     * чтобы при маппинге работал полиморфизм
     */
    protected void includeBase(TypeMap<? extends AbstractEntity, ? extends AbstractDto> typeMapEntityToDto,
                               TypeMap<? extends AbstractDto, ? extends AbstractEntity> typeMapDtoToEntity) {

        typeMapEntityToDto.includeBase(AbstractEntity.class, AbstractDto.class);
        typeMapDtoToEntity.includeBase(AbstractDto.class, AbstractEntity.class);
    }




    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    protected Converter<E, D> getToDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    protected Converter<D, E> getToEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }



    protected void mapSpecificFields(E source, D destination) {

        destination.setCreated(source.getCreated().getEpochSecond());
        destination.setUpdated(source.getUpdated().getEpochSecond());
    }

    protected void mapSpecificFields(D source, E destination) {

        destination.setCreated(Instant.ofEpochSecond(source.getCreated()));
        destination.setUpdated(Instant.ofEpochSecond(source.getUpdated()));
    }

}
