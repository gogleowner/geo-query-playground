package geo.sample;

import org.elasticsearch.common.geo.GeoHashUtils;
import org.junit.Test;

public class GeoHashEqualityTest {
    @Test
    public void geoHashTest() {
        System.out.println(GeoHashUtils.stringEncode(123, 30));
        System.out.println(GeoHashUtils.stringEncode(130, 40));
    }
}
