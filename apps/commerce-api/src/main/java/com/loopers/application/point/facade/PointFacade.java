package com.loopers.application.point.facade;


import com.loopers.application.point.port.in.PointCommand;
import com.loopers.application.point.port.out.PointInfo;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    public PointInfo charge(final PointCommand command) {
        Point point = pointService.charge(command);
        return PointInfo.from(point);
    }

    public PointInfo get(final Long id) {
        Point point = pointService.getPoint(id);
        return PointInfo.from(point);
    }
}
