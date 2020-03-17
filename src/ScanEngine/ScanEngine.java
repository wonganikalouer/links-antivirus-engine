//This is the scan Engine, designed to be portable and easily updatable in the applicaction
//without this library, the system will therefore not work
package ScanEngine;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author acer
 */
public class ScanEngine {
    private String engineVersion="1.0.0";
      
    WebView webView;
    ArrayList<String> VIRUSES=new ArrayList<String>();
    public ScanEngine(WebView webView_ui) {
        //setup webview from parent class
        webView=webView_ui;    
    }
   
    
    public Runnable startEngine(File file){
    return new ScanThread(file);
    }
    
    
    
    
    public class ScanThread implements Runnable{
        File folder;
        long progres_=0;
        long perc=0;
        long max=0;
         float p=0;
        private ScanThread(File file) {
            folder=file;
            max=file.getTotalSpace()-file.getFreeSpace();
//            System.err.println(max+" byets in total");
        }
        @Override
        public void run(){
            scanFolder(folder);
            sendToUI("updateScanUI('Scan Completed!')");
            
            if(VIRUSES.size()>0){
            //more than one virus is found
                System.err.println(VIRUSES.size()+" Viruses found");
            }else{
//                System.out.println("No virus found");
            }
            sendToUI("scanCompleted("+VIRUSES.size()+")");
                    
        }
        public void scanFolder(File f){
            try{
                Thread.sleep(300);
            //do the scanning hereS
                String folder_name=f.getName();
                File[] files=f.listFiles();
                for(File fl:files){
//                    Thread.sleep(50);
                   if(fl.isDirectory()){
                   scanFolder(fl);
                   }else{
                   //virus detection code goes heres
                       String zb=folder_name+".exe";
                       if(fl.getName().equals(zb)){
                       //zro byte agent
                           //added virus name to array
//                           System.err.println("found!");
                           VIRUSES.add(fl.getCanonicalPath());
                           sendToUI("_2n5ert_(\""+fl.getAbsolutePath().replace("\\", "/")+"\",\""+fl.getName().replace("\\", "/")+"\",\"Requires immediate action! Attacks users pc by multiplying itself in Removable Disk. It also deletes executable files.\")");
                       }
               
                       Platform.runLater(new Runnable(){
                       @Override
                       public void run(){
//                        perc+=readFileSize(fl);
                       p=(perc/max)*100;
//                        System.err.println(p+" %");
                       webView.getEngine().executeScript("updateScanUI('"+fl.getName()+"',"+perc+","+max+")");
                       }
                       });
                   }
                }
                
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
    
    //transfer data to user interface class
      public void sendToUI(String script){
    Platform.runLater(new Runnable(){
                       @Override
                       public void run(){
                       webView.getEngine().executeScript(script);
                       }
                       });
    }    
}
