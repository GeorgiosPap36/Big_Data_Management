import java.net.*;
import java.io.*;

public class KVServer
{
    public static void main(String[] args) throws Exception
    {
        int port = 4999;

        try{
            port = Integer.parseInt(args[1]);
        }
        catch (Exception e){
            System.out.println("Second arguement should be integer");
            return;
        }
        String address = args[0];
        ServerClientCom(address, port);

    }

    static void ServerClientCom(String address, int port){
        
        ServerSocket serverSock = null;
        Socket sock = null;
        try{
            serverSock = new ServerSocket(port, 1, InetAddress.getByName(address));
            System.out.println("Server created");
            sock = serverSock.accept();
            System.out.println("Connection established with client:" + sock.getPort());
        }
        catch(Exception e){
            System.out.println(e);
            return;
        }

        BufferedReader clientBuffer = null;
        try {
            clientBuffer = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF8"));
        } catch (IOException ioE) {
            System.out.println(ioE);
        }

        PrintStream ps = null;
        try{
            ps = new PrintStream(sock.getOutputStream());
        }
        catch (IOException ioE){
            System.out.println(ioE);
        }

        String clientMessage = "";

        QueryCalculations qC = new QueryCalculations();

        while (clientMessage != "over") {

            try {
                clientMessage = clientBuffer.readLine();
                System.out.println("Client sent: " + clientMessage);

                clientMessage = clientMessage.toString();
                String message;
                if (clientMessage.contains("PUT ")){
                    message = clientMessage.split(" ", 2)[1];
                    ps.println(qC.Put(message));
                }
                else if (clientMessage.contains("GET ")){
                    message = clientMessage.split(" ", 2)[1];
                    ps.println(qC.Get(message));
                }
                else if (clientMessage.contains("DELETE ")){
                    message = clientMessage.split(" ", 2)[1];
                    ps.println(qC.Delete(message));
                }
                else if (clientMessage.contains("COMPUTE ")){
                    message = clientMessage.split(" ", 2)[1];
                    String mathExpression = message.split(" WHERE ")[0];
                    String queries = message.split(" WHERE ")[1];
                    ps.println(qC.Compute(mathExpression, queries));
                }
                else if (clientMessage.contains("QUERY ")){
                    message = clientMessage.split(" ", 2)[1];
                    ps.println(qC.Query(message));
                }
                else{
                    ps.println("Wrong command");
                }
                
            } catch (IOException ioE) {
                System.out.println(ioE);
                return;
            }
        }

        try{
            serverSock.close();
        }
        catch(Exception e){
            System.out.println(e);
            return;
        }
    }
}