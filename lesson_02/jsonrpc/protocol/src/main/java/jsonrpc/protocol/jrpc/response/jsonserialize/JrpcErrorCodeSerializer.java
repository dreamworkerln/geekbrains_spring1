package jsonrpc.protocol.jrpc.response.jsonserialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jsonrpc.protocol.jrpc.response.JrpcErrorCode;

import java.io.IOException;

// https://www.baeldung.com/jackson-custom-serialization
public class JrpcErrorCodeSerializer extends StdSerializer<JrpcErrorCode> {

    public JrpcErrorCodeSerializer() {
        this(null);
    }

    public JrpcErrorCodeSerializer(Class<JrpcErrorCode> t) {
        super(t);
    }

    @Override
    public void serialize(
            JrpcErrorCode value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

        jgen.writeNumber(value.value());
//        jgen.writeStartObject();
//        jgen.writeNumberField("id", value.value();
//        jgen.writeStringField("itemName", value.itemName);
//        jgen.writeNumberField("owner", value.owner.id);
//        jgen.writeEndObject();
    }
}