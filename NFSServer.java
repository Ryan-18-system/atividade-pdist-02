import java.io.*;
import java.net.*;
import java.util.*;

public class NFSServer {
    private static Map<String, List<String>> fileSystem = new HashMap<>();

    public static void main(String[] args) {
        int port = 24198; // Porta em que o servidor NFS escuta
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor NFS iniciado na porta " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                    String command = (String) in.readObject();
                    String response = processCommand(command);

                    System.out.printf(command);
                    System.out.printf(response);
                    out.writeObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processCommand(String command) {
        String[] tokens = command.split(" ");
        String operation = tokens[0];

        if (operation.equals("readdir")) {
            String directory = tokens[1];
            List<String> files = fileSystem.getOrDefault(directory, new ArrayList<>());
            return String.join(",", files);
        } else if (operation.equals("create")) {
            String fileName = tokens[1];
            StringBuilder retorno = new StringBuilder();
            retorno.append(fileName);
            retorno.append(" Arquivo criado com sucesso");
            return retorno.toString();
        } else if (operation.equals("rename")) {
            String oldName = tokens[1];
            String newName = tokens[2];
            String directory = tokens[3];
            List<String> files = fileSystem.get(directory);
            if (files != null && files.contains(oldName)) {
                files.remove(oldName);
                files.add(newName);
                return "Arquivo renomeado com sucesso.";
            }
            return "Arquivo não encontrado.";
        } else if (operation.equals("remove")) {
            String fileName = tokens[1];
            String directory = tokens[2];
            List<String> files = fileSystem.get(directory);
            if (files != null && files.contains(fileName)) {
                files.remove(fileName);
                return "Arquivo removido com sucesso.";
            }
            return "Arquivo não encontrado.";
        } else {
            return "Comando inválido.";
        }
    }
}
