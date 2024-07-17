pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.minecraftforge.net") }
        maven { url = uri("https://repo.spongepowered.org/maven") }
        maven {
            name = "Parchment"
            url = uri("https://maven.parchmentmc.org")
        }
    }
}