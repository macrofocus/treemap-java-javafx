plugins {
    id("org.openjfx.javafxplugin") version "0.1.0"
    application
}

tasks.compileJava {
    options.release.set(21)
}

repositories {
    maven { url = uri("https://archiva.macrofocus.com/repository/public/") }
    maven { url = uri("https://archiva.macrofocus.com/repository/snapshots/") }
    mavenCentral()

    // Jetpack Compose
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev/") }

    google()
}

javafx {
    val javaFxVersion: String by project
    version = javaFxVersion
    modules("javafx.graphics", "javafx.controls")
}

//val frameworkAttribute = Attribute.of("mkui", String::class.java)
//configurations.all {
//    afterEvaluate {
//        attributes.attribute(frameworkAttribute, "javafx")
//    }
//}
dependencies {
    val localDependencies: String? by project
    if(localDependencies != null && localDependencies.toBoolean()) {
        implementation(project(":macrofocus-common"))
        implementation(project(":molap"))
        implementation(project(":mkui"))
        implementation(project(":treemap"))
    } else {
        val macrofocusVersion: String by project
        implementation("org.macrofocus:macrofocus-common:$macrofocusVersion")
        implementation("org.molap:molap:$macrofocusVersion")
        implementation("org.mkui:mkui-javafx:$macrofocusVersion")
        implementation("com.treemap:treemap-javafx:$macrofocusVersion")
    }
}

application {
    mainClass.set("Demo")
}

distributions {
    main {
//        distributionBaseName.set("someName")
        contents {
            from(".") {
                exclude(".gradle/**")
                exclude("build/**")
                exclude("yarn.lock")
            }

//            from("TreeMap API for Java-JavaFX Developer Guide.pdf")

//            from("dokka") {
//                into("dokka")
//            }
        }
    }
}