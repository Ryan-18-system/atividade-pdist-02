import java.io.*;
import java.net.*;
import java.util.Scanner;

public class NFSClient {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Endereço do servidor NFS
        int serverPort = 24198; // Porta em que o servidor NFS está escutando

        try (Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Escolha uma operação:");
                System.out.println("1. Listar arquivos em um diretório (readdir)");
                System.out.println("2. Criar arquivo (create)");
                System.out.println("3. Renomear arquivo (rename)");
                System.out.println("4. Remover arquivo (remove)");
                System.out.println("5. Sair");

                int escolha = scanner.nextInt();
                scanner.nextLine();

                switch (escolha) {
                    case 1:
                        System.out.println("Informe o diretório para listar os arquivos:");
                        String readdirDirectory = scanner.nextLine();
                        String readdirCommand = "readdir " + readdirDirectory;
                        out.writeObject(readdirCommand);

                        // Recebe a resposta do servidor (lista de arquivos separados por vírgula)
                        String readdirResponse = (String) in.readObject();
                        String[] files = readdirResponse.split(",");

                        System.out.println("Arquivos no diretório:");
                        for (String file : files) {
                            System.out.println(file);
                        }
                        break;

                    case 2:
                        System.out.println("Informe o nome do arquivo a ser criado:");
                        String createFileName = scanner.nextLine();
                        System.out.println("Informe o diretório onde o arquivo será criado:");
                        String createDirectory = scanner.nextLine();
                        String createCommand = "create " + createFileName + " " + createDirectory;
                        out.writeObject(createCommand);

                        // Recebe a resposta do servidor
                        String createResponse = (String) in.readObject();
                        System.out.println("Resposta do servidor: " + createResponse);
                        break;

                    case 3:
                        System.out.println("Informe o nome do arquivo a ser renomeado:");
                        String oldFileName = scanner.nextLine();
                        System.out.println("Informe o novo nome do arquivo:");
                        String newFileName = scanner.nextLine();
                        System.out.println("Informe o diretório onde o arquivo está localizado:");
                        String renameDirectory = scanner.nextLine();
                        String renameCommand = "rename " + oldFileName + " " + newFileName + " " + renameDirectory;
                        out.writeObject(renameCommand);

                        // Recebe a resposta do servidor
                        String renameResponse = (String) in.readObject();
                        System.out.println("Resposta do servidor: " + renameResponse);
                        break;

                    case 4:
                        System.out.println("Informe o nome do arquivo a ser removido:");
                        String removeFileName = scanner.nextLine();
                        System.out.println("Informe o diretório onde o arquivo está localizado:");
                        String removeDirectory = scanner.nextLine();
                        String removeCommand = "remove " + removeFileName + " " + removeDirectory;
                        out.writeObject(removeCommand);

                        // Recebe a resposta do servidor
                        String removeResponse = (String) in.readObject();
                        System.out.println("Resposta do servidor: " + removeResponse);
                        break;

                    case 5:
                        System.out.println("Encerrando o cliente.");
                        return;

                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
