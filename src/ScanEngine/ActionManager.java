package ScanEngine;

import RegistrationEngine.GSLib;
import java.awt.TrayIcon;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import RegistrationEngine.LinksDataStructure;
public class ActionManager {
    TrayIcon localTrayIcon;
    GSLib gs=new GSLib();
    LinksDataStructure lds=new LinksDataStructure();
    
    public ActionManager(TrayIcon ti){
        //this class will be responsible for AI handling viruses
        //not every exe is a virus so analyze and save
        localTrayIcon=ti;
    }

    void handleZero(String path) {
        //handle zero bytes AI method
        //analyze file
        try{
        int fileSize=new FileInputStream(new File(path)).available()/1000000;
            if(fileSize==0){
            //normal file do not delete this file
                if((new FileInputStream(new File(path)).available()/1000)<=0){
                localTrayIcon.displayMessage("Links Action Manager", "File : "+new File(path).getName()+"\n"
                        + "Infected by Zero Bytes Detected is USB", TrayIcon.MessageType.WARNING);
                new File(path).delete();
                saveToDb(new File(path), "Deleted");
                }
                }else if(fileSize>3 && fileSize<5){
            //probably this is a zero byetes file, quarantine it
            localTrayIcon.displayMessage("Links Action Manager", "Highly Suspected File!\n"
                    + new File(path).getName()+" [ Deleted ]", TrayIcon.MessageType.WARNING);
            new File(path).delete();
            saveToDb(new File(path), "Deleted");
            }
            else{
            //this is a suspected file, nothing much, However
            //taking a step to quarantine it without deleting is much safer
            //Just in case
            localTrayIcon.displayMessage("Links Action Manager", "File : "+new File(path).getName()+"\n"
                        + "is Suspicious [Quarantined]", TrayIcon.MessageType.WARNING);
            saveToDb(new File(path), "Quarantined");
                
            }
        }catch(Exception e){}
    }

    void saveToDb(File fl,String action){
    //save this to database, will later be changed using settings
                           Date d=new Date();
                           String dboutput=fl.getName()+"<>"+d.toString()+"<>"+fl.getAbsolutePath().replace("\\", "/")+"<>"+action+";";
                           gs.appendFile(lds.QUARANTINE_FILE, dboutput);
    }
}
