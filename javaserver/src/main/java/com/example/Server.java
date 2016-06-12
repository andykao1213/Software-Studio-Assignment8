package com.example;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


/**
 * Created by Gary on 16/5/28.
 */
public class Server extends JFrame implements Runnable{
    private Thread thread;
    private ServerSocket servSock;
    private JTextArea textArea;

    public Server(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setPreferredSize(new Dimension(400, 400));
        JScrollPane scrollPane = new JScrollPane(this.textArea);
        scrollPane.setPreferredSize(new Dimension(400,400));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane);
        this.pack();
        this.setVisible(true);

        try {
            // Detect server ip
            InetAddress IP = InetAddress.getLocalHost();
            System.out.println("IP of my system is := "+IP.getHostAddress());
            System.out.println("Waitting to connect......");

            // Create server socket
            servSock = new ServerSocket(2000);

            // Create socket thread
            thread = new Thread(this);
            thread.start();

           // this.reader = new BufferedReader(new InputStreamWriter(servSock.getInputStream()));
        } catch (java.io.IOException e) {
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
        } finally{

        }
    }

    @Override
    public void run(){
        BufferedReader reader;
        // Running for waitting multiple client
        while(true){
            try{
                // After client connected, create client socket connect with client
                Socket clntSock = servSock.accept();
                InputStream in = clntSock.getInputStream();

                System.out.println("Connected!!");

                // Transfer data
                byte[] b = new byte[1024];
                int length;

                length = in.read(b);
                String s = new String(b);
                System.out.println("[Server Said]" + s);

               ConnectionThread connection = new ConnectionThread(clntSock);
                connection.start();
            }
            catch(Exception e){
                //System.out.println("Error: "+e.getMessage());
            }
        }
    }

    class ConnectionThread extends Thread
    {
        private Socket socket;
        private InputStream input;
        private OutputStream output;

        ConnectionThread(Socket sck){
            socket = sck;
            try{
                input = socket.getInputStream();
                output = socket.getOutputStream();
            }catch (Exception e){}
        }

        public void run(){
            while(true){
                String mess;
                byte[] b = new byte[1024];
                int length;
                String s = new String(b);
                try{
                    length = input.read(b);
                    mess = new String(b);
                }catch (Exception e){break;}

                if(mess != null){
                    /*if(mess.equals("null"))*/ textArea.append("The result from app is "+mess+"\n");
                }

            }
        }
    }

}

