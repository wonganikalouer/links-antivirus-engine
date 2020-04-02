package UpdateEngine;

import RegistrationEngine.GSLib;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import RegistrationEngine.LinksDataStructure;
import java.io.IOException;
import javafx.application.Platform;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
public class UpdateEngine {
    private static String libVersion="1.0.0";
    private static String webpageURL="http://www.linksantivirus.byethost33.com/update.php?version="+libVersion;
//    private static String webpageURL="file:/D:/update.html";
    WebView netConnector;
    WebView ui;
    public UpdateEngine(WebView we){
        this.ui=we;
        netConnector=new WebView();
    
    //net connector settings
        netConnector.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>(){
            private JSObject jsobject;

            @Override
            public void changed(ObservableValue observable, Worker.State oldValue, Worker.State newValue) {
                if(newValue.equals(Worker.State.SUCCEEDED)){
//                    System.out.println("Loaded the page");
//                    ui.getEngine().executeScript("Toast(\"work in progress\")");
                   //load document online
                    
                    String data=netConnector.getEngine().executeScript("getUploadData()").toString();
                    String raw[]=data.split(";");
                    String version=raw[0].split("=")[1];
                    String date=raw[1].split("=")[1];
                    String features=raw[2].split("=")[1];
                    String downloadLink=raw[3].split("=")[1];
                    
                    if(!version.equals(UpdateEngine.libVersion)){
                    //update is good to download
//                    ui.getEngine().executeScript("Toast(\"Downloading Update <br>Version : "+version+"\")");
                    startDownload(downloadLink);
                    ui.getEngine().executeScript("Toast('download started')");
                    
                    }else{
//                    ui.getEngine().executeScript("To/ast(\"You are already up to date\")");
                    }
                
                }
                
                else if(newValue.equals(Worker.State.FAILED)){
                ui.getEngine().executeScript("stopDownload()");
                ui.getEngine().executeScript("Toast('No Internet connection available<br><br><hr>Try Again Later',8)");
                }
                
            }

            
                });
        
        //net connector downloader
    }

    public void start() {
        //begin downloading the files here
    netConnector.getEngine().load(UpdateEngine.webpageURL);
    }
    
    
    //begin downloading file here
    private void startDownload(String newLoc){
    Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    System.out.println("Downloading update Started Now");
                    File saveFile = new File(new LinksDataStructure().UPDATE_FOLDER+"updateEngine.lnks");
                    try (BufferedInputStream in = new BufferedInputStream(new URL(newLoc).openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(saveFile)) {
    byte dataBuffer[] = new byte[1024];
    int bytesRead;
    int p=in.available();
    int total=p;
    int pp=0,l=0;
    ui.getEngine().executeScript("updateProgress("+0+")");
    ui.getEngine().executeScript("setProgressTotal("+total+")");
    
    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
        l=(((p-total)*-1));
        Thread.sleep(100);
        ui.getEngine().executeScript("updateProgress("+l+")");
        p=p-1024;
        
    }
                //copy the files to the designated folder as a finishing touch using cmd
                Object executeScript = ui.getEngine().executeScript("updateProgress("+total+")");
  
                //downloading completed
              ui.getEngine().executeScript("installingUpdates(\"(1 of 3)\")");
              //extractting files
              File bat=new File(new LinksDataStructure().SYSTEM_DIR+"/temp.bat");
              if(bat.exists()){
              bat.delete();
              }
              String batData="taskkill /F /IM javaw.exe\n"
                      + "copy \""+saveFile.getAbsolutePath().replace("/", "\\")+"\" "
                      + "\"C:\\Links Antivirus\\lib\\LinksEngine.jar\"\n"
                      + "\"C:\\Links Antivirus\\agent1.exe\" update";
              new GSLib().appendFile(bat.getAbsolutePath(), batData);
              
              
              ui.getEngine().executeScript("installingUpdates(\"(2 of 3)\")");
              //stoping servies and agents
              Platform.runLater(new Runnable(){
        @Override
        public void run() {
                  new RegistrationEngine.GSLib().executeExe(bat.getAbsolutePath());
        
        }
    });
              Platform.exit();
              //exit application
              ui.getEngine().executeScript("installingUpdates(\"Complete!\")");

    

            } catch (Exception e) {
    // handle exception
//                System.err.println("could not reach "+e.getMessage());
                ui.getEngine().executeScript("Toast(\""+e.getMessage()+"\",6)");
}
                }
            });
    }

}
