package rmi_client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import server_rmi.ICompute;

public class Start {

    private final GetCommand getCommand;
    private final Registry registry;
    private final ICompute remoteICompute;

    public static boolean workerFlag = true;

    public Start() throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        registry = LocateRegistry.getRegistry(ICompute.PORT);
        remoteICompute = (ICompute) registry.lookup(ICompute.SERVER_NAME);
        getCommand = new GetCommand(remoteICompute);
    }

    public void waitCommand() {
        System.out.println("You are connected to rmi.server.ua!");

        try(Scanner scanner = new Scanner(System.in)) {
            while (workerFlag)
                getCommand.getCommand(scanner.nextLine());
        }
    }
}
