plugins {
    kotlin("jvm") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.github.sonogram"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://papermc.io/repo/repository/maven-public")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation("com.github.monun:tap:3.3.3")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.2")
}
tasks {
    create<Copy>("copyToServer") {
        from(shadowJar)
        var dest = File(rootDir, ".server/plugins")
        if(File(rootDir, shadowJar.get().archiveFileName.get()).exists()) dest = File(dest, "update")
        into(dest)
    }
}
