package soundclient.media;

/**
 * A Media is a representation of a media file present in the user's system.
 *
 * @author lsdpirate
 */
public class Media implements Comparable, Cloneable {

    private String pathToMedia;
    private String name;

    public Media() {
    }

    /**
     * Creates a media with a set path.
     *
     * @param path The path to set.
     */
    public Media(String path) {
        pathToMedia = path;
    }

    /**
     * Creates a media with a path and a name.
     *
     * @param path The path of the media.
     * @param name The name of the media.
     */
    public Media(String path, String name) {
        this(path);
        setName(name);
    }

    /**
     * Returns the path of the media.
     *
     * @return Media's path.
     */
    public String getPath() {
        return pathToMedia;
    }

    /**
     * Set the name for the media.
     *
     * @param newName The new name of the media.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns the media's name.
     *
     * @return The media's name.
     */
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        int result = 0;
        if (o instanceof Media) {
            result = ((Media) o).getPath().compareTo(pathToMedia);
        }
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    protected Media clone() throws CloneNotSupportedException {
        Media m = new Media(this.pathToMedia);
        m.setName(this.name);
        return m;
    }
    

}
