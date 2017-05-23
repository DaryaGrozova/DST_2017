package rmi_client;

public class Parser {

    public String[] getParameters(String fromClient) {

        String[] parametersFromCommand = fromClient.split(" ");

        if(!parametersFromCommand[0].equals("echo"))
            return parametersFromCommand;
        else
            return fromClient.split(" ",2);
    }
}
