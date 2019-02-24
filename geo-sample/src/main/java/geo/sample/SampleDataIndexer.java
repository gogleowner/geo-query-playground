package geo.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class SampleDataIndexer {


    RestHighLevelClient elasticsearchClient =
            new RestHighLevelClient(RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));
    ObjectMapper mapper = new ObjectMapper();



    public static void main(String[] args) throws Exception {
        String requestUrl = "https://apis.zigbang.com/v3/search/getItemByRect?lat_south=37.58062430845063&lat_north=37.64315402090561&lng_west=126.89122644833998&lng_east=126.9446158895186&detail=false&level=14&items=false&ad=true&room_types=[01,02,03,04,05]";

        RestTemplate restTemplate = new RestTemplate();

        ZigBangResponse result = restTemplate.getForObject(requestUrl, ZigBangResponse.class);

        System.out.println(result.mapItems.size());
        for (ZigBangItems mapItem : result.mapItems) {
            System.out.println(mapItem);
        }

        SampleDataIndexer sampleDataIndexer = new SampleDataIndexer();

        sampleDataIndexer.index(result.mapItems);
    }

    private void close() throws Exception {
        elasticsearchClient.close();
    }

    private void index(List<ZigBangItems> items) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();
        List<IndexRequest> indexRequests = items.stream()
                .map(document -> {
                    try {
                        return new IndexRequest("zigbang", "_doc")
                                .source(mapper.writeValueAsString(document), XContentType.JSON);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

//
//        for (ZigBangItems mapItem : items) {
//            bulkRequest.add(
//                    new IndexRequest("zigbang", "_doc")
//                            .source(mapper.writeValueAsString(mapItem)), XContentType.JSON);
//        }


        BulkResponse bulkResp = elasticsearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        if (bulkResp.hasFailures()) {
            System.out.println(bulkResp.buildFailureMessage());
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ZigBangResponse {

        List<ZigBangItems> mapItems;

        public void setMapItems(List<ZigBangItems> mapItems) {
            this.mapItems = mapItems;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ZigBangItems {
        private double lat, lng;
        @JsonProperty("view_count")
        private int viewCount;

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        @Override
        public String toString() {
            return "ZigBangItems{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    ", viewCount=" + viewCount +
                    '}';
        }
    }
}
