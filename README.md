# geo-elasticsearch-playground

## Geo Queries

- elasticsearch에서는 geo 데이터를 위한 두개의 field 타입을 제공한다.
    - `geo_point` : lat/lon 쌍을 지원한다.
    - `geo_shpae` : points, lines, circles, polygons, multi-polygons를 지원한다.

## Geo Hash
- 이상하게, elasticsearch에서 lat, lng으로 색인을 하면 안되는 문제가 발생한다.
- geohash 로 변환하여 색인은 가능하다. 그래서 GeoHash를 택하기로 했다.
- 문제는 geohash가 말그대로 hash 값이라서 데이터 확인하기가 어렵다.
- http://geohash.gofreerange.com 현재로선 이 사이트가 해결책인듯.. 지도를 마우스오버하면 lat, lng, geohash 모두 보여준다.
