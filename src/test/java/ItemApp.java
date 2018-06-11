import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;

public class ItemApp {

  @Rule
  public PactProviderRuleMk2 mockProvider
      = new PactProviderRuleMk2("post_test", "localhost", 8080, this);

  @Pact(provider="post_test", consumer = "test_consumer")
  public RequestResponsePact createPact(PactDslWithProvider builder) {
    Map<String, String> headers = Collections.singletonMap("Content-Type", "application/json");

    return builder
        .given("testing a POST")
        .uponReceiving("POST REQUEST")
        .path("/pact")
        .method("POST")
        .willRespondWith()
        .status(200)
        .headers(headers)
        .body("{\"name\": \"Micaela\"}").toPact();
  }

  @Test
  @PactVerification("post_test")
  public void post_method_test() throws IOException {
    URL myURL = new URL("http://localhost:8080/pact");
    HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.connect();
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder results = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      results.append(line);
    }

    connection.disconnect();
    System.out.println("Results from test -----> " + results.toString());
  }
}
