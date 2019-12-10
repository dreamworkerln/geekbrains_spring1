package jsonrpc.server.repository.base;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;


// НЕ ЗАБУДЬ И НА ИНТЕРФЕЙС CustomRepository ПОВЕСИТЬ АННОТАЦИЮ @NoRepositoryBean
// А ТО НЕ ВЗЛЕТИТ !
@NoRepositoryBean
public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements CustomRepository<T, ID> {

    private final EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        //noinspection unchecked
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void refresh(T t) {
        entityManager.refresh(t);
    }

//    @Override
//    @Transactional
//    public T merge(T t) {
//        return entityManager.merge(t);
//    }
}



