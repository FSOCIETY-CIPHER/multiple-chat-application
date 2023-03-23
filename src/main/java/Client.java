import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private String username;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Client(Socket socket, String username) throws IOException {
        this.socket=socket;
        this.username=username;
        this.bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter= new PrintWriter(socket.getOutputStream(), true);
    }

    public void ClientSendMessage(){
        printWriter.println(username);
        Scanner scanner = new Scanner(System.in);

        while(socket.isConnected()){
            String s = scanner.nextLine();
            printWriter.println(s);

        }
    }

    public void ClientMessageReciever(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                    while(socket.isConnected()) {
                        try {
                            message = bufferedReader.readLine();
                            if (message == null) {
                                System.err.println("SERVER IS DOWN!");
                                break;
                            }
                            System.out.println(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username:");
        String user = scanner.nextLine();
        Socket socket = new Socket("localhost", 2144);
        Client client = new Client(socket,user);

        client.ClientMessageReciever();
        client.ClientSendMessage();

    }
}
