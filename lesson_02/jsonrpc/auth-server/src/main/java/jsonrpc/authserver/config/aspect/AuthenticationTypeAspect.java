package jsonrpc.authserver.config.aspect;


import jsonrpc.authserver.config.RequestScopeBean;
import jsonrpc.authserver.config.AuthType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class AuthenticationTypeAspect {

    private final RequestScopeBean requestScopeBean;

    public AuthenticationTypeAspect(RequestScopeBean requestScopeBean) {
        this.requestScopeBean = requestScopeBean;
    }

    @Before("@annotation(jsonrpc.authserver.config.aspect.ValidAuthenticationType)")
    public void validateAuthenticationType(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ValidAuthenticationType foundAnnotation = AnnotationUtils.findAnnotation(method, ValidAuthenticationType.class);

        Set<AuthType> validAuthTypes = new HashSet<>(Arrays.asList(foundAnnotation.value()));

        AuthType currentAuthType = requestScopeBean.getAuthType();

        if (!validAuthTypes.contains(currentAuthType)) {
            throw new AccessDeniedException("Unauthenticated");
        }
    }

}


