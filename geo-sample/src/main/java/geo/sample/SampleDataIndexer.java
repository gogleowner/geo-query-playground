package geo.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import geo.sample.dto.PlaceDocument;
import geo.sample.dto.ZigbangItem;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

public class SampleDataIndexer {

    private RestHighLevelClient elasticsearchClient =
            new RestHighLevelClient(RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));
    private ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws Exception {
//        String requestUrl = "https://apis.zigbang.com/v3/search/getItemByRect?lat_south=37.58062430845063&lat_north=37.64315402090561&lng_west=126.89122644833998&lng_east=126.9446158895186&detail=false&level=14&items=false&ad=true&room_types=[01,02,03,04,05]";
        String requestUrl = "https://apis.zigbang.com/v3/search/getItemByRect?lat_south=37.26560495001813&lat_north=37.658852804917764&lng_west=126.86796109108576&lng_east=127.30245511837498&detail=false&level=12&items=false&ad=true&room_types=[01,02,03,04,05]";

        RestTemplate restTemplate = new RestTemplate();
        ZigbangResponse result = restTemplate.getForObject(requestUrl, ZigbangResponse.class);

        System.out.println(result.mapItems.size());
        for (ZigbangItem mapItem : result.mapItems) {
            System.out.println(mapItem);
        }

        SampleDataIndexer sampleDataIndexer = new SampleDataIndexer();

        sampleDataIndexer.index(result.mapItems);

        sampleDataIndexer.close();
    }

    private void index(List<ZigbangItem> items) {
        BulkRequest bulkRequest = new BulkRequest();
        for (ZigbangItem item : items) {
            try {
                bulkRequest.add(
                        new IndexRequest("zigbang", "_doc")
                                .source(mapper.writeValueAsString(new PlaceDocument(item)), XContentType.JSON)
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulkResponse.hasFailures()) {
                throw new RuntimeException("bulk request has fail! : " + bulkResponse.buildFailureMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void close() throws Exception {
        elasticsearchClient.close();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ZigbangResponse {
        List<ZigbangItem> mapItems;

        public void setMapItems(List<ZigbangItem> mapItems) {
            this.mapItems = mapItems;
        }
    }

}
