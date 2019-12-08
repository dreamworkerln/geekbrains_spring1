package jsonrpc.server.repository.specifications.product;

import jsonrpc.protocol.dto.base.filter.specification.ProductSpecDto;
import jsonrpc.server.entities.product.Product;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ProductSpecBuilder {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Specification<Product> build(Optional<ProductSpecDto> pSpecDtoOp) {

        Specification<Product> specA = Specification.where(null);
        if (pSpecDtoOp.isPresent()) {

            ProductSpecDto p = pSpecDtoOp.get();

            final String priceName = "price";
            final String categoryName = "category";

            // BETWEEN
            if (p.getPriceMin() != null && p.getPriceMax() != null) {

                specA.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.between(root.get(priceName), p.getPriceMin(), p.getPriceMax());
                        });
            }

            // PRICE LESS THAN MAX
            if (p.getPriceMin() == null && p.getPriceMax() != null) {

                specA.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.lessThanOrEqualTo(root.get(priceName), p.getPriceMax());
                        });
            }

            // PRICE GREATER THAN MIN
            if (p.getPriceMin() != null && p.getPriceMax() == null) {

                specA.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.greaterThanOrEqualTo(root.get(priceName), p.getPriceMin());
                        });
            }


            // IN CATEGORY
            if (p.getCategoryList().size() > 0) {

                specA.and(
                        (root, query, builder) -> {
                            //
                            return builder.in(root.get(categoryName).get("id")).value(p.getCategoryList());
                        });

            }

            // ORDER BY
            if(p.getPriceOrderBy()!= null) {

                specA.and(
                        (root, query, builder) -> {
                            switch (p.getPriceOrderBy()){
                                case ASC:
                                    query.orderBy(builder.asc(root.get(priceName)));
                                    break;
                                case DESC:
                                    query.orderBy(builder.desc(root.get(priceName)));
                                    break;
                            }
                            // Not good to return null
                            
                            //noinspection ConstantConditions
                            return null;
                        });
            }
        }

        return specA;
    }
}



//Specification<Product> ppp = Specification.where(null);
//return ppp.and((root, query, builder) -> builder.between(root.get("price"), BigDecimal.valueOf(0), BigDecimal.valueOf(20)));




/*
        AtomicReference<Specification<Product>> specA = new AtomicReference<>(Specification.where(null));


        pSpecDtoOp.ifPresent(p -> {

            //final String idName = "id";
            final String priceName = "price";
            final String categoryName = "category";

            // BETWEEN
            if (p.getPriceMin() != null && p.getPriceMax() != null) {

                specA.getAndUpdate(s -> s.and(


                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.between(root.get(priceName), p.getPriceMin(), p.getPriceMax());
                        }));

            }

            // PRICE LESS THAN MAX
            if (p.getPriceMin() == null && p.getPriceMax() != null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.lessThanOrEqualTo(root.get(priceName), p.getPriceMax());
                        }));
            }

            // PRICE GREATER THAN MIN
            if (p.getPriceMin() != null && p.getPriceMax() == null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.greaterThanOrEqualTo(root.get(priceName), p.getPriceMin());
                        }
                ));
            }


            // IN CATEGORY
            if (p.getCategoryList().size() > 0) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) ->
                                builder.in(root.get(categoryName).get("id")).value(p.getCategoryList())
                ));
            }


            if(p.getPriceOrderBy()!= null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> {
                            switch (p.getPriceOrderBy()){
                                case ASC:
                                    query.orderBy(builder.asc(root.get(priceName)));
                                    break;
                                case DESC:
                                    query.orderBy(builder.desc(root.get(priceName)));
                                    break;
                            }
                            // HAAAAAACK !!!
                            // генерирует "WHERE product.id is not null"
                            // зато добавляет сортировку
                            //return builder.isTrue(root.isNotNull());

                            //noinspection ConstantConditions
                            return null;
                        }
                ));
            }

        });

        */
