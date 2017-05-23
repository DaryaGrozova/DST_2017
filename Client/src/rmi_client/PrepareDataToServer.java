package rmi_client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

import server_rmi.ICompute;
import server_rmi.ICompute.SortedAlgorithm;
import server_rmi.ICompute.FileInfo;

public class PrepareDataToServer {

    private static final int ELEMENTS = 1_000_000;
    private static final int MAX_VALUE = 1_000_000_000;
    private static final int GOOD_PARAMETERS_LENGTH = 2;

    private final ICompute remoteICompute;

    public PrepareDataToServer(ICompute remoteICompute) {
        this.remoteICompute = remoteICompute;
    }

    public void ping() throws RemoteException {
        System.out.println(remoteICompute.ping());
    }

    public void echo(String[] parameters) throws RemoteException {
        if(checkerParameters(GOOD_PARAMETERS_LENGTH, parameters.length))
            System.out.println(remoteICompute.echo(parameters[1]));
        else
            System.out.println("please enter text");
    }

    public void sort(String[] parameters) throws RemoteException, IOException {
        if(checkerParameters(GOOD_PARAMETERS_LENGTH, parameters.length)) {

            String[] stringMass = new String[ELEMENTS];

            for (int i = 0; i < stringMass.length; i++) {
                stringMass[i] = String.valueOf((float)(Math.random() * MAX_VALUE));
            }

            File file = write(parameters[1], stringMass);
            System.out.println("Send file to server and wait from response");
            FileInfo fileInfo = remoteICompute.executeTask(new SortedAlgorithm(new FileInfo(file)));
            System.out.println("Sort done!");

            write(fileInfo.getFilename(), new String(fileInfo.getFileContent(), StandardCharsets.UTF_8).split(" "));
            System.out.println("File wried successful");
        }
    }

    private boolean checkerParameters(int good, int our){
        return (good == our) ? true : false;
    }

    private File write(String name, String[] inMass) {
        File newFile = new File(name);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(newFile))) {
            for (String element : inMass) {
                dos.writeBytes(element + " ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newFile;
    }

    public void exit() throws RemoteException {
        System.out.println("Exiting from server");
        Start.workerFlag = false;
    }
}
