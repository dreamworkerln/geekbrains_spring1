package jsonrpc.protocol.dto.Product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
@Scope("prototype")
public class ProductDto extends AbstractDtoPersisted {

    private String name;
    private String vCode; // Артикул

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    // ----------------------------------------------------------------------


    private Instant testDate;

    public Instant getTestDate() {
        return testDate;
    }

    public void setTestDate(Instant testDate) {
        this.testDate = testDate;
    }
}
