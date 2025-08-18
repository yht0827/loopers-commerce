plugins {
    `java-library`
    `java-test-fixtures`
}


dependencies {
    api("org.springframework.boot:spring-boot-starter-cache")
    api("com.github.ben-manes.caffeine:caffeine")
}