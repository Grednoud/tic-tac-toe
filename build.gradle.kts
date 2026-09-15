plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "ru.mts.tictactoe"
version = "2.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("ru.mts.tictactoe.TicTacToeApp")
    mainModule.set("ru.mts.tictactoe")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "ru.mts.tictactoe.TicTacToeApp"
        )
    }
}
