import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.BufferedReader;

/*
Create server and check json file
*/

public class JsonValidator {
    public JsonValidator(HttpServer server) throws IOException {
	    
	    Gson builder = new GsonBuilder().setPrettyPrinting().create();
	    server.bind(new InetSocketAddress(80), 0);
	    
	    server.createContext("/", http -> {
		    /* server start */
		    
		    /*Read text from stream*/
		    InputStreamReader isr = new InputStreamReader(http.getRequestBody());
		    final String jsonRequest = new BufferedReader(isr).lines().collect(Collectors.joining());
		    System.out.println("request:" + jsonRequest);
		    
		    /*String for result*/
		    
		    String jsonResponse;
		    String respStr = null;
		    
		    /*Now try convert to string and see errors*/
		    try {
		
			    Object object = builder.fromJson(jsonRequest, Object.class);
			    jsonResponse = builder.toJson(object);   
		    } 

		    catch (JsonSyntaxException ex) {
			    
		    /*Now create erorr-message*/
			    JsonObject jsonError = new JsonObject();
			    jsonError.addProperty("message", ex.getMessage());
			    jsonResponse = builder.toJson(jsonError);
			    
		    /*Now create response*/
			    String exception = ex.toString();
			    String[] er = exception.split(" at ");
			    String errorMessage = er[0]; 
			    String errorPlace = er[1]; 
			    String id = Integer.toString(ex.hashCode());
			    
			    respStr = "{\n" +
				    " \"errorCode\"  : 12345,\n" +
				    " \"errorMessage\" : \"" + errorMessage + "\",\n" +
				    " \"errorPlace\" : \"" + errorPlace + "\",\n" +
				    " \"resource\"   : \"" + jsonRequest + "\",\n" +
				    " \"request-id\" : \"" + id + "\",\n" +
				    "}";
		    }
		    
		    /*And now we can see our result*/
		    System.out.println("response:" + respStr);
		    http.sendResponseHeaders(200, respStr.length());
		    http.getResponseBody().write(respStr.getBytes());
		    
		    /*close server*/
		    http.close();
		    
	    });
    }
	
    /*Starting server and waiting for a Json files*/	
    public static void main(String[] args) throws IOException {
                final HttpServer server = HttpServer.create();
                JsonValidator json = new JsonValidator(server);
                json.start(server);
    }
	

	/*Function for start and stop server*/
	
        private static void start(HttpServer server){server.start();}
        private static void stop(HttpServer server){server.stop(0);}
	
	/*End function for start and stop server*/
}
