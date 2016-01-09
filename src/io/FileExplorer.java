package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

/**
 * FileExplorer is the class which manages all the local file I/O for 
 * the application. It has a filter for media types which is currently (1.0)
 * hardcoded. It provides different methods to retrieve and verify files as well
 * as to retrieve all medias from a path.
 * @version 1.0
 * @author lsdpirate
 */
public class FileExplorer {

    private static final String[] acceptedExtensions = {".mp3", ".wav"};
    private static FilenameFilter filenameFilter;

    /**
     * Creates a FileExplorer with a set filter for the specified files in the
     * acceptedExtension[] array. This is hardcoded for now (1.0) but can easily 
     * be tranformed into a more modular structure.
     */
    public FileExplorer() {

        filenameFilter = (File dir, String name) -> {
            boolean result = false;
            int lastIndex;
            //Look for the index of the last point in the filename
            if ((lastIndex = name.lastIndexOf(".")) > 0) { 
                
                //Create a string containing the file's extension
                String str = name.substring(lastIndex);
                
                for (String ae : acceptedExtensions) {
                    if (ae.equals(str)) {
                        result = true;
                        break;
                    }
                }
            }
            
            return result;
        };
    }

    /**
     * Checks if a specified file exists and is an actual file.
     * It also checks if the file is supported.
     * Returns a boolean representing the result.
     * @param path The path for the file to be checked
     * @return True if the file exists and isn't a directory, otherwise returns
     * false.
     * @throws NullPointerException 
     */
    public boolean checkFileExists(String path) throws NullPointerException {
        boolean result = false;
        try {
            File file = new File(path);
            result = file.exists() & file.isFile();

            String fileName = file.getAbsolutePath();
            int index;

            if ((index = fileName.lastIndexOf(".")) > 0) {
                String str = fileName.substring(index);
                for (String ar : acceptedExtensions) {
                    if (ar.equals(str)) {
                        result = result & true;
                        break;
                    }
                }
            } else {
                result = false;
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("The inserted path was null");
        }
        return result;
    }
    
    /**
     * Utility method to get the file name from a filepath.
     * @param p The path of the file
     * @return The name of the specified file, null if the file doesn't exist
     */
    public String getFileName(String p){
        File f = new File(p);
        return f.getName();
    }
    
    /**
     * Returns a string array containing all paths of supported media files.
     * @param path The path to check.
     * @return A string array of all valid medias' paths. Returns null if no
     * medias were found.
     * @throws FileNotFoundException If the path doesn't exists.
     */
    public String[] getAllMediaFromPath(String path) throws FileNotFoundException {
        String[] result;

        File folder = new File(path);

        if (!folder.isDirectory()) {
            throw new FileNotFoundException("The specified path is not a folder");
        }

        //Make a list of all files in the given folder that are supported files.
        File[] listOfFiles = folder.listFiles(filenameFilter);

        result = new String[listOfFiles.length];

        for (int i = 0; i < result.length; ++i) {
            result[i] = listOfFiles[i].getAbsolutePath();
        }

        return result;
    }

}
