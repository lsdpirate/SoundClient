/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundclient;

import io.CommunicationProtocol;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author lsdpirate
 */
public class test {

    public static void main(String[] args) throws IOException, InterruptedException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 4444));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        ConcurrentLinkedQueue<byte []> q = new ConcurrentLinkedQueue<>();
//
        /*
        I found out that it is possible to pilot the data sending like this 
        (ln 48 - 54) and to make it work with command sending. Next time i will
        implement the whole thing in MediaNetworkSender
        */
        File audioFile = new File("D:\\Musica\\Music 2015 update\\Blackmill - Gaia (Full Version).mp3");
        FileInputStream fis = new FileInputStream(audioFile);
//
//        String head = "SN";
//        long fileLength = audioFile.length();
//        String header = head + fileLength;
//        System.out.println(header);
//
//        pw.write(header);
//        Thread.sleep(500);
//        pw.flush();
        char [] pl = CommunicationProtocol.PLAY_COMMAND_HEADER.getValue().toCharArray();
        int counter = 0;
        byte[] buffer = new byte[(int) audioFile.length()];
        fis.read(buffer);
        for(byte b : buffer){
            socket.getOutputStream().write(b);
            //q.add(new byte [] {b});
            
            if(counter == 1024 * 100){
                socket.getOutputStream().flush();
               // Thread.sleep(500);
                pw.print(CommunicationProtocol.PLAY_COMMAND_HEADER.getValue());
               // pw.flush();
                 socket.getOutputStream().flush();
                
            }
            ++counter;
        }
        socket.getOutputStream().flush();
        
        
//        int count;
//        while ((count = fis.read(buffer)) != -1) {
//            socket.getOutputStream().write(buffer, 0, count);
//        }
    //    byte b = -128;
      //  socket.getOutputStream().write(b);
        socket.getOutputStream().flush();
        

        pw.close();
        socket.close();
    }
}
