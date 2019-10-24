package jsonrpc.server.handlers.userinfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.Cat;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;
import jsonrpc.protocol.dto.userinfo.request.UserByColor;
import jsonrpc.protocol.dto.userinfo.request.UserByNickAndMail;
import jsonrpc.protocol.dto.userinfo.response.UserDto;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.handlers.base.MethodHandlerBase;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;


@Service
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "user")
public class UserHandler extends MethodHandlerBase {


    // curl -v POST -H "Content-Type: application/json" -d '{"id":"22", "token":"f229fbea-a4b9-40a8-b8ee-e2b47bc1391d",
    // "method": "shop.entities.user.getByNickAndMail",
    // "params":{"email":"vasya@pupkin.ru", "nickname":"kfc"}}' "http://localhost:8085/api" ; echo ""

    @JrpcHandler(method = "getByNickAndMail")
    public JrpcResponse getByNickAndMail(JsonNode params) {

        UserDto result = new UserDto();
        UserByNickAndMail request;

        try {
            request = objectMapper.treeToValue(params, UserByNickAndMail.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        // Getting from repository user by "email" and "nickname"

        result.setEmail(request.getEmail());

        result.setInfo("Бла-бла-бла бла бла бла бла ...");
        result.setDate(Instant.now());

        List<Cat> pl = new ArrayList<>();
        pl.add(new Cat("Netty"));
        pl.add(new Cat("Jetty"));
        pl.add(new Cat("Jerry"));
        pl.add(new Cat("Query"));

        result.setPets(pl);
        //response.setStatus(HttpStatus.OK);

        return result;
    }



    // curl -v POST -H "Content-Type: application/json" -d '{"id":"22", "token":"f229fbea-a4b9-40a8-b8ee-e2b47bc1391d",
    // "method": "shop.entities.user.getByColor", "params":{"color":"red"}}' "http://localhost:8085/api" ; echo ""

    @JrpcHandler(method = "getByColor")
    public JrpcResponse getByColor(JsonNode params) {

        UserDto result = new UserDto();
        UserByColor request;

        try {
            request = objectMapper.treeToValue(params, UserByColor.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        // Getting from repository user by "email" and "nickname"

        result.setEmail("123@456");

        result.setInfo("Bla-bla-bla-bla-bla ...");
        result.setDate(Instant.now());

        List<Cat> pl = new ArrayList<>();
        pl.add(new Cat("Tux"));
        pl.add(new Cat("Coi"));
        pl.add(new Cat("Tom"));
        pl.add(new Cat("Ely"));

        result.setPets(pl);
        //response.setStatus(HttpStatus.OK);

        return result;
    }

    

    //public UserInfoHandler() {}

    /*
    @Override
    public JrpcResponse handle(JrpcRequest jrpcRequest) {

        UserDto response = new UserDto();
        UserByNickAndMail request = (UserByNickAndMail) jrpcRequest;



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
    */

//    private PostDto convertToDto(Post post) {
//        PostDto postDto = modelMapper.map(post, PostDto.class);
//        postDto.setSubmissionDate(post.getSubmissionDate(),
//                userService.getCurrentUser().getPreference().getTimezone());
//        return postDto;
//    }
//
//    private Post convertToEntity(PostDto postDto) throws ParseException {
//        Post post = modelMapper.map(postDto, Post.class);
//        post.setSubmissionDate(postDto.getSubmissionDateConverted(
//                userService.getCurrentUser().getPreference().getTimezone()));
//
//        if (postDto.getId() != null) {
//            Post oldPost = postService.getPostById(postDto.getId());
//            post.setRedditID(oldPost.getRedditID());
//            post.setSent(oldPost.isSent());
//        }
//        return post;
//    }


//    @Override
//    protected Class<? extends JrpcRequest> getRequestType() {
//        return UserByNickAndMail.class;
//    }
//

//
//    @Override
//    public JrpcResponse apply(JsonNode params) {
//
//        UserDto response = new UserDto();
//
//        try {
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // parse request
//            UserByNickAndMail request = objectMapper.treeToValue(params, UserByNickAndMail.class);
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
