package com.loopers.application.point;


import com.loopers.application.point.port.in.PointCommand;
import com.loopers.application.point.port.out.PointInfo;
import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    public PointInfo charge(final PointCommand command) {
        PointEntity pointEntity = pointService.charge(command);
        return PointInfo.from(pointEntity);
    }

    public PointInfo get(final Long id) {
        PointEntity pointEntity = pointService.getPoint(id);
        return PointInfo.from(pointEntity);
    }
}
