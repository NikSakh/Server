package Nikita.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<ServerClient> clients = new ArrayList<ServerClient>();
    private int port;
    private DatagramSocket socket;
    private Thread serverRun, manage, receive;
    private boolean running = false;

    public Server(int port){
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch(SocketException e){
            e.printStackTrace();
        }
        serverRun = new Thread(new Runnable() {
            public void run(){
                running = true;
                System.out.println("Server started on port " + port);
                manage();
                receive();
            }
        }, "serverRun");
    }

    private void manage(){
        manage = new Thread(new Runnable(){
            public void run(){
                while(running){

                }
            }
        });
        manage.start();
    }

    private void receive(){
        receive = new Thread(new Runnable(){
            public void run(){
                while(running){
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                    clients.add(new ServerClient("Nikita", packet.getAddress(), packet.getPort(), 3434));
                    System.out.println(clients.get(0).address.toString() + ":" + clients.get(0).port);
                }
            }
        });
        receive.start();
    }

    private void process(DatagramPacket packet){
        String str = new String(packet.getData());
        if(str.startsWith("/c/")) {
            int id = UniqueID.getID();
            clients.add(new ServerClient(str.substring(3, str.length()), packet.getAddress(), packet.getPort(), id));
            System.out.println(id);
            System.out.println(str.substring(3, str.length()));
        } else {
            System.out.println(str);
        }
    }
}
