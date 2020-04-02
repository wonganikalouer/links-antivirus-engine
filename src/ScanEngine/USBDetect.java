//external usb detecting tool in the library, will be easily updated such
package ScanEngine;

import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.io.File;
import RegistrationEngine.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;
public class USBDetect implements Runnable{
    //<editor-fold desc="variables">
    SystemTray localTray;
    TrayIcon localIcon;
    Image image;
    GSLib gs=new GSLib();
    LinksDataStructure lds=new LinksDataStructure();
    ArrayList<String> drives_=new ArrayList();
    private static final String WMIC_PATH = System.getenv("WINDIR") + "\\System32\\wbem\\wmic.exe";

    private static final String CMD_WMI_ARGS = "logicaldisk where DriveType=2 get DeviceID";
    private static final String CMD_WMI_USB = WMIC_PATH + " " + CMD_WMI_ARGS;
    private BufferedReader input;
    //</editor-fold>
    public USBDetect(){
    //<editor-fold desc="constructor" >
    //setup the tray icons and alert system before final execution
    localTray=SystemTray.getSystemTray();
    //get icon from external library
    image=new ImageIcon(getClass().getResource("animated.gif")).getImage();
    localIcon=new TrayIcon(image);
    localIcon.setImageAutoSize(true);
    localIcon.setToolTip("Links Antivirus Agent");
    setupMenu();
    //</editor-fold>
    //tray Icon is ready in the system
    }
    
    @Override
    public void run() {
        while(true){
        //the main loop
        removeZB();
        try{detectUSB();}catch(Exception e){}
        try{Thread.sleep(1000);}catch(Exception e){}
        }
    }

    private void detectUSB() throws Exception {
        Process process = Runtime.getRuntime().exec(CMD_WMI_USB);
        input=new BufferedReader(new InputStreamReader(process.getInputStream()));
        String d,drives="";
        while((d=input.readLine())!=null){
        drives+=d;
        }
        drives=drives.replace("DeviceID", "");
        drives=drives.replace(" ", "");
      
        //System.out.println(drives);
        String[] drv=drives.split(":");
        if(drv.length>drives_.size()){
                //checkRemoved(drv);
                for(int i=0;i<drv.length;i++){
                    if(!checkDrive(drv[i]) && !drv[i].equals("") && new File(drv[i]).getTotalSpace()>0){
                    drives_.add(drv[i]);
                    JOptionPane.showMessageDialog(null, "Drove loaded is "+new File(drv[i]).getTotalSpace(), "Drive Info", 1);
                    //scan this Drive
                    DiskEngine engine=new DiskEngine(new File(drv[i]+":/"),localIcon);
                    Thread th=new Thread(engine);
                    th.start();
            }
        }
        
        }else {
                checkRemoved(drv);
               
            }
    }
    
    //check removed
    public boolean checkDrive(String dr){
        for(int k=0;k<drives_.size();k++){
            
            if(drives_.get(k).equals(dr)){
                //already exists
                return true;
            }
        }
        return false;
    }
    
    //This method removes all removed Disks fromm the system
    private void checkRemoved(String[] dr) {
        boolean drive_exist=false;
        int p=0;
        for(int k=0;k<dr.length;k++){
            for(int j=0;j<drives_.size();j++){
            if(dr[k].equals(drives_.get(j))){
            break;
            }else{
                //drive was not found so remove it from disk database
                drives_.remove(j);
            }
            }
            
        }
    }

    //<editor-fold desc="send Message to tray">
    public void sendMessage(String d) {
        localIcon.displayMessage("Links Update Tool", d, TrayIcon.MessageType.INFO);
    }
    //</editor-fold>
    //<editor-fold desc="seting up trayicon into the system">
    private void setupMenu() {
        
        
        try{localTray.add(localIcon);
        TrayManager tm=new TrayManager(localTray,localIcon);
        tm.__init();
        }catch(Exception e){}
    }
    //</editor-fold>

    private void removeZB() {
       File f=new File(System.getenv("SystemDrive")+"/boots/syswin.exe");
       File f2=new File(lds.APPDATA_DIR+"/Dibifu_9/vshost32.exe");
       
       if(f.exists()){
           gs.executeBat(lds.BAT_SYSWIN);
           f.delete();
       localIcon.displayMessage("Links Action Manager", "A threat [syswin.exe] has been block from accessing your computer", TrayIcon.MessageType.WARNING);
       new ActionManager(localIcon).saveToDb(f, "Deleted");
       }
       if(f2.exists()){
            localIcon.displayMessage("Links Action Manager", "A threat [ Vshost32 ] has been block from accessing your computer", TrayIcon.MessageType.WARNING);
                new ActionManager(localIcon).saveToDb(f2, "Deleted");
           gs.executeBat(lds.BAT_VSHOST32);
          
           try{
               //this might need some administration permissions
               f2.delete();
//               gs.executeExe("TestPermissions.exe");
               f2.deleteOnExit();
           }catch(Exception e){
           }
           //wait two seconds to avoid classhing of codes
           Timer t=new Timer(2000, (e)->{
              
           });
//        localIcon.displayMessage("Links Action Manager", "A threat [ Vshost32 ] has been block from accessing your computer", TrayIcon.MessageType.WARNING);
//       new ActionManager(localIcon).saveToDb(f2, "Deleted");
       }
       
    }
}
