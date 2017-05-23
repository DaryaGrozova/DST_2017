package server_rmi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Instant;

public interface ICompute extends Remote {

    public static final int PORT = 16161;
    public static final String SERVER_NAME = "rmi.server.ua";

    public <T> T executeTask(ITask<T> t) throws IOException, RemoteException;

    public String echo(String text) throws RemoteException;

    public String ping() throws RemoteException;



    public static class FileInfo implements Serializable {

        private static final long serialVersionUID = 229L;
        private byte[] fileContent;
        private String filename;

        public String getFilename() {
            return filename;
        }

        public FileInfo(File file) throws IOException {
            fileContent = Files.readAllBytes(file.toPath());
            filename = file.getName();
        }

        public byte[] getFileContent() {
            return fileContent;
        }
    }



    public class SortedAlgorithm implements ITask<FileInfo>, Serializable {

        private static final long serialVersionUID = 227L;

        private final ICompute.FileInfo fileInfo;
        private String[] inStringMass;
        private float[] inFloatMass;
        private File file;

        public SortedAlgorithm(FileInfo fileInfo) throws IOException {
            this.fileInfo = fileInfo;
        }

        @Override
        public ICompute.FileInfo execute() throws IOException {

            String clientsContent = new String(fileInfo.getFileContent(), StandardCharsets.UTF_8).trim();
            inStringMass = (clientsContent.equals("")) ? new String[]{} : clientsContent.split(" ");

            if(inStringMass.length == 0){
                inFloatMass = new float[]{};
            } else {
                inFloatMass = new float[inStringMass.length];
                for (int i = 0; i < inStringMass.length; i++) {
                    inFloatMass[i] = Float.parseFloat(inStringMass[i]);
                }
            }

            int start = (int) (Instant.now().getEpochSecond());
            sort(inFloatMass);
            int finish = (int) (Instant.now().getEpochSecond() - start);

            System.out.println(" sort continued " + finish + " seconds");
            file = new File("sortedResponse_" + fileInfo.getFilename());

            if (inFloatMass.length != 0) {
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                    for (float element : inFloatMass) {
                        dos.writeBytes(element + ", ");
                    }
                } catch (Exception ex) {
                    System.out.println("Problem with writing file");
                }
            }
            System.out.println("Sending response to client");
            return new ICompute.FileInfo(file);
        }

        private void sort(float[] clientsData) {
            int elementsInMassive = clientsData.length;
            // формуємо нижній ряд піраміди
            for (int i = (elementsInMassive / 2) - 1; i >= 0; i--) {
                siftDown(clientsData, i, elementsInMassive);
            }
            // Просіваємо через піраміду інші елементи
            for (int i = elementsInMassive - 1; i >= 1; i--) {
                float temp = clientsData[0];
                clientsData[0] = clientsData[i];
                clientsData[i] = temp;
                siftDown(clientsData, 0, i - 1);
            }
        }

        private void siftDown(float[] clientsData, int element1, int element2) {
            int maxChild; // індекс максимального наслідника
            boolean done = false; // прапор того, що 'куча' сформована
            // Поки не дійшли до останнього ряду
            while ((element1 * 2 <= element2) && (!done)) {

                if((element1 * 2 + 1) >= clientsData.length)
                    break;

                if (element1 * 2 == element2) // якщо ми в останньому ряді
                {
                    maxChild = element1 * 2;    // запам'ятовуємо лівого наслідника
                } // інакше запам'ятовуємо більший наслідник з двох
                else if (clientsData[element1 * 2] > clientsData[element1 * 2 + 1]) {
                    maxChild = element1 * 2;
                } else{
                    maxChild = element1 * 2 + 1;
                }
                // якщо елемент вершини менший максимального наслідника
                if (clientsData[element1] < clientsData[maxChild]) {
                    float temp = clientsData[element1]; // міняємо їх місцями
                    clientsData[element1] = clientsData[maxChild];
                    clientsData[maxChild] = temp;
                    element1 = maxChild;
                } else // інакше
                    done = true; // піраміда сформована
            }
        }
    }
}
