package geo.sample.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlaceSearchApplicationConfiguration {
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }

}
