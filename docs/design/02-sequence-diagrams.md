# 시퀀스 다이어그램

## 상품 목록 조회
sequenceDiagram
    participant User
    participant PC as ProductController
		participant PS as ProductService
    User->>PC: 조건에 맞는 상품목록 조회 요청(condition, pageable)
    PC->>PS: 상품목록 조회(condition, pageable)
    alt 상품 목록이 비어있음
    PS-->>User: 빈 상품 목록 반환
    end
    PS->>User: 200 OK + 상품 목록 반환

## 상품 상세 조회
sequenceDiagram
    participant User
    participant PC as ProductController
    participant PS as ProductService
		User->>PC: 상품 조회 요청(productId)
    PC->>PS: 상품 상세 조회(productId)
    alt 상품 목록이 비어 있음
    PS-->>User: 404 Not Found
    end
    PS->>User: 200 OK + 상품 목록 반환

## 브랜드 상세 조회
    sequenceDiagram
    participant User
    participant BC as BrandController
    participant BS as BrandService

    User->>BC: 브랜드 조회 요청(brandId)
    BC->>BS: 브랜드 조회(brandId)
    alt 브랜드 없음
    BS-->>User: 404 Not Found
	  end
    BS-->>User: 200 OK + 브랜드 정보 + 상품 목록 반환

## 좋아요 등록
sequenceDiagram
    participant User
	  participant LC as LikeController
    participant US as UserService
    participant PS as ProductService
	  participant LS as LikeService
    User ->> LC: 좋아요 요청 (productId)
    LC ->> US: 회원 조회 (X-USER-ID)
    alt 회원 없음
        US -->> User: 401 Unauthorized
    end
    US->> PS: 상품 목록 조회 (productId)
    alt 상품 없음
        PS-->> User: 404 Not Found
    end
    PS -->> LS: 좋아요 등록 (productId, userId)
    LS -->> User: 200 OK + 좋아요 반영

## 좋아요 삭제
sequenceDiagram
    participant User
	  participant LC as LikeController
    participant US as UserService
    participant PS as ProductService
	  participant LS as LikeService
    User ->> LC: 좋아요 취소 요청 (productId)
    LC ->> US: 회원 조회 (X-USER-ID)
    alt 회원 없음
        US -->> User: 401 Unauthorized
    end
    US->> PS: 상품 목록 조회 (productId)
    alt 상품 없음
        PS-->> User: 404 Not Found
    end
    PS -->> LS: 좋아요 취소 (productId, userId)
    LS -->> User: 200 OK + 좋아요 취소

## 좋아요 상품 목록 조회
sequenceDiagram
    participant User
	  participant LC as LikeController
    participant US as UserService
    participant LS as LikeService
    User ->> LC: 좋아요 한 상품 목록 요청
    LC ->> US: 회원 조회 (X-USER-ID)
    alt 회원 없음
        US -->> User: 401 Unauthorized
    end
    US ->> LS: 좋아요 목록 조회 (userId)
    LS -->> User: 200 OK + 좋아요 목록

## 주문 목록 조회
sequenceDiagram
		participant User
	  participant OC as OrderController
    participant US as UserService
	  participant OS as OrderService
    User ->> OC: 주문 목록 요청
    OC ->> US: 회원 조회 (x-USER-ID)
    alt 회원 없음
        US -->> User: 401 Unauthorized
    end
    US ->> OS: 주문 목록 조회 (userId)
    OS-->>User: 200 OK + 주문 목록 반환

## 주문 상세 조회
sequenceDiagram
    participant User
	  participant OC as OrderController
    participant US as UserService
	  participant OS as OrderService
	  participant PS as PaymentService
    User ->> OC: 주문 상세 요청 (orderId)
    OC ->> US: 회원 조회 (X-USER-ID)
    alt 회원 없음
        US -->> User: 401 Unauthorized
    end
    OC->> OS: 주문 상세 조회 (orderId)
    alt 주문 없음
        OS -->> User: 404 Not Found
    end
    OS->> PS: 결제 상세 조회 (orderId)
    PS -->> User: 200 OK + 주문 + 결제 정보

## 주문생성
sequenceDiagram
    participant User
	  participant OC as OrderController
    participant US as UserService
    participant PDS as ProductService
    participant PNS as PointService
    participant OS as OrderService
    User ->> OC: 주문 생성 요청 (productOptionsId, amount)
    OC ->> US: userId 조회 (X-USER-ID)
    alt 회원 없음
        US -->> User: 401 Unauthorized
    end
    OS ->> PDS: 상품 가격, 재고 조회 (productOptionsId)
    alt 상품 수 50개 초과
		    OS -->> User: 400 Bad Request
    end
    alt 상품 없음
        PDS -->> User: 404 Not Found
    end
    alt 상품 판매중 아님
        PDS -->> User: 400 Bad Request
    end
    alt 재고 없음
        PDS -->> User: 400 Bad Request
    end
    PDS ->> PNS: 보유 포인트 조회 (userId)
    alt 포인트 부족
        PNS -->> User: 400 Bad Request
    end
    PDS ->> PNS: 포인트 차감
    PNS ->> OS: 주문 정보 저장
    OS ->> User: 200 OK + 주문 + 결제 정보