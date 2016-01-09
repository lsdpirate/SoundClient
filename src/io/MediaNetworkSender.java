package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import soundclient.media.Media;

/**
 * The MediaNetworkSender class (name TBD) is the class which manages the
 * network I/O of the application. It can send media and commands to the
 * connected player.
 *
 * @author lsdpirate
 */
public class MediaNetworkSender {

    /*
     Will own an instance of ClientSocket and a Thread of it.
     
     */
    private final ClientSocket socket;
    private boolean connected;
    private boolean bufferInUse;

    /**
     * The default constructor does nothing.
     */
    public MediaNetworkSender() {
        socket = new ClientSocket();
    }

    /**
     * Delegate method for ClientSocket.connect() with controls implementation.
     *
     * @param serverIp The hostname or ip to connect to.
     * @param serverPort The port to connect by.
     * @throws IOException If something goes wrong during the connection.
     * @throws IllegalArgumentException If parameters are invalid (eg. a
     * negative port number)
     */
    public void connectToSocket(String serverIp, int serverPort) throws IOException, IllegalArgumentException {
        if (!socket.isClosed()) {
            socket.close();
            connected = false;
        }

        if (serverPort < 0) {
            throw new IllegalArgumentException("Port number is invalid " + serverPort);
        }
        if (serverIp.length() < 1) {
            throw new IllegalArgumentException("The specified ip isn't valid: " + serverIp);
        }
        socket.connect(serverIp, serverPort);
        connected = true;
    }

    /**
     * Sends the media to the connected player if connected. It gets the file
     * path from the Media object and then gets the binary data out of it. After
     * that the raw bytes will be sent via the socket stream to the player. The
     * sending operation is done in an anonymous thread to avoid invading user's
     * space by freezing the program, this thread listens for any stop signal
     * given if the connection is lost or if the application is closing.
     *
     * @param m The media to send.
     * @throws IOException If an I/O error occurs.
     */
    public void sendMediaOverNetwork(Media m) throws IOException {
        if (socket.isClosed()) {
            throw new IOException("No connection is present");
        }
        File audioFile = new File(m.getPath());
        System.out.println(audioFile.getName());

        //Send the header for the communication protocol
        long fileLength = audioFile.length();
        String header = CommunicationProtocol.SONG_HEADER.getValue() + Long.toString(fileLength);

        //Send the file
        FileInputStream fis = new FileInputStream(audioFile);
        byte[] buffer = new byte[(int) audioFile.length()];

        new Thread() {
            @Override
            public void run() {
                synchronized (socket) {
                    try {
                        int count;
                        while ((count = fis.read(buffer)) != -1 && !socket.isClosed()) {
                            socket.write(buffer, 0, count);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(MediaNetworkSender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        // socket.flush();

    }

    /**
     * Sends the play command to the player over the socket.
     */
    public void sendPlayCommand() {
        synchronized (socket) {
            if (socket.isClosed()) {
                return;
            }
            socket.sendData(CommunicationProtocol.PLAY_COMMAND_HEADER.getValue());

        }
    }

    /**
     * Sends the pause command to the player over the socket.
     */
    public void sendPauseCommand() {
        synchronized (socket) {
            if (socket.isClosed()) {
                return;
            }
            socket.sendData(CommunicationProtocol.PAUSE_COMMAND_HEADER.getValue());
        }
    }

    /**
     * Sends the stop command to the player over the socket.
     */
    public void sendStopCommand() {
        synchronized (socket) {
            if (socket.isClosed()) {
                return;
            }
            socket.sendData(CommunicationProtocol.STOP_COMMAND_HEADER.getValue());
        }
    }

    /**
     * Sends a set-volume command to the connected player via the socket. It it
     * can send any value regardless of the player's limitations.
     *
     * @param volume An integer representing the volume level.
     */
    public void sendVolumeSet(int volume) {
        synchronized (socket) {
            if (socket.isClosed()) {
                return;
            }
            socket.sendData(CommunicationProtocol.VOLUME_SET_HEADER.getValue());
            socket.sendData(Integer.toString(volume));
        }
    }

    /**
     * As for the alpha build this status is not always reliable since there is
     * no keep alive implemented in the system, thus not allowing the user to
     * know if the connection still exists.
     *
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Closes the socket.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void close() throws IOException {
        socket.close();
        connected = false;

    }
}
