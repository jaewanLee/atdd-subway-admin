package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.application.SectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private SectionService sectionService;

    public LineService(LineRepository lineRepository,
                       SectionService sectionService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        sectionService.saveSection(request.getUpStationId(), request.getDownStationId(), request.getDistance(), persistLine);
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    public LineResponse getLine(long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 노선을 찾을 수 없습니다."));
        return LineResponse.of(line);
    }

    public LineResponse updateLine(long id, LineRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 노선을 찾을 수 없습니다."));
        line.update(request.toLine());
        return LineResponse.of(line);
    }

    public void delete(long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 노선을 찾을 수 없습니다."));
        lineRepository.delete(line);
    }

}
