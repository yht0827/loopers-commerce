export const PERFORMANCE_THRESHOLDS = {
    'http_req_duration': [
        'p(95)<50',    // 95% < 50ms
        'p(99)<100',   // 99% < 100ms ← 핵심 SLA
        'p(99.9)<200', // 99.9% < 200ms
    ],
    'custom_response_time': [
        'p(99)<80',    // 커스텀 p99 임계값
    ],
    'http_req_failed': ['rate<0.01'], // 에러율 1% 미만
};

export const STRESS_THRESHOLDS = {
    'http_req_duration': [
        'p(90)<300',    // 90% < 300ms
        'p(95)<500',    // 95% < 500ms
        'p(99)<1000',   // 99% < 1s
        'p(99.9)<2000', // 99.9% < 2s
    ],
    'custom_response_time': [
        'p(99)<800',    // 커스텀 p99 임계값
    ],
    'http_req_failed': ['rate<0.1'],  // 에러율 10% 미만
};