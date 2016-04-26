package io.ingenieux.ngrok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Parameter

@JsonIgnoreProperties(ignoreUnknown = true)
data class NGrokTunnelConfig(
        val addr: String? = null
) {
    val connTuple: Pair<String, Int>
        get() {
            val elts = addr!!.split(":")

            return Pair(elts[0], elts[1].toInt())
        }

    val host: String get() {
        return connTuple.component1()
    }

    val port: Int get() {
        return connTuple.component2()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class NGrokTunnelDefinition(
        var name: String? = null,

        @JsonProperty("public_url")
        var publicUrl: String? = null,

        var proto: String? = null,

        var config: NGrokTunnelConfig? = null
) {
    fun toPropertyDefinition(): Pair<String, String> {
        val propertyName: String = "ngrok.${proto}.${config!!.port}"
        val propertyValue: String = publicUrl!!.replace(Regex("^(tcp|http|https)://"), "")

        return Pair(propertyName, propertyValue)
    }
}

abstract class AbstractNGrokMojo : AbstractMojo() {
    val objectMapper = NGrokClient.objectMapper

    @Parameter(defaultValue = "\${session}", required = true)
    var session: MavenSession? = null

    protected fun exportTunnelDefinitions() {
        val tunnelList: List<NGrokTunnelDefinition> = getTunnelList()

        tunnelList.forEach { tunnelDefinition ->
            val (key, value) = tunnelDefinition.toPropertyDefinition()

            if (session!!.systemProperties.contains(key) &&
                    session!!.systemProperties.get(key)!!.equals(value))
                return;

            log.info("Declaring tunnel ${tunnelDefinition} as " +
                    "${key}=${value}")

            session!!.systemProperties.put(key, value)
        }
    }

    protected fun getTunnelList(): List<NGrokTunnelDefinition> {
        val tunnelListJson = NGrokClient.get("tunnels")!!.get("tunnels")

        val tunnelList: List<NGrokTunnelDefinition> = objectMapper.convertValue(tunnelListJson, object : TypeReference<List<NGrokTunnelDefinition>>() {})

        return tunnelList
    }
}