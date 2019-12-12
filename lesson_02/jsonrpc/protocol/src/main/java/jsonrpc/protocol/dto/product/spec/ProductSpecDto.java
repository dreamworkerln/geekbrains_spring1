package jsonrpc.protocol.dto.product.spec;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ProductSpecDto {

    public enum OrderBy {ASC,DESC}

    // null - не сортировать
    private OrderBy priceOrderBy;

    // null - без нижней границы по цене
    private BigDecimal priceMin;

    // null - без верхней границы по цене
    private BigDecimal priceMax;

    // Если пустой список - то поиск будет осуществляться по всем категориям
    @NotNull
    private List<Long> categoryList = new ArrayList<>();

    // null - выдать сразу все товары, иначе по сколько отдавать
    private Integer limit;

}
