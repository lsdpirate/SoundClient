package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class is a wrapper class for a socket.
 * Many of the methods are delegates of members of the socket class.
 * @author lsdpirate
 */
public class ClientSocket{

    private Socket localSocket;
    private InetSocketAddress serverInfos;
    private OutputStream outputStream;
    private PrintWriter printWriter;

    /**
     * Default constructor for the ClientSocket class.
     * This constructor does nothing so it is much preferrable to use the 
     * parametric contructor. However it is still possible to pass connection 
     * parameters by using the connect() method.
     */
    public ClientSocket() {
        localSocket = new Socket();
    }
    
    /**
     * Creates a ClientSocket wich tries to connect to the specified host. 
     * @param serverIp The ip or hostname of the target host
     * @param serverPort The port to connect to
     * @throws IOException If something during the connection fails
     */
    public ClientSocket(String serverIp, int serverPort) throws IOException {
        connect(serverIp, serverPort);
    }

    /**
     * Connects the socket to the specified host via the specified port.
     * @param serverIp The ip or hostname of the target host
     * @param serverPort The port to connect to
     * @throws IOException If something during the connection fails
     */
    public void connect(String serverIp, int serverPort) throws IOException {
        serverInfos = new InetSocketAddress(serverIp, serverPort);
        localSocket = new Socket();
        localSocket.connect(serverInfos);
        outputStream = localSocket.getOutputStream();
        printWriter = new PrintWriter(outputStream, true);

    }

    /**
     * Delegate method to this Outputstream's write() method.
     * @see OutputStream.write()
     * @param      b     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> is thrown if the output
     *             stream is closed.
     */
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    /**
     * Delegate method for this Outputstream's flush() method.
     * @throws IOException if an I/O error occurs.
     */
    public void flush() throws IOException {
        outputStream.flush();
    }

    /**
     * Sends the data to the connected socket. If there isn't any socket
     * connected an exception will be thrown.
     * @param data The data to be sent
     * @throws IOException If an I/O error occurs or if the socket is not 
     * connected.
     */
    public void sendData(byte[] data) throws IOException {
        localSocket.getOutputStream().write(data);
        localSocket.getOutputStream().flush();
    }

    /**
     * Sends the data to the connected socket. If there isn't any socket
     * connected an exception will be thrown.
     * @param data The data to be sent
     */
    public void sendData(String data) {
        printWriter.write(data);
        printWriter.flush();
    }

    /**
     * Closes the connection and the streams if present.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Returns the connection status for this socket.
     * @return True if there is a connection, False is there is none
     */
    public boolean isClosed(){
        return this.localSocket.isClosed();
    }


}
