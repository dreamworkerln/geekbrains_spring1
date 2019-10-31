package jsonrpc.protocol.dto.Product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.base.jrpc.JrpcParameter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class ProductDto extends AbstractDto {

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
}
