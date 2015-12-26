/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author lsdpirate
 */
public class FileExplorer {

    private static final String[] acceptedExtensions = {".mp3", ".wav"};
    private static FilenameFilter filenameFilter;

    public FileExplorer() {

        filenameFilter = (File dir, String name) -> {
            boolean result = false;
            if (name.lastIndexOf(".") > 0) {

                int lastIndex = name.lastIndexOf(".");
                String str = name.substring(lastIndex);

                for (String ae : acceptedExtensions) {
                    if (ae.equals(str)) {
                        result = true;
                    }
                }
            }

            return result;
        };
    }

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
                    } else {
                        result = false;
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

    public String[] getAllMediaFromPath(String path) {
        String[] result;

        File folder = new File(path);

        if (!folder.isDirectory()) {
            //Throw exception if the given path is not a folder
        }

        //Make a list of all files in the given folder that are audio files.
        File[] listOfFiles = folder.listFiles(filenameFilter);


        result = new String[listOfFiles.length];

        for (int i = 0; i < result.length; ++i) {
            result[i] = listOfFiles[i].getAbsolutePath();
        }

        return result;
    }

}
