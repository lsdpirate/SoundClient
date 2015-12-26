/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author lsdpirate
 */
public class ClientSocket implements Runnable {

    private Socket localSocket;
    private InetSocketAddress serverInfos;

    private PrintWriter printWriter;

    public ClientSocket() {
    }

    public ClientSocket(String serverIp, int serverPort) throws IOException {
        connect(serverIp, serverPort);
    }

    public void connect(String serverIp, int serverPort) throws IOException {
        serverInfos = new InetSocketAddress(serverIp, serverPort);
        localSocket = new Socket();
        localSocket.connect(serverInfos);
        printWriter = new PrintWriter(localSocket.getOutputStream(), true);
    }

    public void sendData(byte [] data) throws IOException {
        localSocket.getOutputStream().write(data);
        localSocket.getOutputStream().flush();
    }
    
    public void sendData(String data){
        printWriter.write(data);
        printWriter.flush();
    }

    public OutputStream getOutputStream() throws IOException {
        return localSocket.getOutputStream();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
