import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private Socket socket;
    private PrintWriter printWriter;
    private String clientUserName;
    private BufferedReader bufferedReader;
    private static ArrayList<ClientHandler> client= new ArrayList<>();
    public ClientHandler(Socket socket) throws IOException {
        this.socket=socket;
        this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter= new PrintWriter(socket.getOutputStream(), true);
        this.clientUserName = bufferedReader.readLine();
        client.add(this);
        broadcastMessage(clientUserName +" just joined the group");
    }


    @Override
    public void run() {
        String message;
        while(socket.isConnected()){
            try {
                message = bufferedReader.readLine();
                if(message==null || message.toLowerCase().equals("disconnect")){
                    client.remove(this);
                    broadcastMessage(clientUserName +" has left the group");
                    break;
                }
                broadcastMessage(clientUserName+ ": "+message);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    public void broadcastMessage(String message){
        for(ClientHandler clientHandler: client){
            if(!clientHandler.equals(this)){
                clientHandler.printWriter.println(message);
            }
        }
    }
}
