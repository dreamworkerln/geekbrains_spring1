package jsonrpc.server.service;

import jsonrpc.server.entities.base.AbstractEntityPersisted;
import jsonrpc.server.entities.storage.StorageItem;

import java.util.Optional;

public interface AbstractService {

    Optional<? extends AbstractEntityPersisted> findById(Long id);


//    /**
//     * Upload created field to entity
//     */
//    default void save(AbstractEntityPersisted entity) {
//
//        if (entity!= null && entity.getId()!= null) {
//            AbstractEntityPersisted persisted = findById(entity.getId()).orElse(null);
//
//            if (persisted!= null) {
//                Utils.fieldSetter("created", entity, persisted.getCreated());
//                //Utils.fieldSetter("updated", entity, persisted.getUpdated());
//            }
//        }
//    }
}
