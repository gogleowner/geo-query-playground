package geo.sample.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import geo.sample.dto.PlaceDocument;
import geo.sample.dto.PlaceRequestDto;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ElasticsearchRepository {
    private final RestHighLevelClient elasticsearchClient;
    private final ObjectMapper jsonObjectMapper;

    @Autowired
    public ElasticsearchRepository(RestHighLevelClient elasticsearchClient, ObjectMapper jsonObjectMapper) {
        this.elasticsearchClient = elasticsearchClient;
        this.jsonObjectMapper = jsonObjectMapper;
    }

    public List<PlaceDocument> searchPlace(PlaceRequestDto request) {
        SearchRequest searchRequest = new SearchRequest("zigbang");

        searchRequest.source(new SearchSourceBuilder().query(
                QueryBuilders.geoBoundingBoxQuery("pin.location")
                        .setCorners(request.getLatNorth(), request.getLonWest(),
                                    request.getLatSouth(), request.getLonEast())));

        try {
            return Arrays.stream(elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits())
                    .map(SearchHit::getSourceAsString)
                    .map(source -> {
                        try {
                            return jsonObjectMapper.readValue(source, PlaceDocument.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }
    }

    public void indexPlaceDocuments(Collection<PlaceDocument> documents) {
        BulkRequest bulkRequest = new BulkRequest();
        for (PlaceDocument doc : documents) {
            try {
                bulkRequest.add(
                        new IndexRequest("zigbang", "_doc")
                                .source(jsonObjectMapper.writeValueAsString(doc), XContentType.JSON)
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

            System.out.println("문서 [" + bulkResponse.getItems().length + "] 개 적재 완료");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
