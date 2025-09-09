plugins {
    `java-library`
    `java-test-fixtures`
}

dependencies {
    api("org.springframework.kafka:spring-kafka")

    testFixturesImplementation("org.springframework.boot:spring-boot-test")
    testFixturesImplementation("org.testcontainers:kafka")
}
