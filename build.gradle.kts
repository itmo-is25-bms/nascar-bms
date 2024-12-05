import com.google.protobuf.gradle.id
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val protobufVersion by extra("4.29.1")
val grpcVersion by extra("1.68.2")
val grpcKotlinVersion by extra("1.4.1")

plugins {
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.google.protobuf") version "0.9.4"
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.spring") version "2.1.0"
}

group = "ru.nascar"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	gradlePluginPortal()
	mavenCentral()
}


dependencies {
	// spring boot
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// spring
	implementation("org.springframework.retry:spring-retry:2.0.10")
	implementation("org.springframework:spring-aspects:6.1.14")

	// metrics
	implementation("io.micrometer:micrometer-registry-prometheus:1.14.1")

	// grpc
	implementation("net.devh:grpc-spring-boot-starter:3.1.0.RELEASE")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.google.protobuf:protobuf-java:$protobufVersion")
	implementation("io.grpc:grpc-protobuf")
	implementation("io.grpc:grpc-stub")
	implementation("io.grpc:grpc-netty")
	implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

	// utils
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// database
	implementation("org.liquibase:liquibase-core")
	runtimeOnly("org.postgresql:postgresql")

	// test block
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.dbunit:dbunit:2.8.0")
	testImplementation("com.github.springtestdbunit:spring-test-dbunit:1.3.0")
	testImplementation("io.zonky.test:embedded-postgres:2.1.0")
}

tasks.withType<KotlinCompile> {
	compilerOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget.set(JvmTarget.JVM_17)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

configure<DependencyManagementExtension> {
	imports {
		mavenBom("io.grpc:grpc-bom:$grpcVersion")
	}
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:${protobufVersion}"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
		}
		id("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
		}
	}

	generateProtoTasks {
		ofSourceSet("main").forEach { generateProtoTask ->
			generateProtoTask
				.plugins {
					id("grpc")
					id("grpckt")
				}
		}
	}
}