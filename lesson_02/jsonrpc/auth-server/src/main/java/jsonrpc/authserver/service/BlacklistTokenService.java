package jsonrpc.authserver.service;

import jsonrpc.authserver.entities.BlacklistedToken;
import jsonrpc.authserver.entities.Token;
import jsonrpc.authserver.repository.BlacklistedTokenRepository;
import jsonrpc.protocol.http.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class BlacklistTokenService {


    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public BlacklistTokenService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    /**
     * Add access_token to blacklist
     * @param token Token
     */
    public void blacklist(Token token) {

        if (token.getType() != TokenType.ACCESS) {
            throw new IllegalArgumentException("token should be access_token type");
        }
        blacklistedTokenRepository.save(new BlacklistedToken(token.getId()));
    }


    /**
     * Cleanup blacklist from rotten tokens
     */
    public void vacuum() {

        Long beforeS = Instant.now().getEpochSecond() - TokenType.ACCESS.getTtl();
        Instant before = Instant.ofEpochSecond(beforeS);

        blacklistedTokenRepository.vacuum(before);
    }

    public Map<Long,Long> getFrom(Long from) {
       return blacklistedTokenRepository.findByIdGreaterThanEqual(from).stream()
               .collect(Collectors.toMap(BlacklistedToken::getId, BlacklistedToken::getTokenId));

    }
}




