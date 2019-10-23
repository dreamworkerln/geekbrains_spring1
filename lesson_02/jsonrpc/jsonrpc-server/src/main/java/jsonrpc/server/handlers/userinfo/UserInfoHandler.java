package jsonrpc.server.handlers.userinfo;

import jsonrpc.protocol.dto.Cat;
import jsonrpc.protocol.dto.base.jrpc.JrpcParam;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;
import jsonrpc.protocol.dto.userinfo.request.UserProfile;
import jsonrpc.protocol.dto.userinfo.response.UserInfo;
import jsonrpc.server.handlers.base.MethodHandlerBase;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import jsonrpc.server.handlers.base.ApiHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Component
@Scope("prototype")
@ApiHandler(method = "userInfo"/*, request = UserProfile.class*/)
public class UserInfoHandler extends MethodHandlerBase {

    public UserInfoHandler() {

    }

    @Override
    public JrpcResponse handle(JrpcParam jrpcParam) {

        UserInfo response = new UserInfo();
        UserProfile request = (UserProfile) jrpcParam;



        response.setEmail(request.getEmail());

        response.setInfo("Бла-бла-бла бла бла бла бла ...");
        response.setDate(Instant.now());

        List<Cat> pl = new ArrayList<>();
        pl.add(new Cat("Netty"));
        pl.add(new Cat("Jerry"));

        response.setPets(pl);
        //response.setStatus(HttpStatus.OK);



        return response;


    }


    @Override
    protected Class<? extends JrpcParam> getRequestType() {
        return UserProfile.class;
    }


//
//    @Override
//    public JrpcResponse apply(JsonNode params) {
//
//        UserInfo response = new UserInfo();
//
//        try {
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // parse request
//            UserProfile request = objectMapper.treeToValue(params, UserProfile.class);
//
//            response.setEmail(request.getEmail());
//
//            response.setInfo("Бла-бла-бла бла бла бла бла ...");
//            response.setDate(Instant.now());
//
//            List<Cat> pl = new ArrayList<>();
//            pl.add(new Cat("Netty"));
//            pl.add(new Cat("Jerry"));
//
//            response.setPets(pl);
//            //response.setStatus(HttpStatus.OK);
//
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException(e);
//        }
//
//        return response;
//    }

}
