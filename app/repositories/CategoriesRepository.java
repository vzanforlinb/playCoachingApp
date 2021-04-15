package repositories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import dtos.CategoryResponseDto;
import play.libs.F;
import play.libs.WS;

import java.util.Arrays;
import java.util.List;

public class CategoriesRepository {

    private final String BASE_URL = "https://internal-api.mercadolibre.com/sites/%s/categories";
    public static final String X_CALLER_SCOPES = "X-Caller-Scopes";
    public static final String ADMIN = "admin";

    public F.Promise<List<CategoryResponseDto>> get(String siteId) {
        String url = String.format(BASE_URL, siteId);
        WS.WSRequestHolder requestHolder = WS.url(url);
        requestHolder.setHeader(X_CALLER_SCOPES, ADMIN);

        return requestHolder.get().map(
            response -> {
                // TODO: Un ObjectMapper para el proyecto
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
                objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                return Arrays.asList(objectMapper.readValue(response.getBody(), CategoryResponseDto[].class)) ;
            }
        );
    }


}
