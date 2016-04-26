package io.ingenieux.ngrok

import org.apache.maven.plugins.annotations.Mojo

@Mojo(name = "list-tunnels",
        requiresOnline = true
)
class ListTunnelsMojo : AbstractNGrokMojo() {
    override fun execute() {
        getTunnelList().forEach { tunnel ->
            log.info("Tunnel: " + tunnel)
        }
    }
}