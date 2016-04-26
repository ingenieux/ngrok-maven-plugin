# ngrok-maven-plugin

Work in progress. Requires [ngrok beta](https://dl.equinox.io/ngrok/ngrok/beta)

# Usage

First, start ngrok with no tunnels, as such:

```shell
$ ngrok start --none
```

NOTE: It requires ```authtoken``` accordingly set.

## Starting Tunnels

Via the plugin, you can create tunnels on demand:

```shell
$ mvn ngrok:start-tunnel -Dngrok.tunnels=[servicename]:[proto]:[port],[servicename]:[proto]:[port]
```

e.g.:

```shell
$ mvn ngrok:start-tunnel -Dngrok.tunnels=restapi:http:8080
```

## Exporting Tunnel Info

Once you have tunnels running, the plugin exports the contents of your tunnels into build variables:

```shell
$ mvn io.ingenieux:ngrok-maven-plugin:0.0.1-SNAPSHOT:export-tunnels
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building ngrok maven plugin 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- ngrok-maven-plugin:0.0.1-SNAPSHOT:export-tunnels (default-cli) @ ngrok-maven-plugin ---
[INFO] Declaring tunnel NGrokTunnelDefinition(name=command_line (http), publicUrl=http://4c29c4ac.ngrok.io, proto=http, config=NGrokTunnelConfig(addr=localhost:8080)) as ngrok.http.8080=4c29c4ac.ngrok.io
[INFO] Declaring tunnel NGrokTunnelDefinition(name=command_line, publicUrl=https://4c29c4ac.ngrok.io, proto=https, config=NGrokTunnelConfig(addr=localhost:8080)) as ngrok.https.8080=4c29c4ac.ngrok.io
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 2.555 s
[INFO] Finished at: 2016-04-25T22:15:34-05:00
[INFO] Final Memory: 15M/210M
[INFO] ------------------------------------------------------------------------
```

Notice the variables:

  * ```ngrok.https.8080=4c29c4ac.ngrok.io```
  * ```ngrok.http.8080=4c29c4ac.ngrok.io```
  
Using those, you can run integration tests behind firewalls using ngrok.

