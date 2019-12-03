package jsonrpc.server.entities.base.mapper;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.server.entities.base.AbstractEntityPersisted;
import jsonrpc.utils.Utils;

import java.util.Optional;
import java.util.function.Function;

public interface IdMapper {

    interface FindById extends Function<Long,Optional<? extends AbstractEntityPersisted>>{}

    /**
     * Set id, created
     */
    default void idMap(FindById findById,
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


//    default void toCreated(
//                           AbstractDtoPersisted source,
//                           AbstractEntityPersisted target) {
//    }
}
