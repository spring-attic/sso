package demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SsoApplication.class)
@WebAppConfiguration
@IntegrationTest({"debug", "server.port:0"})
public class ApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Value("${security.oauth2.client.userAuthorizationUri}")
	private String authorizeUri;

	private RestTemplate template = new TestRestTemplate();

	@Test
	public void homePageLoads() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void userEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/dashboard/user", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		String location = response.getHeaders().getFirst("Location");
		assertTrue("Wrong location: " + location,
				location.startsWith("http://localhost:" + port + "/dashboard/login"));
	}

	@Test
	public void envEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/env", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals("Basic realm=\"Spring\"",
				response.getHeaders().getFirst("WWW-Authenticate"));
	}

	@Test
	public void loginRedirects() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/dashboard/login", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		String location = response.getHeaders().getFirst("Location");
		assertTrue("Wrong location: " + location + " not " + authorizeUri, location.startsWith(authorizeUri));
	}

}
