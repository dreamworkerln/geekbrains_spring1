package jsonrpc.protocol.dto.base.filter.specification;

import java.math.BigDecimal;

public class PriceSpecificationDto extends IntervalSpecificationDto<BigDecimal> {

    public PriceSpecificationDto() {
    }

    public PriceSpecificationDto(BigDecimal min, BigDecimal max) {
        super(min, max);
    }
}
