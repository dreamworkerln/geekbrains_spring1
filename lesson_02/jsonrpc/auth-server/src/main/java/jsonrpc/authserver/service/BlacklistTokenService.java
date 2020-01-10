package jsonrpc.authserver.service;

import jsonrpc.authserver.entities.BlacklistedToken;
import jsonrpc.authserver.entities.token.AccessToken;
import jsonrpc.authserver.repository.BlacklistedTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.Map;
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
     * @param token AccessToken
     */
    public void blacklist(AccessToken token) {
        blacklistedTokenRepository.save(new BlacklistedToken(token.getId(), token.getExpiredAt()));
    }


//    /**
//     * Cleanup blacklist from rotten token
//     */
//    public void vacuum() {
//
//        Long beforeS = Instant.now().getEpochSecond() - TokenType.ACCESS.getTtl();
//        Instant before = Instant.ofEpochSecond(beforeS);
//
//        blacklistedTokenRepository.vacuum(before);
//    }

    /**
     * Get all blacklisted tokens from position to now
     */
    public Map<Long,Long> getFrom(Long from) {
       return blacklistedTokenRepository.findByIdGreaterThanEqual(from).stream()
               .collect(Collectors.toMap(BlacklistedToken::getId, BlacklistedToken::getTokenId));
    }

    public void vacuum() {
        blacklistedTokenRepository.vacuum();
    }

}




