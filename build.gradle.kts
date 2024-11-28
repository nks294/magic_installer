plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20240303")
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.jar {
    from(sourceSets.main.get().output)
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.shadowJar {
    from(sourceSets.main.get().output)
    archiveFileName.set("Magic.jar")
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

tasks.named<CreateStartScripts>("startShadowScripts") {
    dependsOn(tasks.jar)
}

tasks.named<Zip>("distZip") {
    dependsOn(tasks.shadowJar)
}

tasks.named<Tar>("distTar") {
    dependsOn(tasks.shadowJar)
}

tasks.named<CreateStartScripts>("startScripts") {
    dependsOn(tasks.shadowJar)
}

application {
    mainClass.set("nks.magic.emw.Main")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
