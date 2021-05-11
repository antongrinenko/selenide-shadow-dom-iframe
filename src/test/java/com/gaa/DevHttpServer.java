package com.gaa;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static java.lang.Thread.currentThread;

public class DevHttpServer {
    private HttpServer server;

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/", new DevHttpHandler());
        server.start();
    }

    private class DevHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String filename = httpExchange.getRequestURI().getPath().replaceFirst("/(.*)", "$1");
            byte[] fileContent = readFileContent(filename);

            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.sendResponseHeaders(200, fileContent.length);
            outputStream.write(fileContent);
            outputStream.flush();
            outputStream.close();
        }
    }

    private byte[] readFileContent(String fileName) throws IOException {
        InputStream in = currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (in == null) return null;
        try {
            return IOUtils.toByteArray(in);
        }
        finally {
            in.close();
        }
    }
}
