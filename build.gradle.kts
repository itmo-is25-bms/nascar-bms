import com.google.protobuf.gradle.*
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val protobufVersion by extra("3.21.12")
val grpcVersion by extra("1.53.0")
val grpcKotlinVersion by extra("1.3.0")

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.google.protobuf") version "0.9.2"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
}

group = "ru.nascar"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	gradlePluginPortal()
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-parent:3.0.4")
	implementation("org.springframework.boot:spring-boot-maven-plugin:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.google.protobuf:protobuf-java:$protobufVersion")
	implementation("io.grpc:grpc-protobuf")
	implementation("io.grpc:grpc-stub")
	implementation("io.grpc:grpc-netty")
	implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
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