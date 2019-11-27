package jsonrpc.protocol.dto.product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
@Scope("prototype")
public class ProductDto extends AbstractDtoPersisted {

    private String name;
    private String vcode; // Артикул

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVcode() {return vcode;}

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    // ----------------------------------------------------------------------


    private Instant testDate;

    public Instant getTestDate() {
        return testDate;
    }

    public void setTestDate(Instant testDate) {
        this.testDate = testDate;
    }



    public static void validate(ProductDto productDto) {

        if (productDto == null) {
            throw new IllegalArgumentException("productDto == null");
        }

        if (StringUtils.isBlank(productDto.vcode)) {
            throw new IllegalArgumentException("productDto.vcode is empty");
        }

        //ToDo implement etc checks ...
    }

}
