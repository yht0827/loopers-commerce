import { check } from 'k6';

export function checkResponse(response, customChecks = {}) {
    const defaultChecks = {
        'status is 200': (r) => r.status === 200,
        'p99 goal met': (r) => r.timings.duration < 100,
    };
    
    return check(response, { ...defaultChecks, ...customChecks });
}