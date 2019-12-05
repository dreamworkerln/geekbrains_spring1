package jsonrpc.protocol.dto.base.filter.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component
public class IntervalSpecificationDto<T extends Number> extends SpecificationDto {

    private T min;
    private T max;
    private String fieldName;

    public IntervalSpecificationDto() {}

    public IntervalSpecificationDto(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public String getFieldName() {return fieldName;}
    public void setFieldName(String fieldName) {this.fieldName = fieldName;}

    public T getMin() {
        return min;
    }
    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }
    public void setMax(T max) {
        this.max = max;
    }


    public boolean validInterval() {

        return StringUtils.isNotBlank(fieldName) &&
               min != null &&
               max != null;
    }

    public boolean validFrom() {

        return StringUtils.isNotBlank(fieldName) &&
               min != null;
    }

    public boolean validTo() {

        return StringUtils.isNotBlank(fieldName) &&
               max != null;
    }

}
