package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import soundclient.media.Media;
import soundclient.media.PlaylistTracker;
import util.MainLogger;

/**
 * The MediaNetworkSender class (name TBD) is the class which manages the
 * network I/O of the application. It can send media and commands to the
 * connected player.
 *
 * @author lsdpirate
 */
public class MediaNetworkSender {

    /*
     Will own 2 instances of ClientSocket, one instance will be used for
     data sending while the other will be reserved for player/client dialog.
     */
    private final ClientSocket dataSocket;
    private boolean connected;
    private final ClientSocket commandSocket;
    private boolean peerListening;
    private PlaylistTracker playlistTracker;

    /**
     * Default constructor for MediaNetworkSender. This does not do anything
     * relevant.
     */
    public MediaNetworkSender() {
        dataSocket = new ClientSocket();
        commandSocket = new ClientSocket();

    }

    public MediaNetworkSender(PlaylistTracker playlistTracker) {
        this();
        this.playlistTracker = playlistTracker;
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
        if (!dataSocket.isClosed()) {
            dataSocket.close();
            connected = false;
        }
        if (!commandSocket.isClosed()) {
            commandSocket.close();

        }

        if (serverPort < 0) {
            throw new IllegalArgumentException("Port number is invalid " + serverPort);
        }
        if (serverIp.length() < 1) {
            throw new IllegalArgumentException("The specified ip isn't valid: " + serverIp);
        }
        dataSocket.connect(serverIp, serverPort);
        MainLogger.log(Level.FINE, "Data connection established");

        commandSocket.connect(serverIp, serverPort + 1);
        MainLogger.log(Level.FINE, "Command connection established");

        connected = true;
        peerListening = true;

        //Anonymous thread that whatches for any messages coming from the 
        //connected player via the commandSocket.
        new Thread("Command reader thread") {
            @Override
            public void run() {
                while (connected) {
                    String msg = "";
                    try {
                        msg = commandSocket.read();
                    } catch (IOException ex) {
                        MainLogger.log(Level.SEVERE, "Could not get command's inputstream", ex);
                    }
                    switch (msg) {
                        case "SSC":     //CommunicationProtocol.STOP_STREAM_CODE
                            peerListening = false;
                            break;
                        case "SC": //CommunicationProtocol.SHUTDOWN_CODE
                            peerListening = false;
                        case "PN": //CommunicationProtocol.PLAY_NEXT
                            if (playlistTracker != null) {
                                playlistTracker.dequeueMedia();
                            }
                             {
                                try {
                                    close();
                                } catch (IOException ex) {
                                    MainLogger.log(Level.SEVERE, "Could not close sockets", ex);
                                }
                            }
                    }

                }
            }
        }.start();
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
        if (dataSocket.isClosed()) {
            throw new IOException("No connection is present");
        }

        peerListening = true;

        File audioFile = new File(m.getPath());
        MainLogger.log(Level.FINE, "Sending media via network " + m.getName());
        //Send the header for the communication protocol
        long fileLength = audioFile.length();
        String header = CommunicationProtocol.SONG_HEADER.getValue() + Long.toString(fileLength);

        //Send the file
        FileInputStream fis = new FileInputStream(audioFile);
        byte[] buffer = new byte[(int) audioFile.length()];

        if (playlistTracker != null) {
            playlistTracker.addMediaToQueue(m);
        }

        new Thread("Sender thread for: " + m.getName()) {
            @Override
            public void run() {
                try {

                    fis.read(buffer);
                    //commandSocket.sendData(CommunicationProtocol.INCOMING_DATA.getValue());

                    synchronized (dataSocket) {
                        dataSocket.sendData(CommunicationProtocol.START_OF_FILE.getValue());
                        dataSocket.flush();
                        Thread.sleep(100);
                        for (byte b : buffer) {

                            if (!peerListening) {
                                break;
                            }
                            dataSocket.write(b);
                        }
                        dataSocket.flush();
                        Thread.sleep(1000);
                        dataSocket.sendData(CommunicationProtocol.END_OF_FILE.getValue());
                    }

                    //Transfer transfer = new Transfer("TRS " + m.getName());
                    //OutBufferTracker.addTransfer(transfer);
                    // int count;
                    //while ((count = fis.read(buffer)) != -1 && !socket.isClosed()) {
                    //  socket.write(buffer, 0, count);
                    //completion:percentage=filesize
                    //  int perc = count * 100 / (int)audioFile.length();
                    //   transfer.setTransferStatus(perc);
                    //}
                    // OutBufferTracker.removeTransfer(transfer);
                } catch (IOException ex) {
                    MainLogger.log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MediaNetworkSender.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.start();

    }

    /**
     * Sends the play command to the player over the socket.
     */
    public void sendPlayCommand() {

        if (commandSocket.isClosed()) {
            return;
        }
        try {
            commandSocket.flush();
        } catch (IOException ex) {
            MainLogger.log(Level.SEVERE, null, ex);
        }
        commandSocket.sendData(CommunicationProtocol.PLAY_COMMAND_HEADER.getValue());

    }

    /**
     * Sends the pause command to the player over the socket.
     */
    public void sendPauseCommand() {
        if (commandSocket.isClosed()) {
            return;
        }
        commandSocket.sendData(CommunicationProtocol.PAUSE_COMMAND_HEADER.getValue());
    }

    /**
     * Sends the stop command to the player over the socket.
     */
    public void sendStopCommand() {
        if (commandSocket.isClosed()) {
            return;
        }
        peerListening = false;
        commandSocket.sendData(CommunicationProtocol.STOP_COMMAND_HEADER.getValue());
    }

    /**
     * Sends a set-volume command to the connected player via the socket. It it
     * can send any value regardless of the player's limitations.
     *
     * @param volume An integer representing the volume level.
     */
    public void sendVolumeSet(int volume) {
        if (commandSocket.isClosed()) {
            return;
        }
        commandSocket.sendData(CommunicationProtocol.VOLUME_SET_HEADER.getValue());
        commandSocket.sendData(Integer.toString(volume));

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
        connected = false;
        dataSocket.close();
        commandSocket.close();
        MainLogger.log(Level.INFO, "Sockets have been closed");

    }

    public PlaylistTracker getPlaylistTracker() {
        return this.playlistTracker;
    }
}
