package io.github.mgluizbrito.mediaconduit_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("extract")
    public Map<String, List<ExtractorsAvailableList>> listExtractors(){

        return Map.of("Available Services:", Arrays.asList(ExtractorsAvailableList.values()));
    }
}

enum ExtractorsAvailableList{
    YOUTUBE,
}