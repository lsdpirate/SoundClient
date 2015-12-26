/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundclient.media;

import io.FileExplorer;
import java.io.File;
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
            File f = new File(p); //To change in future, this class shouldn't have to work with File
            Media newMedia = new Media(f.getAbsolutePath());
            newMedia.setName(f.getName());
            medias.add(newMedia);
        }
    }

    public void addLibrary(String library) throws Exception {

        importFromLibrary(library);
        pathsToLibraries.add(library); //Not safe, must check if the import was
        //successful

    }

    private void importFromLibrary(String libraryPath) throws Exception {
        String[] medias = fileExplorer.getAllMediaFromPath(libraryPath);
        if (medias.length < 1) {
            throw new Exception("No medias found");
        }
        for (String s : medias) {
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
}
