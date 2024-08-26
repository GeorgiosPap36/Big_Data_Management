import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KVClient
{
    static List<String> serverInfo;
    static List<String> queries;
    static List<Socket> socks;

    static int k;

    public static void main(String[] args) throws Exception
    {
        FileInteract serverTxt = new FileInteract(args[0]);
        FileInteract dataTxt = new FileInteract(args[1]);

        k = Integer.parseInt(args[2]);
        
        serverInfo = serverTxt.ReadFile();
        queries = dataTxt.ReadFile();

        socks = new ArrayList<>();

        for (int i = 0; i < serverInfo.size(); i++){
            String ip = serverInfo.get(i).split(" ")[0];
            int port = Integer.parseInt(serverInfo.get(i).split(" ")[1]);
            socks.add(new Socket(ip, port));
            System.out.println("Connected to server: " + ip + ", " + port);

            ClientReceiveMessage(socks.get(i));
        }

        ClientSendMessage();
    }

    static void ClientReceiveMessage(Socket sock) throws Exception
    {
        Thread receiveThread = new Thread(new Runnable()
        {
            public void run()
            {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                } catch (IOException ioE) {
                    System.out.println(ioE);
                }
                String serverMessage = "";

                while (serverMessage != null) {
                    try {
                        serverMessage = br.readLine();
                    } catch (IOException ioE) {
                        System.out.println(ioE);
                        System.out.println("Connection lost with: " + sock.getRemoteSocketAddress().toString());
                        socks.remove(sock);
                        return;
                    }
                    System.out.println("Server (" + sock.getPort() + ")" + " sent: " + serverMessage);
                }
            }
        });
        receiveThread.start();
    }

    static void ClientSendMessage(){
        Thread sendThread = new Thread(new Runnable() {

            public void run()
            {
                DataOutputStream dos = null;
                BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

                String myMessage = "";
                int counter; //Counts if the message has been send to k servers

                //Send the PUT queries
                for (String query : queries){
                    try{
                        myMessage = "PUT " + query;
                        counter = 0;
                        for (Socket sock : socks){
                            dos = new DataOutputStream(sock.getOutputStream());
                            dos.writeBytes(myMessage + "\n");
                            counter++;
                            if (counter == k){
                                break;
                            }
                        }
                        Collections.shuffle(socks);
                    }
                    catch(Exception e){
                        System.out.println("ERROR");
                        return;
                    }
                }

                while (myMessage != null) {
                    try {
                        myMessage = kb.readLine();

                        int downServers = serverInfo.size() - socks.size();

                        if (downServers >= k){
                            if (myMessage.contains("GET ") || myMessage.contains("QUERY ") || myMessage.contains("COMPUTE ")){
                                System.out.println("More than " + k + " servers are down. The output might not be correct.");
                                for (Socket sock : socks){
                                    dos = new DataOutputStream(sock.getOutputStream());
                                    dos.writeBytes(myMessage + "\n");
                                }
                            }
                            else if (myMessage.contains("DELETE ")){
                                System.out.println("Some servers are down. Delete cannot happen.");
                            }
                        }
                        else if (downServers > 0){
                            if (myMessage.contains("DELETE ")){
                                System.out.println("Some servers are down. Delete cannot happen.");
                            }
                        }
                        else{
                            for (Socket sock : socks){
                                //System.out.println(sock.toString());
                                dos = new DataOutputStream(sock.getOutputStream());
                                dos.writeBytes(myMessage + "\n");
                            }    
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }
            }
        });
        sendThread.start();
    }

}