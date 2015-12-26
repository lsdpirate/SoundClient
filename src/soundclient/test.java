/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author lsdpirate
 */
public class test {
    
    
    public static void main(String [] args) throws IOException, InterruptedException{
        
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 4444));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        
        File audioFile = new File("D:\\Musica\\Music 2015 update\\Blackmill - Gaia (Full Version).mp3");
        FileInputStream fis = new FileInputStream(audioFile);
        
        String head = "SN";
        long fileLength = audioFile.length();
        String header = head + fileLength;
        System.out.println(header);
        
        pw.write(header);
        Thread.sleep(500);
        pw.flush();
        byte[] buffer = new byte[(int) audioFile.length()];
        int count;
        while ((count = fis.read(buffer)) != -1) {
            socket.getOutputStream().write(buffer, 0, count);
        }
    
        pw.close();
        socket.close();
    }
}
