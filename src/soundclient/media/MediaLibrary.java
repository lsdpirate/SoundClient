/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundclient.media;

import io.FileExplorer;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author lsdpirate
 */
public class MediaLibrary {

    private ArrayList<Media> medias = new ArrayList<>();
    private ArrayList<String> pathsToLibraries = new ArrayList<>();
    private FileExplorer fileExplorer = new FileExplorer();

    public MediaLibrary() {
    }

    public void addMediaFromPath(String p) {
        if (fileExplorer.checkFileExists(p)) {
            Media newMedia = new Media(p);
            newMedia.setName(fileExplorer.getFileName(p));
            if(!checkIfPresent(newMedia))medias.add(newMedia);
        }
    }

    public void addLibrary(String library) throws Exception {

        //This try...catch block might be useless but we'll leave it here
        try {
            importFromLibrary(library);
            pathsToLibraries.add(library);
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void importFromLibrary(String libraryPath) throws Exception {
        String[] mediasPaths = fileExplorer.getAllMediaFromPath(libraryPath);
        if (mediasPaths.length < 1) {
            throw new Exception("No medias found");
        }
        for (String s : mediasPaths) {
            addMediaFromPath(s);
        }
    }

    private void importFromAllLibraries() throws Exception {
        if (!pathsToLibraries.isEmpty()) {
            for (String p : pathsToLibraries) {
                importFromLibrary(p);
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (Media m : medias) {
            result += m.getPath() + "\n";
        }

        return result;
    }

    public ArrayList<Media> getMedias() {
        return medias;
    }

    public ArrayList<String> getPathsToLibraries() {
        return pathsToLibraries;
    }

    private boolean checkIfPresent(Media newMedia) {
        boolean result = false;
        
        for(Media m : medias){
            if(m.compareTo(newMedia) == 0){
                result = true;
                break;
            }
        }
        
        return result;
    }
}
