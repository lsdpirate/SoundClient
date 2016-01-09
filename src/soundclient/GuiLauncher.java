package soundclient;

import io.MediaNetworkSender;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import soundclient.media.MediaLibrary;
import user_interface.FXFormController;

/**
 * This class represents the entry point for the application if a gui is wanted.
 * It extends javafx's Application class, which is used to correctly launch a 
 * javafx gui. In the <code> main() </code> method <code> launch() </code>
 * is called, which causes <code> start()</code> to be invoked. In the 
 * <code> start()</code> method we create an fxml gui with an attached controller,
 * we then extract that controller to pass him the macro-controllers of our 
 * application.
 * @author lsdpirate
 */
public class GuiLauncher extends Application{

    private MediaNetworkSender mns;
    private MediaLibrary media;
    
    public GuiLauncher(){
        
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../user_interface/FXForm.fxml"));
        
        Parent root = (Parent)fxmlLoader.load();
        FXFormController c;
        c = fxmlLoader.<FXFormController>getController();
        c.init(mns = new MediaNetworkSender(), media = new MediaLibrary());
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String ... args){
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        mns.close();
    }

    
   
}
