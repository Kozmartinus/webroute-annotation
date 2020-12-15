package org.example.app;


import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                String pattern = "<\\w+>";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(route);

                if (m.find( )) {
                    int signIndex = route.indexOf('<');
                    String path = route.substring(0, signIndex - 1);
                    server.createContext(path, new parameterHandler(signIndex, method));
                } else {
                    routesOfType.put(route, method.invoke(Routes.class.getDeclaredConstructor().newInstance()));
                    server.createContext(route, new MyHandler());
                }

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

    static class parameterHandler implements HttpHandler {
        private final Method method;
        private final int signIndex;

        public parameterHandler(int number, Method method) {
            this.signIndex = number;
            this.method = method;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().toString();
            String parameter = path.substring(this.signIndex);
            String response = null;
            try {
                response = (String) this.method.invoke(Routes.class.getDeclaredConstructor().newInstance(), parameter);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}