import http from 'k6/http';
import {sleep} from 'k6';
import {Trend} from 'k6/metrics';
import {DEFAULT_SLEEP, SORT_OPTIONS, SUMMARY_TREND_STATS} from '../../config/base.js';
import {PERFORMANCE_THRESHOLDS} from '../../config/thresholds.js';
import {buildProductsUrl, getRandomBrandId} from '../../utils/api.js';
import {checkResponse} from '../../utils/helpers.js';

const customResponseTime = new Trend('custom_response_time', true);

export const options = {
    vus: 1,
    duration: '30s',
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