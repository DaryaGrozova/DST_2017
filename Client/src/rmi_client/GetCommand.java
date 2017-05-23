package rmi_client;

import server_rmi.ICompute;

public class GetCommand {
    private final PrepareDataToServer prepareDataToServer;
    
    public GetCommand(ICompute remoteICompute) {
        prepareDataToServer = new PrepareDataToServer(remoteICompute);
    }

    private final Parser parser = new Parser();

    public void getCommand(String inLine) {

        try {
            String[] parameters = parser.getParameters(inLine);

            switch (parameters[0]) {

                case "ping":
                    prepareDataToServer.ping();
                    break;

                case "echo":
                    prepareDataToServer.echo(parameters);
                    break;

                case "sort":
                    prepareDataToServer.sort(parameters);
                    break;

                case "exit":
                    prepareDataToServer.exit();
                    break;

                default:
                    System.out.println("Ops, I don`t know this command!");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
