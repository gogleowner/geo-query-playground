package geo.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
        for (ZigBangItem mapItem : result.mapItems) {
            System.out.println(mapItem);
        }

        SampleDataIndexer sampleDataIndexer = new SampleDataIndexer();

        sampleDataIndexer.index(result.mapItems);

        sampleDataIndexer.close();
    }

    private void close() throws Exception {
        elasticsearchClient.close();
    }

    private void index(List<ZigBangItem> items) {
        List<IndexRequest> indexRequests = items.stream()
                .map(ZigBangDocument::new)
                .map(document -> {
                    try {
                        System.out.println(mapper.writeValueAsString(document));

                        return new IndexRequest("zigbang", "_doc")
                                .source(mapper.writeValueAsString(document), XContentType.JSON);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        indexRequests.forEach(req -> {
            try {
                elasticsearchClient.index(req, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ZigBangResponse {

        List<ZigBangItem> mapItems;

        public void setMapItems(List<ZigBangItem> mapItems) {
            this.mapItems = mapItems;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ZigBangItem {
        private double lat, lng;

        @JsonSetter("view_count")
        private int viewCount;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public int getViewCount() {
            return viewCount;
        }

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
            return "ZigBangItem{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    ", viewCount=" + viewCount +
                    '}';
        }
    }

    public static class ZigBangDocument {
        private int viewCount;
        @JsonProperty("pin")
        private Pin pin;

        public ZigBangDocument() {}

        public ZigBangDocument(ZigBangItem item) {
            this.viewCount = item.viewCount;


            // TODO lat, lng must change to geohash
            // References : GeoHashUtils , http://geohash.co


            this.pin = new Pin(item.lat, item.lng);
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public static class Pin {
            Location location;

            public Pin() {}

            public Pin(double lat, double lng) {
                this.location = new Location(lat, lng);
            }

            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }
        }

        public static class Location {
            private double lat, lng;

            public Location() {}

            public Location(double lat, double lng) {
                this.lat = lat;
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }
    }

}
