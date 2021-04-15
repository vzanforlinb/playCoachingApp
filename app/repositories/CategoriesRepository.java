package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.CategoryResponseDto;
import play.libs.F;
import play.libs.WS;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class CategoriesRepository {

    private ObjectMapper objectMapper;

    @Inject
    public CategoriesRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final String BASE_URL = "https://internal-api.mercadolibre.com/sites/%s/categories";
    public static final String X_CALLER_SCOPES = "X-Caller-Scopes";
    public static final String ADMIN = "admin";

    public F.Promise<List<CategoryResponseDto>> get(String siteId) {
        String url = String.format(BASE_URL, siteId);
        WS.WSRequestHolder requestHolder = WS.url(url);
        requestHolder.setHeader(X_CALLER_SCOPES, ADMIN);

        return requestHolder.get().map(
            response -> Arrays.asList(objectMapper.readValue(response.getBody(), CategoryResponseDto[].class))
        );
    }


}
