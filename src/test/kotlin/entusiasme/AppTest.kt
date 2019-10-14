package entusiasme

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import org.slf4j.LoggerFactory
class AppTest {
    private val logger = LoggerFactory.getLogger(App::class.java.name)

    @Test fun testMainPathRequest() {
        val context: LoggerContext = LoggerContext()
        val spinderStreamLogger: Logger = context.getLogger(App::class.java.name)
        val listAppender = ListAppender<ILoggingEvent>()
        listAppender.start()
        spinderStreamLogger.addAppender(listAppender)

        withTestApplication({ module() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
