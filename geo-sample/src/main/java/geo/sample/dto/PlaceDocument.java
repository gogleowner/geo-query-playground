package geo.sample.dto;

public class PlaceDocument {

    private int viewCount;
    // todo add house id, address info


    private Pin pin;

    public PlaceDocument() {}

    public PlaceDocument(ZigbangItem item) {
        this.viewCount = item.getViewCount();
        this.pin = new Pin(item.getLat(), item.getLon());
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Pin getPin() {
        return pin;
    }

    public void setPin(Pin pin) {
        this.pin = pin;
    }

    public static class Pin {
        private Location location;

        public Pin() {}

        public Pin(double lat, double lon) {
            this.location = new Location(lat, lon);
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Location {
        private double lat, lon;

        public Location() {}

        public Location(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
}
