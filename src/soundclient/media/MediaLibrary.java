package soundclient.media;

import io.FileExplorer;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * MediaLibrary is a class which manages a list of medias and imports them from the
 * file system individually or from an entire folder.
 * @author lsdpirate
 */
public class MediaLibrary {

    private ArrayList<Media> medias = new ArrayList<>();
    private ArrayList<String> pathsToLibraries = new ArrayList<>();
    private FileExplorer fileExplorer = new FileExplorer();

    public MediaLibrary() {
    }

    /**
     * Adds a media from an entered path. If the media is not found, nothing
     * happens.
     * @param p The path of the media. 
     */
    public void addMediaFromPath(String p) {
        if (fileExplorer.checkFileExists(p)) {
            Media newMedia = new Media(p);
            newMedia.setName(fileExplorer.getFileName(p));
            if(!checkIfPresent(newMedia))medias.add(newMedia);
        }
    }

    /**
     * Adds the path into a list and all medias files located in the given folder
     * into the medias list.
     * @param library The folder to load the files from.
     * @throws Exception In case the entered path doesn't exist.
     */
    public void addLibrary(String library) throws Exception {

        //This try...catch block might be useless but we'll leave it here
        try {
            importFromLibrary(library);
            pathsToLibraries.add(library);
        } catch (FileNotFoundException ex) {
            throw ex;
        }catch (IllegalArgumentException ex){
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Adds all medias from a specified folder.
     * @param libraryPath The path to the folder.
     * @throws Exception If the given path doesn't lead to a folder or 
     * doesn't exist.
     * @throws IllegalArgumentException If the entered path was null.
     */
    private void importFromLibrary(String libraryPath) throws Exception {
        if (libraryPath == null)throw new IllegalArgumentException("The entered path was null");
        String[] mediasPaths = fileExplorer.getAllMediaFromPath(libraryPath);
        if (mediasPaths.length < 1) {
            throw new Exception("No medias found");
        }
        for (String s : mediasPaths) {
            addMediaFromPath(s);
        }
    }

    /**
     * Imports all medias from all paths present in the list of libraries.
     * @throws Exception If any of the paths isn't valid or doesn't exist.
     */
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

    /**
     * Checks if a media is already present in the list.
     * @param newMedia The media to check.
     * @return True if the media exists, otherwise False.
     */
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
