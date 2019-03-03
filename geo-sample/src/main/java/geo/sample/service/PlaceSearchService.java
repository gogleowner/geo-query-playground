package geo.sample.service;

import geo.sample.dto.PlaceDocument;
import geo.sample.dto.PlaceRequestDto;
import geo.sample.repository.ElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceSearchService {
    private final ElasticsearchRepository repository;

    @Autowired
    public PlaceSearchService(ElasticsearchRepository repository) {
        this.repository = repository;
    }

    public List<PlaceDocument> searchPlace(PlaceRequestDto placeRequestDto) {
        return repository.searchPlace(placeRequestDto);
    }
}
