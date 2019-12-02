package jsonrpc.server.entities.base.mapper;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.server.entities.base.AbstractEntityPersisted;
import jsonrpc.server.service.AbstractService;
import jsonrpc.utils.Utils;

import java.util.Optional;

public abstract class AbstractMapper {

    /**
     * Set id, created
     */
    protected void postMap(AbstractService service,
                                 AbstractDtoPersisted source,
                                 AbstractEntityPersisted target) {

        // 1. Set id
        Utils.fieldSetter("id", target, source.getId());


        // 2. Set created and updated from server (if have this entity persisted)
        if (target.getId() != null) {

            Optional<? extends AbstractEntityPersisted> persisted = service.findById(target.getId());

            persisted.ifPresent(p -> {
                Utils.fieldSetter("created", target, p.getCreated());
                // не обязательно, хибер там потом все равно обновит updated, но пускай будет для точности
                Utils.fieldSetter("updated", target, p.getUpdated());
            });
        }
    }
}
