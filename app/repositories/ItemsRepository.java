package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.ItemsResponseDto;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.WS;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemsRepository {

    private ObjectMapper objectMapper;

    @Inject
    public ItemsRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final String BASE_URL = "https://internal-api.mercadolibre.com/sites/%s/search";
    public static final String X_CALLER_SCOPES = "X-Caller-Scopes";
    public static final String ADMIN = "admin";
    public static final String USER_ID = "seller_id";


    public F.Promise<ItemsResponseDto> get(long userId, String siteId) {
        String url = String.format(BASE_URL, siteId);

        WS.WSRequestHolder requestHolder = WS.url(url);
        requestHolder.setHeader(X_CALLER_SCOPES, ADMIN);
        requestHolder.setQueryParameter(USER_ID, String.valueOf(userId));
        requestHolder.setQueryParameter("caller.id", String.valueOf(userId));
        requestHolder.setQueryParameter("client.id", "1");

        return requestHolder.get().map(
            response -> objectMapper.readValue(response.getBody(), ItemsResponseDto.class)

        );
    }

    public F.Promise<Void> createTable() {
        try {
            // TODO: revisar en que thread pool se ejecuta este codigo
            return F.Promise.promise(() -> {
                JPA.withTransaction(() -> JPA.em()
                    .createNativeQuery("CREATE TABLE IF NOT EXISTS ITEMS (id BIGINT auto_increment,title VARCHAR(30),price DOUBLE,quantity INT)")
                    .executeUpdate());
                return null;
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
