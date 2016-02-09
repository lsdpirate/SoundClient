/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author lsdpirate
 */
public class test {

    public static void main(String[] args) throws IOException, InterruptedException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 1234));
        System.out.println("Connected");
        Scanner scanner = new Scanner(System.in);
        System.out.println("waiting...");
        scanner.nextLine();
        
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.println("msg0");
        pw.flush();
        System.out.println("msg0 sent, waiting...");
        scanner.nextLine();
        System.out.println("msg1 sent, waiting...");
        pw.println("msg1");
        pw.flush();
        scanner.nextLine();
        for (int i = 0; i < 100; i++) {
            pw.println("msg" + Integer.toString(i));
            pw.flush();
        }
        pw.flush();
        scanner.nextLine();
        
        
    }
}
