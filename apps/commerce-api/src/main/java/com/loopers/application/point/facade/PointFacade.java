package com.loopers.application.point.facade;


import com.loopers.application.point.port.in.PointCommand;
import com.loopers.application.point.port.out.PointInfo;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    public PointInfo charge(final PointCommand command) {
        PointModel pointModel = pointService.charge(command);
        return PointInfo.from(pointModel);
    }

    public PointInfo get(final Long id) {
        PointModel pointModel = pointService.getPoint(id);
        return PointInfo.from(pointModel);
    }
}
