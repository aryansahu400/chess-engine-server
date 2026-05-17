package in.aryaura.chess.engine.server.controller;

import in.aryaura.chess.engine.server.configuration.VirtualThreadSchedulerConfiguration;
import in.aryaura.chess.engine.server.model.AnalysisRequest;
import in.aryaura.chess.engine.server.service.AnalysisService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping(value = "/analyse-move", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> analyseMove(@RequestBody AnalysisRequest request) {
        return analysisService.analysis(request).subscribeOn(VirtualThreadSchedulerConfiguration.vtScheduler);
    }
}
