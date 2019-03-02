package geo.sample.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZigbangItem {
    private double lat;

    @JsonSetter("lng")
    private double lon;

    @JsonSetter("view_count")
    private int viewCount;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "ZigbangItem{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", viewCount=" + viewCount +
                '}';
    }

}
