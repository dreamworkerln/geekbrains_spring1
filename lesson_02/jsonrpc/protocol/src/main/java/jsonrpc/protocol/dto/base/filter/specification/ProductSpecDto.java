package jsonrpc.protocol.dto.base.filter.specification;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class ProductSpecDto {

    public enum OrderBy {ASC,DESC}

    private OrderBy priceOrderBy;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private List<Long> categoryList = new ArrayList<>();
    private Integer limit;

}
