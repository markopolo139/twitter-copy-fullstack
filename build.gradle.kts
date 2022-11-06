import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.internal.impldep.org.apache.ivy.plugins.parser.m2.PomModuleDescriptorBuilder
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    application

    id("org.springframework.boot") version "2.6.3"

    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.6.10"

    id("io.spring.dependency-management") version "1.1.0" apply false
}

group = "me.marek"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven { url = uri("https://repo.spring.io/milestone") }
}

kotlin {
    jvm {
        apply(plugin = "io.spring.dependency-management")

        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
        withJava()

    }
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }

    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-amqp")
                implementation("org.springframework.boot:spring-boot-starter-data-jpa")
                implementation("org.springframework.boot:spring-boot-starter-mail")
                implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
                implementation("org.springframework.boot:spring-boot-starter-validation")

                implementation("org.springframework.boot:spring-boot-starter-web")
                implementation("org.springframework.boot:spring-boot-starter-security")
                implementation("org.springframework.security:spring-security-messaging:5.7.1")
                implementation("org.passay:passay:1.6.1")

                implementation("org.springframework.boot:spring-boot-starter-websocket")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")

                implementation("io.jsonwebtoken:jjwt-api:0.11.5")

                implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
                implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")

                runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
                runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
                runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

                implementation("org.springframework.boot:spring-boot-starter-log4j2")
                configurations {
                    all{
                        exclude(module = "spring-boot-starter-logging")
                    }
                }
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("org.springframework.amqp:spring-rabbit-test")
                implementation("org.springframework.security:spring-security-test")
                implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("me.marek.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}