package com.loopers.support.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductsInserter {

	private static final String[] PRODUCT_NAMES = {
		"iPhone 14", "iPhone 15", "Galaxy S23", "Galaxy S24", "Pixel 7", "Pixel 8",
		"MacBook Pro", "Galaxy Book", "Surface Laptop", "iPad Pro", "Galaxy Tab",
		"AirPods Pro", "Galaxy Buds", "Apple Watch", "Galaxy Watch"
	};

	public static void execute(int recordCount) throws Exception {
		System.out.println("=== products 테이블 데이터 삽입 시작 ===");

		String csvFilePath = "products_data.csv";

		try (Connection conn = DriverManager.getConnection(
			TableSpecificBulkInsert.JDBC_URL,
			TableSpecificBulkInsert.USERNAME,
			TableSpecificBulkInsert.PASSWORD)) {

			// 1. 테이블 준비
			prepareProductsTable(conn);

			// 2. 브랜드 ID 목록 가져오기
			List<Long> brandIds = getBrandIds(conn);
			if (brandIds.isEmpty()) {
				throw new SQLException("brands 테이블에 데이터가 없습니다. brands 테이블을 먼저 실행하세요.");
			}

			// 3. CSV 생성
			generateProductsCSV(csvFilePath, recordCount, brandIds);

			// 4. 데이터 삽입
			insertProductsData(conn, csvFilePath, recordCount, brandIds);

			// 5. 결과 확인
			verifyProductsData(conn);

			// 6. 정리
			new File(csvFilePath).delete();

		} catch (Exception e) {
			System.err.println("products 테이블 삽입 실패: " + e.getMessage());
			throw e;
		}
	}

	private static void prepareProductsTable(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			System.out.println("📋 products 테이블 준비 중...");

			String createTableSQL = """
				CREATE TABLE IF NOT EXISTS products (
				    id BIGINT AUTO_INCREMENT PRIMARY KEY,
				    brand_id BIGINT NOT NULL,
				    like_count INT DEFAULT 0,
				    price DECIMAL(10,2) NOT NULL,
				    quantity INT NOT NULL,
				    name VARCHAR(255) NOT NULL,
				    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
				    FOREIGN KEY (brand_id) REFERENCES brands(id)
				) ENGINE=InnoDB
				""";

			stmt.execute(createTableSQL);
			System.out.println("✅ products 테이블 준비 완료");
		}
	}

	private static List<Long> getBrandIds(Connection conn) throws SQLException {
		List<Long> brandIds = new ArrayList<>();

		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT id FROM brands ORDER BY id");
			while (rs.next()) {
				brandIds.add(rs.getLong("id"));
			}
		}

		System.out.printf("📊 사용 가능한 브랜드 수: %d개%n", brandIds.size());
		return brandIds;
	}

	private static void generateProductsCSV(String filePath, int recordCount, List<Long> brandIds) throws IOException {
		System.out.println("📝 products CSV 파일 생성 중...");

		Random random = new Random();
		long startTime = System.currentTimeMillis();

		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			for (int i = 0; i < recordCount; i++) {
				Long brandId = brandIds.get(random.nextInt(brandIds.size()));
				int likeCount = random.nextInt(10000); // 0-9999
				double price = (random.nextInt(200) + 1) * 100; // 100-20000
				int quantity = random.nextInt(100) + 1; // 1-100
				String productName = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)] + "_" + i;

				writer.printf("%d,%d,%.2f,%d,%s%n", brandId, likeCount, price, quantity, productName);

				if (i > 0 && i % 100000 == 0) {
					System.out.printf("  진행률: %.1f%% (%,d건)%n", (i * 100.0) / recordCount, i);
				}
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.printf("✅ products CSV 생성 완료 - %,d건, %.1f초%n",
			recordCount, (endTime - startTime) / 1000.0);
	}

	private static void insertProductsData(Connection conn, String csvFilePath, int recordCount, List<Long> brandIds) throws
		SQLException {
		try {
			// LOAD DATA LOCAL INFILE 시도
			loadProductsDataLocalInfile(conn, csvFilePath);

		} catch (SQLException e) {
			System.out.println("LOAD DATA 실패, Batch Insert 사용: " + e.getMessage());
			batchInsertProducts(conn, recordCount, brandIds);
		}
	}

	private static void loadProductsDataLocalInfile(Connection conn, String csvFilePath) throws SQLException {
		String sql = String.format("""
			LOAD DATA LOCAL INFILE '%s' 
			INTO TABLE products 
			FIELDS TERMINATED BY ',' 
			LINES TERMINATED BY '\\n'
			(brand_id, like_count, price, quantity, name)
			""", csvFilePath);

		long startTime = System.currentTimeMillis();

		try (Statement stmt = conn.createStatement()) {
			conn.setAutoCommit(false);
			int rowsAffected = stmt.executeUpdate(sql);
			conn.commit();

			long endTime = System.currentTimeMillis();
			System.out.printf("✅ products LOAD DATA 완료 - %,d건, %.1f초%n",
				rowsAffected, (endTime - startTime) / 1000.0);
		}
	}

	private static void batchInsertProducts(Connection conn, int recordCount, List<Long> brandIds) throws SQLException {
		String sql = "INSERT INTO products (brand_id, like_count, price, quantity, name) VALUES (?, ?, ?, ?, ?)";
		int batchSize = 10000;
		Random random = new Random();

		long startTime = System.currentTimeMillis();
		conn.setAutoCommit(false);

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < recordCount; i++) {
				Long brandId = brandIds.get(random.nextInt(brandIds.size()));
				int likeCount = random.nextInt(10000);
				double price = (random.nextInt(200) + 1) * 100;
				int quantity = random.nextInt(100) + 1;
				String productName = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)] + "_" + i;

				pstmt.setLong(1, brandId);
				pstmt.setInt(2, likeCount);
				pstmt.setDouble(3, price);
				pstmt.setInt(4, quantity);
				pstmt.setString(5, productName);
				pstmt.addBatch();

				if (i > 0 && i % batchSize == 0) {
					pstmt.executeBatch();
					pstmt.clearBatch();
					System.out.printf("  진행률: %.1f%%\n", (i * 100.0) / recordCount);
				}
			}

			pstmt.executeBatch();
			conn.commit();

			long endTime = System.currentTimeMillis();
			System.out.printf("✅ products Batch Insert 완료 - %,d건, %.1f초%n",
				recordCount, (endTime - startTime) / 1000.0);
		}
	}

	private static void verifyProductsData(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products");
			if (rs.next()) {
				System.out.printf("📊 products 총 레코드: %,d건%n", rs.getLong(1));
			}

			rs = stmt.executeQuery("""
				SELECT p.id, b.brand_name, p.name, p.price, p.quantity 
				FROM products p 
				JOIN brands b ON p.brand_id = b.id 
				ORDER BY p.id LIMIT 5
				""");

			System.out.println("📋 products 샘플 데이터:");
			while (rs.next()) {
				System.out.printf("  ID: %d, 브랜드: %s, 제품: %s, 가격: %.0f, 수량: %d%n",
					rs.getLong(1), rs.getString(2), rs.getString(3),
					rs.getDouble(4), rs.getInt(5));
			}
		}
	}
}
