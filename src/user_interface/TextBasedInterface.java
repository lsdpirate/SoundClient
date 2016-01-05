/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import io.MediaNetworkSender;
import java.io.IOException;
import java.util.Scanner;
import soundclient.media.Media;
import soundclient.media.MediaLibrary;

/**
 *
 * @author lsdpirate
 */
public class TextBasedInterface {
    private static String motd = "SoundClient v 0.8 - the music streaming client!";
    Scanner scanner = new Scanner(System.in);
    MediaNetworkSender mns;
    MediaLibrary medias;

    public TextBasedInterface() {
        mns = new MediaNetworkSender();
        medias = new MediaLibrary();
        System.out.println(motd);
        readInput();
    }

    private void readInput() {

        String input = "";
        do {
            System.out.print("prompt:");
            input = scanner.next();
            if (input.equals("load")) {

                try {
                    String path = scanner.nextLine();
                    path = path.trim();
                    System.out.println(path);
                    medias.addLibrary(path);

                    System.out.println("Loading from " + path);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (input.equals("add")) {
                String mediaPath = scanner.nextLine();
                mediaPath = mediaPath.trim();
                medias.addMediaFromPath(mediaPath);
            } else if (input.equals("connect")) {
                String ip = scanner.next();
                int port = scanner.nextInt();
                System.out.println("Connecting to " + ip + "at " + port);

                try {
                    mns.connectToSocket(ip, port);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else if (input.equals("list")) {
                int col = 0;
                int index = 1;
                for (Media m : medias.getMedias()) {
                    if (col == 3) {
                        System.out.println("");
                        col = 0;
                    }
                    System.out.print(index + ")");
                    System.out.print(m.getName());
                    System.out.print("\t");
                    ++index;
                    ++col;
                }
                System.out.println("");
            } else if (input.equals("q")) {
                int index = scanner.nextInt();
                Media toPlay = medias.getMedias().get(index - 1);

                try {
                    mns.sendMediaOverNetwork(toPlay);
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }
            }else if(input.equals("play")){
                mns.sendPlayCommand();
            }else if(input.equals("pause")){
                mns.sendPauseCommand();
            }else if(input.equals("stop")){
                mns.sendStopCommand();
            }else if (input.equals("volume")){
                mns.sendVolumeSet(scanner.nextInt());
            }

        } while (!input.equals("exit"));
    }
}
