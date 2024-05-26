plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.sonarqube") version "4.4.1.3373"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

sonar {
	properties {
		property("sonar.projectKey", "AFK-3_StaffDashboard")
		property("sonar.organization", "afk-3")
		property("sonar.host.url", "https://sonarcloud.io")


		property ("sonar.test.exclusions", "src/main/java/id/ac/ui/cs/advprog/staffdashboard/service/TopUpRequestServiceImpl.java")
		property ("sonar.test.exclusions", "src/main/java/id/ac/ui/cs/advprog/staffdashboard/service/PurchaseRequestServiceImpl.java")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation ("org.springframework.boot:spring-boot-starter-actuator")
	implementation ("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")

}





tasks.register<Test>("unitTest") {
	description = "Runs unit tests."
	group = "verification"

	filter {
		excludeTestsMatching("*FunctionalTest")
	}
}

tasks.register<Test>("functionalTest") {
	description = "Runs functional tests."
	group = "verification"

	filter {
		includeTestsMatching("*FunctionalTest")
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.test {
	filter {
		excludeTestsMatching("*FunctionalTest")
	}

	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		csv.required.set(true)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}

	// Exclude classes or packages from the report
	classDirectories.setFrom(
			files(
					classDirectories.files.map {
						fileTree(it) {
							exclude(
									"id/ac/ui/cs/advprog/staffdashboard/service/PurchaseRequestServiceImpl.class",
									"id/ac/ui/cs/advprog/staffdashboard/service/TopUpRequestServiceImpl.class"
									// Add more exclusion patterns as needed
							)
						}
					}
			)
	)
}
