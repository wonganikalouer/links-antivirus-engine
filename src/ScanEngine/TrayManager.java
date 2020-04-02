/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScanEngine;

import RegistrationEngine.GSLib;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class TrayManager {
TrayIcon ti;
SystemTray st;
GSLib gs;
    TrayManager(SystemTray localTray, TrayIcon localIcon) {
        ti=localIcon;
        st=localTray;
        gs=new GSLib();
    }
    
    public void __init(){
    PopupMenu mainMenu=new PopupMenu();
        
        MenuItem open=new MenuItem("Open Scanner");
        MenuItem about=new MenuItem("About Links");
        Menu disable=new Menu("Disable Seurity");
        Menu hotspot=new Menu("Hotspot Manager");
        MenuItem update=new MenuItem("Check Update");
        MenuItem exit=new MenuItem("Exit Software",new MenuShortcut(KeyEvent.VK_L,true));
        MenuItem startHotspot=new MenuItem("Start Hotspot");
        MenuItem configureHotspot=new MenuItem("Configure");
        
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gs.executeExe("LinksAntivirus.exe");
            }
        });
        
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             //open GUI of About With Library Data
            }
        });
        
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gs.executeExe("Downloader.exe");
            }
        });
        
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        startHotspot.addActionListener((e)->{
            
            try{
            gs.executeExe(TrayManager.class.getResource("hotspot/createHotspot.bat").toExternalForm());
           JOptionPane.showMessageDialog(null, "Hostpot Created successfully");
            }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            }
           
        });
        
        
        hotspot.add(startHotspot);
        hotspot.add(configureHotspot);
        //Hotspot data is ready
        
        //add the popup menu
        mainMenu.add(open);
        mainMenu.add(about);
        mainMenu.add(hotspot);
        mainMenu.add(update);
        mainMenu.addSeparator();
        mainMenu.add(exit);
        ti.setPopupMenu(mainMenu);
    }
    
}
