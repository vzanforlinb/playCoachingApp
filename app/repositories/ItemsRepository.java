package repositories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import dtos.ItemsResponseDto;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.WS;


public class ItemsRepository {

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
            response -> {
                // TODO: Un ObjectMapper para el proyecto
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

                return objectMapper.readValue(response.getBody(), ItemsResponseDto.class);
            }
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
