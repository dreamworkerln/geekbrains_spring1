package jsonrpc.resourceserver.repository.specifications.product;

import jsonrpc.protocol.dto.product.spec.ProductSpecDto;
import jsonrpc.resourceserver.entities.product.Product;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;

public class ProductSpecBuilder {




    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Specification<Product> build(Optional<ProductSpecDto> pSpecDtoOp) {

        Specification<Product> specA = Specification.where(null);
        if (pSpecDtoOp.isPresent()) {

            ProductSpecDto p = pSpecDtoOp.get();

            final String idName = "id";
            final String priceName = "price";
            final String categoryName = "category";


            // BETWEEN
            if (p.getPriceMin() != null && p.getPriceMax() != null) {

                specA = specA.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.between(root.get(priceName), p.getPriceMin(), p.getPriceMax());
                        });
            }

            // PRICE LESS THAN MAX
            if (p.getPriceMin() == null && p.getPriceMax() != null) {

                specA = specA.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.lessThanOrEqualTo(root.get(priceName), p.getPriceMax());
                        });
            }

            // PRICE GREATER THAN MIN
            if (p.getPriceMin() != null && p.getPriceMax() == null) {

                specA = specA.and(
                        (root, query, builder) -> {
                            //query.orderBy(builder.desc(root.get(priceName)));
                            return builder.greaterThanOrEqualTo(root.get(priceName), p.getPriceMin());
                        });
            }


            // IN CATEGORY
            if (p.getCategoryList().size() > 0) {

                specA = specA.and(
                        (root, query, builder) -> {

                            return builder.in(root.get(categoryName).get("id")).value(p.getCategoryList());
                            //return builder.in(root.get("category.id")).value(p.getCategoryList());
                        });
            }




            // DEFAULT SORT  BY ID ASC
            if(p.getPriceOrderBy() == null) {

                specA = specA.and(
                        (root, query, builder) -> {
                            query.orderBy(builder.asc(root.get(idName)));
                            //noinspection ConstantConditions - not good to return null but ...
                            return null;
                        });

            }


            // SORT BY PRICE
            if(p.getPriceOrderBy()!= null) {

                specA = specA.and(
                        (root, query, builder) -> {
                            switch (p.getPriceOrderBy()){
                                case ASC:
                                    query.orderBy(builder.asc(root.get(priceName)));
                                    break;
                                case DESC:
                                    query.orderBy(builder.desc(root.get(priceName)));
                                    break;
                            }
                            //noinspection ConstantConditions - not good to return null but ...
                            return null;
                        });
            }

        }

        return specA;
    }


}



// Glitch code...

/*
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Specification<ProductN> build(Optional<ProductSpecDto> pSpecDtoOp) {

        AtomicReference<Specification<ProductN>> specA = new AtomicReference<>(Specification.where(null));


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


            if (p.getPriceOrderBy() != null) {

                specA.getAndUpdate(s -> s.and(
                        (root, query, builder) -> {
                            switch (p.getPriceOrderBy()) {
                                case ASC:
                                    query.orderBy(builder.asc(root.get(priceName)));
                                    break;
                                case DESC:
                                    query.orderBy(builder.desc(root.get(priceName)));
                                    break;
                            }
                            //noinspection ConstantConditions
                            return null;
                        }
                ));
            }

        });

        return specA.get();

    }
    */