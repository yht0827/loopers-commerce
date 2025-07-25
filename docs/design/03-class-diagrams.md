# 클래스 다이어그램

## 상품 목록 조회
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Brands {
    Long id
    String name
    String description
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    }

    class LikedProducts {
    Long user_id
    Long product_id
    Product product
    }

    Products "N" --> Brands: 참조
    LikedProducts "N" --> Products: 참조
    LikedProducts "N" --> Users: 참조
    Users ..> LikedProducts: 좋아요

## 상품 상세 조회
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Brands {
    Long id
    String name
    String description
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    List<ProductOptions> productOptions
    }

    class ProductOptions {
    Long id
    String name
    Long additionalPrice
    }

    class LikedProducts {
    Long user_id
    Long product_id
    Product product
    }

    class Stocks {
    Long id
    Long quantity
    }

    Products "N" --> Brands: 참조
    Products --> "N" ProductOptions: 소유
    Stocks --> ProductOptions: 참조
    LikedProducts "N" --> Products: 참조
    LikedProducts "N" --> Users: 참조
    Users ..> LikedProducts: 좋아요

## 브랜드 상세 조회
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Brands {
    Long id
    String name
    String description
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    List<ProductOptions> productOptions
    }

    class LikedProducts {
    Long user_id
    Long product_id
    Product product
    }

    Products "N" --> Brands: 참조
    LikedProducts "N" --> Products: 참조
    LikedProducts "N" --> Users: 참조
    Users ..> LikedProducts: 좋아요

## 좋아요 등록
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    List<ProductOptions> productOptions
    }

    class LikedProducts {
    Long user_id
    Long product_id
    Product product
    }

    LikedProducts "N" --> Products: 참조
    LikedProducts "N" --> Users: 참조
    Users ..> LikedProducts: 좋아요


## 좋아요 삭제
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    List<ProductOptions> productOptions
    }

    class LikedProducts {
    Long user_id
    Long product_id
    Product product
    }

    LikedProducts "N" --> Products: 참조
    LikedProducts "N" --> Users: 참조
    Users ..> LikedProducts: 좋아요

## 좋아요 상품 목록 조회
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    List<ProductOptions> productOptions
    }

    class LikedProducts {
    Long user_id
    Long product_id
    Product product
    }

    LikedProducts "N" --> Products: 참조
    LikedProducts "N" --> Users: 참조
    Users ..> LikedProducts: 좋아요

## 주문 목록 조회
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Orders {
    Long id
    String orders_number
    Long total_price
    Status status
    List<OrderItems> OrderItems
    }

    class OrderItems {
    Long id
    Long price
    Long quantity
    }

    Orders "N" --> Users: 참조
    Orders --> "N" OrderItems: 소유
    Users ..> Orders: 주문

## 주문 상세 조회
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Orders {
    Long id
    String orders_number
    Long total_price
    Status status
    List OrderItems
    }

    class OrderItems {
    Long id
    Long price
    Long quantity
    }

    class Payments {
    Long id
    Long amount
    String status
    String method
    String address
    }

    Orders "N" --> Users: 참조
    Orders --> "N" OrderItems: 소유
    Users ..> Orders: 주문
    Payments --> Orders: 주문 완료
    Users ..> "N" Payments: 결제

## 주문생성
classDiagram
    class Users {
    Long id
    String name
    Gender gender
    LocalDate birthDate
    String email
    }

    class Orders {
    Long id
    String orders_number
    Long total_price
    Status status
    List OrderItems
    }

    class Products {
    Long id
    String name
    Long price
    Brand brand
    List<ProductOptions> productOptions
    }

    class ProductOptions {
    Long id
    String name
    Long additionalPrice
    }

    class OrderItems {
    Long id
    Long price
    Long quantity
    }

    class Points {
    Long ProductId
    Long balance
    +use(amount)
    }

    class Stocks {
    Long id
    Long quantity
    }

    Users ..> Orders: 주문
    Products --> "N" ProductOptions: 소유
    Stocks -->  ProductOptions: 참조
    Orders "N" --> Users: 참조
    Orders --> "N" OrderItems: 소유
    OrderItems "N" --> ProductOptions: 참조
    Points --> Users: 참조
