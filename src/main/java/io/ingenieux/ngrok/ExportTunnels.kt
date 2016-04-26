package io.ingenieux.ngrok

import org.apache.maven.plugins.annotations.Mojo

@Mojo(name = "export-tunnels", requiresOnline = true)
class ExportTunnels : AbstractNGrokMojo() {
    override fun execute() {
        exportTunnelDefinitions()
    }
}