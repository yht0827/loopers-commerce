import http from 'k6/http';
import {sleep} from 'k6';
import {Trend} from 'k6/metrics';
import {DEFAULT_SLEEP, SORT_OPTIONS, SUMMARY_TREND_STATS} from '../../config/base.js';
import {STRESS_THRESHOLDS} from '../../config/thresholds.js';
import {buildProductsUrl, getRandomBrandId} from '../../utils/api.js';
import {checkResponse} from '../../utils/helpers.js';

const customResponseTime = new Trend('custom_response_time', true);

export const options = {
    stages: [
        {duration: '1m', target: 50},    // 1분 동안 50명까지 램프업
        {duration: '2m', target: 100},   // 2분 동안 100명까지 증가
        {duration: '3m', target: 150},   // 3분 동안 150명까지 증가 (스트레스)
        {duration: '5m', target: 200},   // 5분 동안 200명까지 증가 (고부하)
        {duration: '3m', target: 250},   // 3분 동안 250명까지 증가 (최대 부하)
        {duration: '2m', target: 100},   // 2분 동안 100명으로 감소
        {duration: '1m', target: 0},     // 1분 동안 0명으로 정리
    ],
    summaryTrendStats: SUMMARY_TREND_STATS,
    thresholds: STRESS_THRESHOLDS,
};

export default function () {
    // 다양한 시나리오를 랜덤하게 실행
    const scenarios = [
        () => testProductList(),
        () => testProductListWithBrand(),
        () => testProductListWithSort(),
        () => testProductListWithBrandAndSort(),
    ];

    const randomScenario = scenarios[Math.floor(Math.random() * scenarios.length)];
    randomScenario();

    sleep(DEFAULT_SLEEP);
}

function testProductList() {
    const url = buildProductsUrl();
    const response = http.get(url);

    checkResponse(response, {
        'products list loaded': (r) => r.body.includes('data'),
    });
    customResponseTime.add(response.timings.duration);
}

function testProductListWithBrand() {
    const brandId = getRandomBrandId();
    const url = buildProductsUrl(SORT_OPTIONS.CREATED_AT, brandId);
    const response = http.get(url);

    checkResponse(response, {
        'brand products loaded': (r) => r.body.includes('data'),
    });
    customResponseTime.add(response.timings.duration);
}

function testProductListWithSort() {
    const sortTypes = Object.values(SORT_OPTIONS);
    const randomSort = sortTypes[Math.floor(Math.random() * sortTypes.length)];
    const url = buildProductsUrl(randomSort);
    const response = http.get(url);

    checkResponse(response, {
        'sorted products loaded': (r) => r.body.includes('data'),
    });
    customResponseTime.add(response.timings.duration);
}

function testProductListWithBrandAndSort() {
    const brandId = getRandomBrandId();
    const sortTypes = Object.values(SORT_OPTIONS);
    const randomSort = sortTypes[Math.floor(Math.random() * sortTypes.length)];
    const url = buildProductsUrl(randomSort, brandId);
    const response = http.get(url);

    checkResponse(response, {
        'brand and sorted products loaded': (r) => r.body.includes('data'),
    });
    customResponseTime.add(response.timings.duration);
}