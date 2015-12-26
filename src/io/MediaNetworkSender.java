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
     Will own an instance of ClientSocket and a Thread of it
     */
    private ClientSocket socket;

    public MediaNetworkSender() {
    }

    public void connectToSocket(String serverIp, int serverPort) throws IOException {
        if (socket != null) {
            //To implement: cleanup operations
        }

        socket = new ClientSocket(serverIp, serverPort);
    }

    public void sendMediaOverNetwork(Media m) throws IOException {
        if (socket == null) {
            return;
        }
        File audioFile = new File(m.getPath());
        System.out.println(audioFile.getName());
        
        FileInputStream fis = new FileInputStream(audioFile);
        byte[] buffer = new byte[(int) audioFile.length()];
        int count;
        while ((count = fis.read(buffer)) != -1) {
            socket.getOutputStream().write(buffer, 0, count);
        }


    }
}
