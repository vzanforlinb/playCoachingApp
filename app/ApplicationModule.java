import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import play.Logger;
import play.libs.WS;

public class ApplicationModule extends AbstractModule {

    @Override
    public void configure() {
    //    bind(Logger.class).to(Logger.class).asEagerSingleton();
    //    bind(WS.class).to(WS.class).asEagerSingleton();
        bind(ObjectMapper.class).toProvider(this::provideObjectMapper);
    }

    @Singleton
    @Provides
    protected ObjectMapper provideObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return objectMapper;
    }

    @Singleton
    @Provides
    protected Logger.ALogger provideLogger() {
      return   new Logger.ALogger(play.api.Logger.apply("application"));
    }
}
