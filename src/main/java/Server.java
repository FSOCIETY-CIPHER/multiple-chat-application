import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket){
        this.serverSocket=serverSocket;
    }

    public void startServer() throws IOException, InterruptedException {
        while(!serverSocket.isClosed()){
            System.out.println("Waiting for client connection...");

            Socket socket = serverSocket.accept();

            System.out.println("Server Connected");
            ClientHandler clientHandler=new ClientHandler(socket);
            Thread t1 = new Thread(clientHandler);
            t1.start();

        }
        serverSocket.close();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(2144);
        Server server=new Server(serverSocket);
        server.startServer();
    }
}
