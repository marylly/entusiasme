package entusiasme

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.LoggerFactory

class App {
    private val logger = LoggerFactory.getLogger(App::class.java.name)
}

val logger = LoggerFactory.getLogger("Testing")

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Hello, world!", ContentType.Text.Html)
        }
    }
}

fun main(args: Array<String>) {
    logger.info("Starting Netty server on port 8080")
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}
