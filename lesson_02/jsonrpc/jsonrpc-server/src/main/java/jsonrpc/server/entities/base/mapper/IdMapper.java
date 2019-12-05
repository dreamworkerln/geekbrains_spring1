package jsonrpc.server.entities.base.mapper;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.server.entities.base.AbstractEntityPersisted;
import jsonrpc.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.function.Function;

public abstract class IdMapper {



    // Просто вложенный функциональный интерфейс
    //
    // Function<Long,Optional<? extends AbstractEntityPersisted>>
    // сюда передается  из к-л маппера SomeEntityMapper метод из соответствующего сервиса SomeEntity.findById(Long.id)
    // который выдает сущность по id и мы навешиваем из существующей сущности (если она есть) к-л поля
    // в данном случае created, created, которые не передаются в DTO.
    public interface FindById extends Function<Long,Optional<? extends AbstractEntityPersisted>>{}

    /**
     * Set id, created, created
     */
    public void idMap(FindById findById,
                       AbstractDtoPersisted source,
                       AbstractEntityPersisted target) {

        Utils.fieldSetter("id", target, source.getId());

        // Насколько медленно эта срань будет работать, лазая за каждой сущностью в базу,
        // посмотреть дату создания и изменения ....
        if (source.getId() != null) {

            findById.apply(source.getId()).ifPresent(o -> {
                Utils.fieldSetter("created", target, o.getCreated());
                Utils.fieldSetter("updated", target, o.getUpdated());
            });
        }
    }
}
