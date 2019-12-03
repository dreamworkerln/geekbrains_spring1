package jsonrpc.server.service.other;

import org.springframework.stereotype.Component;

// Треш-технологии

@Component
public class CrutchService {

    private static CrutchService INSTANCE;

    public static CrutchService INSTANCE() {
        return INSTANCE;
    }

    protected CrutchService() {

        CrutchService.INSTANCE = this;
    }
}
