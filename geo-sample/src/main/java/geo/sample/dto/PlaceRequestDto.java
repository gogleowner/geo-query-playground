package geo.sample.dto;

public class PlaceRequestDto {
    private double latNorth, lonWest, latSouth, lonEast;

    public double getLatNorth() {
        return latNorth;
    }

    public void setLatNorth(double latNorth) {
        this.latNorth = latNorth;
    }

    public double getLonWest() {
        return lonWest;
    }

    public void setLonWest(double lonWest) {
        this.lonWest = lonWest;
    }

    public double getLatSouth() {
        return latSouth;
    }

    public void setLatSouth(double latSouth) {
        this.latSouth = latSouth;
    }

    public double getLonEast() {
        return lonEast;
    }

    public void setLonEast(double lonEast) {
        this.lonEast = lonEast;
    }

    @Override
    public String toString() {
        return "PlaceRequestDto{" +
                "latNorth=" + latNorth +
                ", lonWest=" + lonWest +
                ", latSouth=" + latSouth +
                ", lonEast=" + lonEast +
                '}';
    }
}
