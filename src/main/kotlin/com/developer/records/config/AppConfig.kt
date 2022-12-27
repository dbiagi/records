package com.developer.records.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class AppConfig {
    companion object {
        const val HISTORY_FILE_FOLDER = "history-files"
    }

    val logger = KotlinLogging.logger { }

    fun getStoragePath(): String = "./$HISTORY_FILE_FOLDER"

    @Bean
    fun initFolder() {
        val folderPath = File(getStoragePath())

        if (!folderPath.exists() && !folderPath.mkdir()) {
            logger.error("error creating history file folder at path {}", folderPath.absoluteFile)
            throw RuntimeException("error creating history file folder")
        }
    }
}
