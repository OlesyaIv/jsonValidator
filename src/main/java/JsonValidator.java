import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import net.sf.oval.constraint.NotNull;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

public class json_validate {

        public json_validate(HttpServer server) throws IOException {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                server.bind(new InetSocketAddress(80), 0);
                server.createContext("/", httpExchange -> {

                        int id = 0;
 
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
                        String buildString = bufferedReader.readLine();
                        StringBuilder stringBuilder = new StringBuilder();
                        while (buildString != null) {
                                stringBuilder.append(buildString);
                                buildString = bufferedReader.readLine();
                        }
                        String request = stringBuilder.toString();
                        System.out.println("Request: " + request);
                        String response = null;

                        try {

                                Object object = gson.fromJson(request, Object.class);
                                response = gson.toJson(object);

                        } catch (JsonSyntaxException ex) {
 					String exception = ex.toString();
           				 String[] er = exception.split(" at ");
            				String errorMessage = er[0]; 
            				String errorPlace = er[1]; 
            				String id = Integer.toString(ex.hashCode());
                                int errorCode = exception.hashCode();
            
           				 errStr = "{\n" +
            					" \"errorCode\"  : 12345,\n" +
            					" \"errorMessage\" : \"" + errorMessage + "\",\n" +
            					" \"errorPlace\" : \"" + errorPlace + "\",\n" +
            					" \"resource\"   : \"" + file + "\",\n" +
            					" \"request-id\" : \"" + id + "\",\n" +
            					"}";
        					}
        			return errStr;
//*
                                JsonObject error = new JsonObject();
 
                                String errorDescription = exception.getMessage().split(": ")[1];
                                String errorMessage = errorDescription.split(" at ")[0]; 
                                String errorPlace = errorDescription.split(" at ")[1];
   
                                int errorCode = exception.hashCode();
  
                                error.addProperty("errorCode", errorCode);
                                error.addProperty("errorMessage", errorMessage);
                                error.addProperty("errorPlace", errorPlace);
                                error.addProperty("resource", request);
                                error.addProperty("request-id", id);
                                response = gson.toJson(error);
                        }
                        id = id + 1;

                        System.out.println(response);

                        httpExchange.sendResponseHeaders(200, response.length());
                        httpExchange.getResponseBody().write(response.getBytes());
                        httpExchange.close();
*//
                });
        }


        public static void main(String[] args) throws IOException {

                final HttpServer server = HttpServer.create();
                json_validate json = new json_validate(server);
                json.start(server);
        }


        private static void start(HttpServer server){server.start();}

         private static void stop(HttpServer server){server.stop(0);}

}