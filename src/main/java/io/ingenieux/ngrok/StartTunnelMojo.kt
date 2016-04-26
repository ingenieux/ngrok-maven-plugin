package io.ingenieux.ngrok

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.util.*

class TunnelDefinition(var name: String = "", var proto: String = "http", var addr: String)

@Mojo(name = "start-tunnel")
class StartTunnelMojo : AbstractNGrokMojo() {
    @Parameter(property = "ngrok.tunnels")
    var tunnels: String = ""

    fun getTunnelDefinitions() = tunnels.split(",")
            .map { e -> e.split(Regex("[\\=\\:]")) }
            .map { e ->
                val (name, proto, port) = e

                TunnelDefinition(name, proto, port)
            }
            .toList()

    override fun execute() {
        getTunnelDefinitions().forEach { tunnelSpec ->
            try {
                val result = NGrokClient.post("tunnels", tunnelSpec)

                log.info("Submitted: " + objectMapper.writeValueAsString(result))

            } catch (e: Exception) {
                log.warn("Error: ", e);
            }
        }

        exportTunnelDefinitions()
    }

}

