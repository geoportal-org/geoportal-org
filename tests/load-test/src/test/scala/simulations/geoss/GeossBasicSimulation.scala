package simulations.geoss

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

/**
 * Basic performance test for geoportal (GEOSS), which simulates standard user behaviour.
 * Results of this test may be included in Monthly report.
 */
class GeossBasicSimulation extends Simulation {
  val baseUrl = if (System.getProperty("uiBaseUrl") != null) System.getProperty("uiBaseUrl") else "http://localhost:3000"
  val idpBaseUrl = if (System.getProperty("idpBaseUrl") != null) System.getProperty("idpBaseUrl") else "http://localhost:8080"
  val httpProtocol = http
    .baseUrl(baseUrl) // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("GATLING LOAD TEST")
    .disableFollowRedirect

  val repeat = Integer.parseInt(if(System.getProperty("repeat") != null) System.getProperty("repeat") else "5")
  val users = Integer.parseInt(if (System.getProperty("users") != null) System.getProperty("users") else "50")

  val scn = scenario("Homepage Simulation")
    .repeat(repeat) {
      group("Homepage") {
        exec(
          http("Homepage")
            .get("")
        )
      }
        .pause(1 seconds)
        .exec(
          http("Login page")
            .get(idpBaseUrl + "/realms/geoss/protocol/openid-connect/auth?protocol=oauth2&response_type=code&client_id=geoss-ui&redirect_uri=" + java.net.URLEncoder.encode(baseUrl + "/login", "UTF-8") + "&scope=" + java.net.URLEncoder.encode("openid profile email roles", "UTF-8") + "&state=" + java.util.UUID.randomUUID.toString)
        )
        .pause(1 seconds)
        .exec(
          http("About page")
            .get("/about")
        )
    }

  setUp(scn.inject(atOnceUsers(users)).protocols(httpProtocol))

}
