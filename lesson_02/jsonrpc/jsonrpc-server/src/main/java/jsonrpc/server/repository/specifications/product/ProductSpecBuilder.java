package jsonrpc.server.repository.specifications.product;

import jsonrpc.protocol.dto.base.filter.specification.ProductSpecDto;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.specifications.base.SearchOperation;
import jsonrpc.server.repository.specifications.base.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ProductSpecBuilder {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Specification<Product> build(Optional<ProductSpecDto> pSpecDtoOp) {

        AtomicReference<Specification<Product>> specA = new AtomicReference<>(Specification.where(null));

        pSpecDtoOp.ifPresent(p -> {

            final String priceName = "price";
            final String categoryName = "category";

            // BETWEEN
            if (p.getPriceMin() != null && p.getPriceMax() != null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> builder.between(root.get(priceName), p.getPriceMin(), p.getPriceMax())
                ));

            }

            // PRICE LESS THAN MAX
            if (p.getPriceMin() == null && p.getPriceMax() != null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> builder.lessThanOrEqualTo(root.get(priceName), p.getPriceMax())
                ));
            }

            // PRICE GREATER THAN MIN
            if (p.getPriceMin() != null && p.getPriceMax() == null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(priceName), p.getPriceMin())
                ));
            }


            // IN CATEGORY
            if (p.getCategoryList().size() > 0) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> builder.in(root.get(categoryName).get("id")).value(p.getCategoryList())
                ));
            }
        });
        return specA.get();

        //Specification<Product> ppp = Specification.where(null);

        //return ppp.and((root, query, builder) -> builder.between(root.get("price"), BigDecimal.valueOf(0), BigDecimal.valueOf(20)));
    }
}
