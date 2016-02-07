package soundclient.media;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author lsdpirate
 */
public class PlaylistTracker extends Observable {

    private LinkedList<Media> playlist;

    public PlaylistTracker() {
        playlist = new LinkedList<>();
    }

    public void addMediaToQueue(Media m) {
        try {
            synchronized (playlist) {
                playlist.add(m.clone());
                super.setChanged();
                super.notifyObservers();
            }
        } catch (CloneNotSupportedException ex) {

        }
    }

    public void dequeueMedia() {
        synchronized (playlist) {
            playlist.remove(0);
            super.setChanged();
            super.notifyObservers();
        }
    }

    public Media getCurrentlyPlaying() {
        return playlist.get(0);
    }

    @Override
    public synchronized void addObserver(Observer obsrvr) {
        super.addObserver(obsrvr);
    }

}
