/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RegistrationEngine;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */

public class LinksDataStructure {
    public String APPDATA_DIR=System.getenv("APPDATA"); 
    public String SYSTEM_DIR=System.getenv("LOCALAPPDATA")+"/Links Antivirus";    
    public String QUARANTINE_FILE=SYSTEM_DIR+"/quarantine.lnks";
    public String SETTINGS_FILE=SYSTEM_DIR+"/settings.lnks";
    public String SECURITY_FILE=SYSTEM_DIR+"/security.lnks";
    public String SHEDULE_FILE=SYSTEM_DIR+"/schedules.lnks";
    public String LINKS_USER_DATA=SYSTEM_DIR+"/userdata.lnks";
    public String UPDATE_FOLDER=SYSTEM_DIR+"/Update/";
    public String THEME_SETTINGS=SYSTEM_DIR+"/theme.lth";
    public String REGISTRATION_FILE=APPDATA_DIR+"/win.dll";
    public String DATE_FILE=APPDATA_DIR+"/system32.dll";
    public int TRIAL_PERIOD=15;
    public String QUICK_DIR_SCANNER=System.getenv("LOCALAPPDATA");
    public String APPLICATION_VERSION="2.0.3";//THIS IS used for checking user updates
    GSLib gs=new GSLib();
    public String BAT_SYSWIN;
    public String BAT_VSHOST32;
    String softwareVersion="2.0.35";
    SystemTray localSystemTray;
    public LinksDataStructure(){
    BAT_SYSWIN=getClass().getResource("bat/syswin_task_kill.bat").toExternalForm();
    BAT_VSHOST32=getClass().getResource("bat/v8.bat").toExternalForm();
    }
    public void init(){
        //prepare folders and directories for saving links antivirus files
        if(!new File(SYSTEM_DIR).exists()){
            new File(SYSTEM_DIR).mkdir();
            try {
                new File(QUARANTINE_FILE).createNewFile();
                new File(SETTINGS_FILE).createNewFile();
                new File(SECURITY_FILE).createNewFile();
                new File(SHEDULE_FILE).createNewFile();
                new File(REGISTRATION_FILE).createNewFile();
                new File(LINKS_USER_DATA).createNewFile();
                new File(THEME_SETTINGS).createNewFile();
                new File(UPDATE_FOLDER).mkdir();
                gs.appendFile(DATE_FILE, "0");
            } catch (IOException ex) {
                Logger.getLogger(LinksDataStructure.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String[] readSettings(){
    return gs.readFile(SETTINGS_FILE).split(";");
    }
    
    public void saveSettings(String[] s){
    //after updaing data, settings wiill have to be saved here
        new File(SETTINGS_FILE).delete();
        String settingsSetup=s[0]+";"+s[1];
        gs.appendFile(SETTINGS_FILE, settingsSetup);
    }
    
    public String getVersion(){
    return this.softwareVersion;
    }

    public void manualTray(SystemTray sy) {
    //you can setup add anything in the system tray after updating, deleting the UI
    localSystemTray=sy;
//    localSystemTray.getTrayIcons()[0].displayMessage("test UI", "This is a message from the library", TrayIcon.MessageType.ERROR);
    }
}
