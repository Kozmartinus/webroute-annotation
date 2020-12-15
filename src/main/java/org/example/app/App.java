package org.example.app;


import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.Routes;
import org.example.WebRoute;

public class App {

    private static Map routes = new HashMap<>(){{
        put("GET", new HashMap<>());
        put("POST", new HashMap<>());
    }};

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        for (Method method : Routes.class.getMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                String route = method.getAnnotation(WebRoute.class).path();
                String methodType = method.getAnnotation(WebRoute.class).method();
                Map routesOfType = (HashMap) routes.get(methodType);
                routesOfType.put(route, method.invoke(Routes.class.getDeclaredConstructor().newInstance()));
                server.createContext(route, new MyHandler());
            }
        }
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().toString();
            String method = t.getRequestMethod();
            Map routesOfType = (HashMap) routes.get(method);
            String response = (String) routesOfType.get(path);

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}