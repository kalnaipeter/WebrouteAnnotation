package com.codecool.webroute;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    private static Map<String, Method> routesToMethodsMap = new HashMap<String, Method>();

    public static void main(String[] args) throws Exception {
        connectRoutesToMethods();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    private static void connectRoutesToMethods(){
        for(Method method: Routes.class.getMethods()) {
            if(method.isAnnotationPresent(WebRoute.class)) {
                WebRoute annotation = method.getAnnotation(WebRoute.class);
                String route = annotation.value();
                routesToMethodsMap.put(route, method);
            }
        }
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String route = t.getRequestURI().getPath();
            Method methodForCurrentRoute = routesToMethodsMap.get(route);

            String response = "";
            try {
                response = (String) methodForCurrentRoute.invoke(new Routes());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}