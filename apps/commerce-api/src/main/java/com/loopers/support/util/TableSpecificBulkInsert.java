package com.loopers.support.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TableSpecificBulkInsert {

	// 공통 JDBC 설정
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/loopers?" +
		"allowLoadLocalInfile=true&" +
		"allowLoadLocalInfileInPath=&" +
		"rewriteBatchedStatements=true&" +
		"useSSL=false&" +
		"allowPublicKeyRetrieval=true";

	static final String USERNAME = "application";
	static final String PASSWORD = "application";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n=== 테이블별 대용량 데이터 삽입 시스템 ===");
			System.out.println("1. brands 테이블 데이터 삽입");
			System.out.println("2. products 테이블 데이터 삽입");
			System.out.println("3. 모든 테이블 순차 실행");
			System.out.println("4. 테이블 구조 확인");
			System.out.println("5. 종료");
			System.out.print("선택하세요 (1-5): ");

			String choice = scanner.nextLine().trim();

			try {
				switch (choice) {
					case "1":
						System.out.print("삽입할 브랜드 개수를 입력하세요: ");
						int brandCount = Integer.parseInt(scanner.nextLine().trim());
						BrandsInserter.execute(brandCount);
						break;

					case "2":
						System.out.print("삽입할 제품 개수를 입력하세요: ");
						int productCount = Integer.parseInt(scanner.nextLine().trim());
						ProductsInserter.execute(productCount);
						break;

					case "3":
						System.out.print("각 테이블에 삽입할 데이터 개수를 입력하세요: ");
						int allCount = Integer.parseInt(scanner.nextLine().trim());
						executeAll(allCount);
						break;

					case "4":
						checkTableStructures();
						break;

					case "5":
						System.out.println("프로그램을 종료합니다.");
						return;

					default:
						System.out.println("잘못된 선택입니다. 1-5 중에서 선택하세요.");
				}

			} catch (Exception e) {
				System.err.println("오류 발생: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 모든 테이블 순차 실행
	 */
	public static void executeAll(int recordCount) throws Exception {
		System.out.println("\n=== 모든 테이블 순차 실행 ===");

		// 1. brands 테이블 먼저 실행 (products가 참조하므로)
		System.out.println("\n1️⃣ brands 테이블 데이터 삽입 시작...");
		BrandsInserter.execute(recordCount);

		// 2. products 테이블 실행
		System.out.println("\n2️⃣ products 테이블 데이터 삽입 시작...");
		ProductsInserter.execute(recordCount);

		System.out.println("\n✅ 모든 테이블 데이터 삽입 완료!");
	}

	/**
	 * 테이블 구조 확인
	 */
	public static void checkTableStructures() throws SQLException {
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			System.out.println("\n=== 테이블 구조 확인 ===");

			// brands 테이블 구조
			System.out.println("\n📋 brands 테이블:");
			showTableStructure(conn, "brands");

			// products 테이블 구조
			System.out.println("\n📋 products 테이블:");
			showTableStructure(conn, "products");

			// 데이터 개수 확인
			System.out.println("\n📊 현재 데이터 개수:");
			showRecordCount(conn, "brands");
			showRecordCount(conn, "products");
		}
	}

	private static void showTableStructure(Connection conn, String tableName) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("DESCRIBE " + tableName);
			while (rs.next()) {
				System.out.printf("  %s: %s%s%s%n",
					rs.getString("Field"),
					rs.getString("Type"),
					"YES".equals(rs.getString("Null")) ? " (NULL 허용)" : " (NOT NULL)",
					rs.getString("Key").isEmpty() ? "" : " [" + rs.getString("Key") + "]"
				);
			}
		} catch (SQLException e) {
			System.out.println("  테이블이 존재하지 않습니다: " + tableName);
		}
	}

	private static void showRecordCount(Connection conn, String tableName) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
			if (rs.next()) {
				System.out.printf("  %s: %,d건%n", tableName, rs.getLong(1));
			}
		} catch (SQLException e) {
			System.out.println("  " + tableName + ": 테이블 없음");
		}
	}

}
