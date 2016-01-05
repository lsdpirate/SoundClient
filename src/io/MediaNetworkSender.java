/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import soundclient.media.Media;

/**
 *
 * @author lsdpirate
 */
public class MediaNetworkSender {

    /*
     Will own an instance of ClientSocket and a Thread of it.
     The thread will be used mostly for keep alives
     */
    private ClientSocket socket;
    private boolean connected;
    
    public MediaNetworkSender() {
    }

    public void connectToSocket(String serverIp, int serverPort) throws IOException, IllegalArgumentException {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        
        if(serverPort < 0){
            throw new IllegalArgumentException("Port number is invalid " + serverPort);
        }
        if(serverIp.length() < 1){
            throw new IllegalArgumentException("The specified ip isn't valid: " + serverIp);
        }
        socket = new ClientSocket(serverIp, serverPort);
        connected = true;
    }

    public void sendMediaOverNetwork(Media m) throws IOException {
        if (socket == null) {
            throw new IOException("No connection is present");
        }
        File audioFile = new File(m.getPath());
        System.out.println(audioFile.getName());

        //Send the header for the communication protocol
        long fileLength = audioFile.length();
        String header = CommunicationProtocol.SONG_HEADER + Long.toString(fileLength);

        
        //Send the file
        FileInputStream fis = new FileInputStream(audioFile);
        byte[] buffer = new byte[(int) audioFile.length()];
        int count;
        while ((count = fis.read(buffer)) != -1) {
            socket.write(buffer, 0, count);
        }
        socket.flush();
        
    }
    
    public void sendPlayCommand(){
        if(socket == null) return;
        socket.sendData(CommunicationProtocol.PLAY_COMMAND_HEADER);
    }
    
    public void sendPauseCommand(){
        if(socket == null) return;
        socket.sendData(CommunicationProtocol.PAUSE_COMMAND_HEADER);
    }
    
    public void sendStopCommand(){
        if (socket == null) return;
        socket.sendData(CommunicationProtocol.STOP_COMMAND_HEADER);
    }
    
    public void sendVolumeSet(int volume){
        if(socket == null) return;
        socket.sendData(CommunicationProtocol.VOLUME_SET_HEADER);
        socket.sendData(Integer.toString(volume));
    }
    /**
     * As for the alpha build this status is not always reliable since there is
     * no keep alive implemented in the system, thus not allowing the user to know
     * if the connection still exists.
     * @return 
     */
    public boolean isConnected(){
        return connected;
    }
}
