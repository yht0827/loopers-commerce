package com.loopers.domain.point;

import com.loopers.application.point.port.in.PointCommand;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public Point charge(final PointCommand command) {
        Point point = pointRepository.findByUsersId(command.userId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, " 해당 [id = " + command.userId() + "]의 포인트가 존재하지 않습니다."));

        point.charge(command.balance());

        return pointRepository.save(point);
    }

    @Transactional(readOnly = true)
    public Point getPoint(final Long usersId) {
        return pointRepository.findByUsersId(usersId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, " 해당 [id = " + usersId + "]의 포인트가 존재하지 않습니다."));
    }
}
