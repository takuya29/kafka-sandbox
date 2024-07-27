package org.example.common

import java.io.File
import java.io.IOException
import java.util.Properties

/**
 * Ported from https://confluent.cloud/
 */
class PropertyUtils {
    companion object {
        fun readConfig(configFile: String): Properties {
            val file = File(PropertyUtils::class.java.classLoader.getResource(configFile)!!.toURI())

            // reads the client configuration from client.properties
            // and returns it as a Properties object
            if (!file.exists()) {
                throw IOException("$configFile not found.")
            }

            val config = Properties()
            file.inputStream().use { inputStream ->
                config.load(inputStream)
            }
            return config
        }
    }
}
