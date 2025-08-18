import http from 'k6/http';
import {sleep} from 'k6';
import {Rate, Trend} from 'k6/metrics';
import {SORT_OPTIONS, SUMMARY_TREND_STATS} from '../../config/base.js';
import {buildProductsUrl, getRandomBrandId} from '../../utils/api.js';
import {checkResponse} from '../../utils/helpers.js';

const customResponseTime = new Trend('custom_response_time', true);
const errorRate = new Rate('error_rate');

export const options = {
    stages: [
        {duration: '2m', target: 100},   // 워밍업: 2분 동안 100명
        {duration: '5m', target: 200},   // 부하 증가: 5분 동안 200명
        {duration: '10m', target: 300},  // 스트레스: 10분 동안 300명
        {duration: '5m', target: 400},   // 극한 부하: 5분 동안 400명
        {duration: '10m', target: 500},  // 최대 부하: 10분 동안 500명
        {duration: '5m', target: 200},   // 부하 감소: 5분 동안 200명
        {duration: '2m', target: 0},     // 정리: 2분 동안 0명
    ],
    summaryTrendStats: SUMMARY_TREND_STATS,
    thresholds: {
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
        'error_rate': ['rate<0.1'],       // 커스텀 에러율 10% 미만
    },
};

export default function () {
    // 가중치를 둔 시나리오 실행 (실제 사용 패턴 모방)
    const random = Math.random();

    if (random < 0.4) {
        // 40% - 기본 상품 목록 조회
        testProductList();
    } else if (random < 0.7) {
        // 30% - 브랜드별 상품 조회
        testProductListWithBrand();
    } else if (random < 0.9) {
        // 20% - 정렬된 상품 조회
        testProductListWithSort();
    } else {
        // 10% - 복합 조건 조회
        testComplexScenario();
    }

    // 랜덤 대기 시간 (0.5초 ~ 2초)
    sleep(Math.random() * 1.5 + 0.5);
}

function testProductList() {
    const url = buildProductsUrl();
    const response = http.get(url);

    const success = checkResponse(response, {
        'products list loaded': (r) => r.body.includes('data'),
        'response time acceptable': (r) => r.timings.duration < 1000,
    });

    if (!success) {
        errorRate.add(1);
    } else {
        errorRate.add(0);
    }

    customResponseTime.add(response.timings.duration);
}

function testProductListWithBrand() {
    const brandId = getRandomBrandId();
    const url = buildProductsUrl(SORT_OPTIONS.CREATED_AT, brandId);
    const response = http.get(url);

    const success = checkResponse(response, {
        'brand products loaded': (r) => r.body.includes('data'),
        'has brand filter': (r) => r.url.includes(`brandId=${brandId}`),
    });

    if (!success) {
        errorRate.add(1);
    } else {
        errorRate.add(0);
    }

    customResponseTime.add(response.timings.duration);
}

function testProductListWithSort() {
    const sortTypes = Object.values(SORT_OPTIONS);
    const randomSort = sortTypes[Math.floor(Math.random() * sortTypes.length)];
    const url = buildProductsUrl(randomSort);
    const response = http.get(url);

    const success = checkResponse(response, {
        'sorted products loaded': (r) => r.body.includes('data'),
        'has sort parameter': (r) => r.url.includes(`sort=${randomSort}`),
    });

    if (!success) {
        errorRate.add(1);
    } else {
        errorRate.add(0);
    }

    customResponseTime.add(response.timings.duration);
}

function testComplexScenario() {
    const brandId = getRandomBrandId();
    const sortTypes = Object.values(SORT_OPTIONS);
    const randomSort = sortTypes[Math.floor(Math.random() * sortTypes.length)];

    // 연속적인 API 호출로 실제 사용자 행동 모방
    const scenarios = [
        () => {
            // 시나리오 1: 상품 목록 -> 브랜드 필터 -> 정렬
            http.get(buildProductsUrl());
            sleep(0.5);
            http.get(buildProductsUrl(SORT_OPTIONS.CREATED_AT, brandId));
            sleep(0.5);
            const response = http.get(buildProductsUrl(randomSort, brandId));
            return response;
        },
        () => {
            // 시나리오 2: 정렬된 목록 -> 다른 정렬 -> 브랜드 필터
            http.get(buildProductsUrl(SORT_OPTIONS.PRICE_ASC));
            sleep(0.3);
            http.get(buildProductsUrl(SORT_OPTIONS.LIKES_DESC));
            sleep(0.3);
            const response = http.get(buildProductsUrl(SORT_OPTIONS.LIKES_DESC, brandId));
            return response;
        }
    ];

    const randomScenario = scenarios[Math.floor(Math.random() * scenarios.length)];
    const response = randomScenario();

    const success = checkResponse(response, {
        'complex scenario completed': (r) => r.body.includes('data'),
    });

    if (!success) {
        errorRate.add(1);
    } else {
        errorRate.add(0);
    }

    customResponseTime.add(response.timings.duration);
}