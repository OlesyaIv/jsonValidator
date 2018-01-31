/*
JSON Validator
@author Olesya Ivashekvich
*/
package main.java;
import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
//import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.BufferedReader;

/*
Class JsonValidator
*/

public class JsonValidator {
	/**
	 * Create server and check json file
	 * 
         * @param server is newly created server in 'main' function, we use this param to bind server and create context
         * @throws IOException this exception can happen when something wrong with input/output operations
         */
	int cnt = 0;
    public JsonValidator(HttpServer server) throws IOException {
	    
	    Gson builder = new GsonBuilder().setPrettyPrinting().create();
	    server.bind(new InetSocketAddress(80), 0);
	    
	    server.createContext("/", http -> {
		    /* server start */
		    
		    /*
		    * Read text from stream
		    */
		    InputStreamReader isr = new InputStreamReader(http.getRequestBody());
		    final String jsonRequest = new BufferedReader(isr).lines().collect(Collectors.joining());
		    System.out.println("request:" + jsonRequest);
		    String filename = http.getRequestURI().getPath();
		    /*
		    * String for result
		    */
		    cnt = cnt + 1; 
		    String jsonResponse;
		    String respStr = null;
		    
		    /*
		    * Now try convert to string and see errors
		    */
		    try {
		
			    Object object = builder.fromJson(jsonRequest, Object.class);
			    respStr = builder.toJson(object);   // jsonResponse
		    } 

		    catch (JsonSyntaxException ex) {
			    
		    /*
		    * Now create erorr-message
		    */
			    JsonObject jsonError = new JsonObject();
			    jsonError.addProperty("message", ex.getMessage());
			    jsonResponse = builder.toJson(jsonError);
			    
		    /*
		    * Now create response
		    */
			    String exception = ex.toString();
			    String[] er = exception.split(" at ");
			    String errorMessage = er[0]; 
			    String errorPlace = er[1]; 
			  //  String id = Integer.toString(ex.hashCode());
			 
			    respStr = "{\n" +
				    " \"errorCode\"  : 12345,\n" +
				    " \"errorMessage\" : \"" + errorMessage + "\",\n" +
				    " \"errorPlace\" : \"" + errorPlace + "\",\n" +
				    " \"resource\"   : \"" + filename + "\",\n" +
				    " \"request-id\" : \"" + cnt + "\",\n" +
				    "}";
		    }
		    
		    /*
		    * And now we can see our result
		    */
		    System.out.println("response:" + respStr);
		    http.sendResponseHeaders(200, respStr.length());
		    http.getResponseBody().write(respStr.getBytes());
		    
		    /*
		    * close server
		    */
		    http.close();
		    
	    });
    }
	
	/**
	 *Starting server and waiting for a Json files
	 *
         * @throws IOException this exception can happen when something wrong
         */
    public static void main(String[] args) throws IOException {
                final HttpServer server = HttpServer.create();
                JsonValidator json = new JsonValidator(server);
                json.start(server);
    }
	

	/*Function for start and stop server*/
	/**
	 * Method binds server to HTTP port and starts listening
	 *
         * @param server we start server
         */
        private static void start(HttpServer server){server.start();}
	/**
	 * Method stop server and stop listening
	 *
         * @param server we stop server
         */
        private static void stop(HttpServer server){server.stop(0);}
	
	/*End function for start and stop server*/
}
