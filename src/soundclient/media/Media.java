/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundclient.media;

/**
 *
 * @author lsdpirate
 */
public class Media implements Comparable{

    private String pathToMedia;
    private String name;

    public Media() {
    }

    public Media(String path) {
        pathToMedia = path;
    }

    public Media(String path, String name) {
        this(path);
        setName(name);
    }

    public String getPath() {
        return pathToMedia;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        if(o instanceof Media){
            result = ((Media)o).getPath().compareTo(pathToMedia);
        }
        return result;
    }
    
    @Override
    public String toString(){
        return getName();
    }
}
