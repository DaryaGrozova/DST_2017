package server_rmi;

import java.io.IOException;

public class Main{

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();

        System.out.println("Hello! Welcome to my server!");
        System.out.println("Server started, press enter to close server");

        try {
            System.in.read();
            server.closeServer();
        }catch (IOException ioex){
            ioex.printStackTrace();
        }

        System.out.println(" Server closed");
    }
}
