package geo.sample.controller;

import geo.sample.dto.PlaceDocument;
import geo.sample.dto.PlaceRequestDto;
import geo.sample.service.PlaceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceSearchController {
    private final PlaceSearchService placeSearchService;

    @Autowired
    public PlaceSearchController(PlaceSearchService placeSearchService) {
        this.placeSearchService = placeSearchService;
    }

    @GetMapping("/search")
    public List<PlaceDocument> searchPlace(@ModelAttribute PlaceRequestDto placeRequestDto) {
        return placeSearchService.searchPlace(placeRequestDto);
    }

}
