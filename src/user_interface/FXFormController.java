/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import io.MediaNetworkSender;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import soundclient.media.Media;
import soundclient.media.MediaLibrary;

public class FXFormController implements Initializable {

    @FXML
    private ListView<Media> lbMedias;

    @FXML
    private MenuItem miLoadMedia;

    @FXML
    private Slider slVolume;

    @FXML
    private Button btPlay;

    @FXML
    private TextField tfPort;

    @FXML
    private MenuItem miClose;

    @FXML
    private Label lStatus;

    @FXML
    private Button btPause;

    @FXML
    private TextField tfIp;

    @FXML
    private Button btStop;

    @FXML
    private MenuItem miStop;

    @FXML
    private MenuItem miPause;

    @FXML
    private MenuItem miLoadFolder;

    @FXML
    private MenuItem miPlay;

    @FXML
    private Button btConnect;

    @FXML
    private Button btEnq;

    MediaNetworkSender mns;
    MediaLibrary medias;
    ObservableList<Media> list;

    @FXML
    public void playClicked(MouseEvent me) {
        play();
    }

    @FXML
    public void pauseClicked(MouseEvent me) {
        pause();
    }

    @FXML
    public void stopClicked(MouseEvent me) {
        stop();
    }

    @FXML
    public void volumeChanged(MouseEvent me) {
        int val = (int) slVolume.getValue();
        mns.sendVolumeSet(val);
    }
    
    @FXML
    public void closeClicked(ActionEvent ae){
        Platform.exit();
    }

    @FXML
    public void enqClicked(MouseEvent me) {
        Media selectedMedia = lbMedias.getSelectionModel().getSelectedItem();
        if (selectedMedia != null) {

            try {
                mns.sendMediaOverNetwork(selectedMedia);
            } catch (IOException ex) {
                
                //Show a message to the user
                Alert a = new Alert(AlertType.WARNING, "Could not send the media to queue, no connection to the player");
                a.showAndWait();

                Logger.getLogger(FXFormController.class.getName()).log(Level.SEVERE, "No connection", ex);
            }
        }
    }
    
    @FXML
    public void connectClicked(MouseEvent me) {
        connect(tfIp.getText(), tfPort.getText());
    }

    @FXML
    public void loadFromFolderClicked(ActionEvent ae) {
        loadFromFolder();
    }
    
    @FXML
    public void loadMediaClicked(ActionEvent ae){
        loadMedia();
    }

    private void connect(String ip, String port) {
        int intPort = Integer.parseInt(port);

        try {
            mns.connectToSocket(ip, intPort);
        } catch (IOException ex) {
            Logger.getLogger(FXFormController.class.getName()).log(Level.SEVERE, "Connection failed", ex);
        } catch (IllegalArgumentException ex) {
            //No input was given, there is no need to take action
        }
        if (mns.isConnected()) {
            lStatus.setText("Status: connected");
            lStatus.setTextFill(Color.GREEN);
            Logger.getLogger(FXFormController.class.getName()).log(Level.FINE, "Connected to {0} {1}", new Object[]{ip, port});
        } else {
            lStatus.setText("Status: disconnected");
            lStatus.setTextFill(Color.RED);
            Logger.getLogger(FXFormController.class.getName()).log(Level.FINE, "Connection failed");
        }

    }

    private void play() {
        mns.sendPlayCommand();
    }

    private void stop() {
        mns.sendStopCommand();
    }

    private void pause() {
        mns.sendPauseCommand();
    }

    private void loadFromFolder() {
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("Load Folder");
        String path = folderChooser.showDialog(null).getAbsolutePath();
        if (path != null) {
            try {
                medias.addLibrary(path);
            } catch (Exception ex) {
                Logger.getLogger(FXFormController.class.getName()).log(Level.SEVERE, "Could not load the specified folder", ex);
            }
        }
        list = FXCollections.observableArrayList(medias.getMedias());
        lbMedias.setItems(list);

    }

    private void loadMedia() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load media...");
        String path = fc.showOpenDialog(null).getAbsolutePath();
        if(path != null){
            try{
                medias.addMediaFromPath(path);
            }catch (Exception ex) {
                Logger.getLogger(FXFormController.class.getName()).log(Level.SEVERE, "Could not load the specified folder", ex);
            }
        }
        list = FXCollections.observableArrayList(medias.getMedias());
        lbMedias.setItems(list);
    }
    
    public void init(MediaNetworkSender mns, MediaLibrary ms){
        this.mns = mns;
        medias = ms;
        list = FXCollections.observableArrayList(medias.getMedias());
        lbMedias.setItems(list);
        
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

}
