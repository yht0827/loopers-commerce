import { BASE_URL, BRAND_ID_RANGE, SORT_OPTIONS } from '../config/base.js';

export function getRandomBrandId() {
    const { min, max } = BRAND_ID_RANGE;
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

export function buildProductsUrl(sort = SORT_OPTIONS.CREATED_AT, brandId = null) {
    const url = new URL('/products', BASE_URL);
    url.searchParams.set('sort', sort);
    
    if (brandId) {
        url.searchParams.set('brandId', brandId);
    }
    
    return url.toString();
}