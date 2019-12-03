package jsonrpc.server.controller.jrpc.zspecifications;

import jsonrpc.server.entities.product.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceSpecification {

    public Specification<Product> from(String field, BigDecimal value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
    }

    public Specification<Product> to(String field, BigDecimal value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(field), value);
    }

    public Specification<Product> between(String field, BigDecimal min, BigDecimal max) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(field), min, max);
    }

}
