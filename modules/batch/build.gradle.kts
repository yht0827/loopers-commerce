plugins {
    `java-library`
}

dependencies {
    api(project(":modules:jpa"))
    api(project(":modules:metrics"))

    api("org.springframework.boot:spring-boot-starter-batch")
    api("org.springframework.boot:spring-boot-starter-jdbc")
}

