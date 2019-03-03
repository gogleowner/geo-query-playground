package geo.sample.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import geo.sample.dto.PlaceDocument;
import geo.sample.dto.ZigbangItem;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceCrawlService {

    public Collection<PlaceDocument> crawlPlace() {
        String requestUrl = "https://apis.zigbang.com/v3/search/getItemByRect?lat_south=37.26560495001813&lat_north=37.658852804917764&lng_west=126.86796109108576&lng_east=127.30245511837498&detail=false&level=12&items=false&ad=true&room_types=[01,02,03,04,05]";

        RestTemplate restTemplate = new RestTemplate();
        ZigbangResponse result = restTemplate.getForObject(requestUrl, ZigbangResponse.class);

        System.out.println(result.mapItems.size());
        for (ZigbangItem mapItem : result.mapItems) {
            System.out.println(mapItem);
        }

        return result.mapItems.stream().map(PlaceDocument::new).collect(Collectors.toList());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ZigbangResponse {
        List<ZigbangItem> mapItems;

        public void setMapItems(List<ZigbangItem> mapItems) {
            this.mapItems = mapItems;
        }
    }


}
