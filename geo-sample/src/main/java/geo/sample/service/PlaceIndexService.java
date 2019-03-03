package geo.sample.service;

import geo.sample.repository.ElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PlaceIndexService {
    private final PlaceCrawlService crawlService;
    private final ElasticsearchRepository repository;

    @Autowired
    public PlaceIndexService(PlaceCrawlService crawlService, ElasticsearchRepository repository) {
        this.crawlService = crawlService;
        this.repository = repository;
    }

    @PostConstruct
    public void crawlAndIndex() {
        repository.indexPlaceDocuments(crawlService.crawlPlace());
    }

}
