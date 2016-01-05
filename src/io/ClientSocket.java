package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsdpirate
 */
public class ClientSocket implements Runnable {

    private Socket localSocket;
    private InetSocketAddress serverInfos;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private ArrayDeque<byte[]> toSendData;

    public ClientSocket() {
    }

    public ClientSocket(String serverIp, int serverPort) throws IOException {
        connect(serverIp, serverPort);
        toSendData = new ArrayDeque();
    }

    public void connect(String serverIp, int serverPort) throws IOException {
        serverInfos = new InetSocketAddress(serverIp, serverPort);
        localSocket = new Socket();
        localSocket.connect(serverInfos);
        outputStream = localSocket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);

    }

    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public void sendData(byte[] data) throws IOException {
        localSocket.getOutputStream().write(data);
        localSocket.getOutputStream().flush();
    }

    public void sendData(String data) {
        printWriter.write(data);
        printWriter.flush();
    }

    public void close() throws IOException {
        if (printWriter != null) {
            printWriter.close();
        }
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
        if (!localSocket.isClosed()) {
            localSocket.close();
        }
    }

    public void appendInOutputBuffer(byte[] b) {
        synchronized (toSendData) {
            toSendData.add(b);
        }

    }

    @Override
    public void run() {
        while(!localSocket.isClosed()){
            synchronized (toSendData){
                if(Thread.holdsLock(toSendData)){
                    try {
                        sendData(toSendData.getFirst());
                    } catch (IOException ex) {
                        Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

}
