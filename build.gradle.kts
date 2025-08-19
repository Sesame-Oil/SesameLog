plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	id("com.github.node-gradle.node") version "5.0.0"
}

group = "com.sesame"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    runtimeOnly("com.h2database:h2") // 이 줄 추가
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    // WebFlux 사용 시: springdoc-openapi-starter-webflux-ui
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}
node {
	version.set("22.2.0")       // Node 버전
	npmVersion.set("9.8.1")     // npm 버전
	download.set(true)           // Node 설치 자동 다운로드
	workDir.set(file("${project.buildDir}/nodejs"))
	npmWorkDir.set(file("${project.buildDir}/npm"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<com.github.gradle.node.npm.task.NpmTask>("buildFrontend") {
	dependsOn("npmInstall")
	workingDir.set(file("frontend"))  // Vue 프로젝트 경로
	args.set(listOf("run", "build"))
}

tasks.register<Copy>("copyFrontend") {
	dependsOn("buildFrontend")
	from("frontend/dist")                                // 빌드 산출물
	into("src/main/resources/static")                   // Spring Boot static 폴더
}

tasks.named("bootJar") {
	dependsOn("copyFrontend")  // JAR 빌드시 Vue 빌드 자동 포함
}


