erDiagram
    users {
        bigint id PK "사용자 ID"
        varchar name "이름"
        varchar gender "성별 (e.g. 'M', 'F')"
        date birth_date "생년월일"
        varchar email UK "이메일"
        datetime created_at "생성 일시"
        datetime updated_at "수정 일시"
    }
    points {
        bigint id PK "포인트 ID"
        bigint balance "잔액"
        datetime updated_at "마지막 수정 일시"
        bigint user_id FK "사용자 ID"
    }
    brands {
        bigint id PK "브랜드 ID"
        varchar name "이름"
        text description "설명"
        datetime created_at "생성 일시"
        datetime updated_at "수정 일시"
    }
    products {
        bigint id PK "상품 ID"
        varchar name "이름"
        bigint price "가격"
        datetime created_at "생성일 시"
        datetime updated_at "수정 일시"
        bigint brand_id FK "브랜드 ID"
    }
    product_options {
        bigint id PK "상품 옵션 ID"
        varchar name "옵션 명"
        bigint additional_price "추가 가격"
        datetime created_at "생성 일시"
        datetime updated_at "수정 일시"
        bigint product_id FK "상품 ID"
    }
    stocks {
        bigint id PK "재고 ID"
        int quantity "수량"
        datetime updated_at "마지막 입고일시"
        bigint product_option_id FK "상품 옵션 ID"
    }
    orders {
        bigint id PK "주문 ID"
        varchar orders_number "주문 번호"
        bigint total_price "총 주문 금액"
        varchar status "주문 상태 (e.g. 'PENDING', 'PAID', 'SHIPPED')"
        datetime created_at "주문 일시"
        datetime updated_at "수정 일시"
        bigint user_id FK "사용자 ID"
    }
    order_items {
        bigint id PK "주문 상품 항목 ID"
        bigint price "주문 당시 가격"
        int quantity "주문 수량"
        varchar order_id FK "주문 ID"
        bigint product_option_id FK "상품 옵션 ID"
    }
    payments {
        bigint id PK "결제 ID"
        bigint amount "결제 금액"
        varchar status "결제 상태 (e.g., 'PENDING', 'COMPLETED', 'FAILED')"
        varchar method "결제 수단 (e.g., 'CREDIT_CARD', 'BANK_TRANSFER')"
        varchar address "배송 주소"
        datetime created_at "결제 일시"
        varchar order_id FK "주문 ID"
    }
    liked_products {
        bigint user_id PK,FK "사용자 ID"
        bigint product_id PK,FK "상품 ID"
        datetime created_at "찜한 일시"
    }

    users ||--|| points : "소유"
    users ||--o{ orders : "주문"
    users ||--o{ liked_products : "좋아요"
    brands ||--o{ products : "포함"
    products ||--|{ product_options : "소유"
    products ||--o{ liked_products : "좋아요 표시"
    product_options ||--|| stocks : "참조"
    product_options ||--o{ order_items : "참조"
    orders ||--|{ order_items : "소유"
    orders ||--o| payments : "참조"