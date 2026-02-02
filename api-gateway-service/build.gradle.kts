extra["springCloudVersion"] = "2025.1.1"

dependencies {
  implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webmvc")
  implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

