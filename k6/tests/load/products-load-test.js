import http from 'k6/http';
import {sleep} from 'k6';
import {Trend} from 'k6/metrics';
import {DEFAULT_SLEEP, SORT_OPTIONS, SUMMARY_TREND_STATS} from '../../config/base.js';
import {PERFORMANCE_THRESHOLDS} from '../../config/thresholds.js';
import {buildProductsUrl, getRandomBrandId} from '../../utils/api.js';
import {checkResponse} from '../../utils/helpers.js';

const customResponseTime = new Trend('custom_response_time', true);

export const options = {
    stages: [
        {duration: '30s', target: 1},   // 1명으로 시작
        {duration: '2m', target: 20},   // 3분 동안 20명까지 증가
        {duration: '1m', target: 20},   // 1분 유지
        {duration: '30s', target: 0},   // 정리
    ],
    summaryTrendStats: SUMMARY_TREND_STATS,
    thresholds: PERFORMANCE_THRESHOLDS,
};

export default function () {
    const brandId = getRandomBrandId();
    const url = buildProductsUrl(SORT_OPTIONS.CREATED_AT, brandId);

    const response = http.get(url);

    checkResponse(response);
    customResponseTime.add(response.timings.duration);

    sleep(DEFAULT_SLEEP);
}