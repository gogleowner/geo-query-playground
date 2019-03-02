package geo.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SampleGeoDataSearcher {

    private RestHighLevelClient elasticsearchClient =
            new RestHighLevelClient(RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {



        SampleGeoDataSearcher searcher = new SampleGeoDataSearcher();

        Collection<String> documents = searcher.search(37.64315402090561d, 126.89122644833998d,
                37.58062430845063d, 126.9446158895186d);

        System.out.println("document size : " + documents.size());

        for (String document : documents) {
            System.out.println(document);
        }

        searcher.close();
    }

    private Collection<String> search(double latNorth, double lonWest,
                                      double latSouth, double lonEast) {
        SearchRequest searchRequest = new SearchRequest("zigbang");

        searchRequest.source(new SearchSourceBuilder().query(
                QueryBuilders.geoBoundingBoxQuery("pin.location")
                        .setCorners(latNorth, lonWest, latSouth, lonEast)));

        try {
            return Arrays.stream(elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits())
                    .map(SearchHit::getSourceAsString)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }
    }

    private void close() {
        try {
            elasticsearchClient.close();
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }
    }

}
