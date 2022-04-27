plugins {
    kotlin("jvm") version "1.6.10"
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

fun getArchString(): String {
    var osArch = System.getProperty("os.arch");
    osArch = osArch.toLowerCase();
    print(osArch)
    if ("i386" == osArch || "x86" == osArch || "i686" == osArch) {
        return "x86";
    } else if (osArch.startsWith("amd64") || osArch.startsWith("x86_64")) {
        return "x86_64";
    } else if (osArch.startsWith("arm64")) {
        return "arm64";
    } else if (osArch.startsWith("arm")) {
        return "arm";
    } else if ("ppc" == osArch || "powerpc" == osArch) {
        return "ppc";
    } else if (osArch.startsWith("ppc")) {
        return "ppc_64";
    } else if (osArch.startsWith("sparc")) {
        return "sparc";
    } else if (osArch.startsWith("mips64")) {
        return "mips64";
    } else if (osArch.startsWith("mips")) {
        return "mips";
    } else if (osArch.contains("risc")) {
        return "risc";
    }
    return "unknown";
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    val cudaClassifier = "windows-${getArchString()}"
    implementation("org.jcuda:jcuda:11.2.0") {
        isTransitive = false
    }
    implementation("org.jcuda:jcuda-natives:11.2.0:$cudaClassifier")

    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}