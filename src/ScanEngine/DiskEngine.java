/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScanEngine;

import UIManager.UI;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javafx.application.Platform;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class DiskEngine implements Runnable{
     File folder;
        long progres_=0;
        long perc=0;
        long max=0;
         float p=0;
         
         TrayIcon localTray;
         ActionManager actionMan;
         UIManager.UI ui;
        public DiskEngine(File file,TrayIcon ti) {
            folder=file;
            if(file.equals(null)){
            return;
            }
            localTray=ti;
            actionMan=new ActionManager(localTray);
            max=file.getTotalSpace()-file.getFreeSpace();
            ui=new UIManager.UI();
            ui.show();
            JButton fixButton=ui.getFixButton();
            JButton stopButton=ui.getStopButton();
            JFrame j=ui.getFrame();
            fixButton.addActionListener((ActionEvent e) -> {
                //this is coolsome
                JOptionPane.showMessageDialog(null, "Threats Fixed!", "Links Action Manager", 0);
            });
            
            stopButton.addActionListener(((ActionEvent e) -> {
                //stopping the can hereeee
                j.dispose();
            }));
            
//            System.err.println(max+" byets in total");
        }
        @Override
        public void run(){
            scanFolder(folder);
            ui.dispose();
            
            
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
                       ui.setThreats(fl.getAbsolutePath());
                       if(fl.getName().equals(zb)){
                        //zerobyete agent or just a similar app, 
                        //quarantine this app or remove it
                          actionMan.handleZero(fl.getAbsolutePath());
                       }
                   }
                }
                
            }catch(InterruptedException e){
                ui.dispose();
            }
        }

}
