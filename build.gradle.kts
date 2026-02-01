plugins {
  java
  id("org.springframework.boot") version "4.0.2" apply false
  id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "ru.staygo"
version = "0.0.1"

allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "org.springframework.boot")
  apply(plugin = "io.spring.dependency-management")

  java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}
