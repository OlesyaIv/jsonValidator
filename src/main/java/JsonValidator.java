import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

/**
 * Class implements Server interface
 * <p>
 * Task of class is checking valid Json file or not
 */
public class JsonValidator implements Server {

    @NotNull
    private final HttpServer server;
    @NotNull
    private final Gson builder;

    private static final int PORT = 80;
    private static final int CODE_OK = 200;
    private static final String ROOT = "/";

    /**
     * Checking Json file
     *
     * @throws IOException because method create() of HttpServer can throw IOException
     */
    public JsonValidator() throws IOException {
        this.builder = new GsonBuilder().setPrettyPrinting().create();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.server.createContext(ROOT, http -> {
            InputStreamReader isr = new InputStreamReader(http.getRequestBody());
            final String jsonRequest = new BufferedReader(isr).lines().collect(Collectors.joining());
            System.out.println("request:" + jsonRequest);
            String jsonResponse;
            try {
                Object object = builder.fromJson(jsonRequest, Object.class);
                jsonResponse = builder.toJson(object);
            } catch (JsonSyntaxException e) {
                JsonObject jsonError = new JsonObject();
                jsonError.addProperty("message", e.getMessage());
                jsonResponse = builder.toJson(jsonError);
            }
            System.out.println("response:" + jsonResponse);
            http.sendResponseHeaders(CODE_OK, jsonResponse.length());
            http.getResponseBody().write(jsonResponse.getBytes());
            http.close();
        });
    }

    /**
     * Starting server and waiting for a Json files
     *
     * @param args - does not matter
     * @throws IOException - because constructor of JsonValidator can throw IOException
     */
    public static void main(String[] args) throws IOException {
        JsonValidator jsonvalidator = new JsonValidator();
        jsonvalidator.start();
        Runtime.getRuntime().addShutdownHook(new Thread(jsonvalidator::stop));
    }

    /**
     * Implements method of bind server to HTTP port and start listening.
     */
    @Override
    public void start() {
        this.server.start();
    }

    /**
     * Implements method of stop listening and free all the resources.
     */
    @Override
    public void stop() {
        this.server.stop(0);
    }
}