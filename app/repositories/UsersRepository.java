package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.UserResponseDto;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.WS;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UsersRepository {

    private ObjectMapper objectMapper;

    @Inject
    public UsersRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final String BASE_URL = "https://internal-api.mercadolibre.com/users/";
    public static final String X_CALLER_SCOPES = "X-Caller-Scopes";
    public static final String ADMIN = "admin";

    public F.Promise<UserResponseDto> get(long userId) {
        String url = BASE_URL + userId;
        WS.WSRequestHolder requestHolder = WS.url(url);
        requestHolder.setHeader(X_CALLER_SCOPES, ADMIN);

        return requestHolder.get().map(
            response -> objectMapper.readValue(response.getBody(), UserResponseDto.class)
        );
    }

    public F.Promise<Void> createTable() {
        try {
            // TODO: revisar en que thread pool se ejecuta este codigo
            return F.Promise.promise(() -> {
                JPA.withTransaction(() -> JPA.em()
                    .createNativeQuery("CREATE TABLE IF NOT EXISTS USERS (id BIGINT auto_increment,name VARCHAR(30),site_id DOUBLE,quantity INT)")
                    .executeUpdate());
                return null;
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
