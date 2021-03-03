plugins {
    id("org.openjfx.javafxplugin") version "0.0.9"
    application
}

tasks.compileJava {
    options.release.set(11)
}

repositories {
    maven { url = uri("https://www.macrofocus.com/archiva/repository/public/") }
    maven { url = uri("https://www.macrofocus.com/archiva/repository/snapshots/") }
    mavenCentral()
}

javafx {
    version = "12"
    modules("javafx.graphics")
}

val frameworkAttribute = Attribute.of("mkui", String::class.java)
configurations.all {
    afterEvaluate {
        attributes.attribute(frameworkAttribute, "javafx")
    }
}
dependencies {
    val kotlinVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    val javaFxVersion: String by project
    val osName = System.getProperty("os.name").toLowerCase()
    val openjfxPlatform = if(osName.contains("mac")) "mac" else if(osName.contains("win")) "win" else if(osName.contains("linux")) "linux" else null
    implementation("org.openjfx:javafx-base:$javaFxVersion:$openjfxPlatform")
    implementation("org.openjfx:javafx-graphics:$javaFxVersion:$openjfxPlatform")

    val macrofocusVersion: String by project
    implementation("org.macrofocus:macrofocus-common:$macrofocusVersion")
    implementation("org.molap:molap:$macrofocusVersion")
    implementation("org.macrofocus:mkui:$macrofocusVersion")
    implementation("com.treemap:treemap:$macrofocusVersion")
}

application {
    mainClass.set("Demo")
}

distributions {
    main {
//        distributionBaseName.set("someName")
        contents {
//            from("TreeMap API for Java-JavaFX Developer Guide.pdf")

            from("../treemap/build/dokka/html/") {
                into("dokka")
            }
        }
    }
}