/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RegistrationEngine;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author acer
 */
public class GSLib {
    public void appendFile(String location, String data_to_write){
    try {
			FileWriter writer = new FileWriter(location, true);
                        writer.flush();
                        writer.write(data_to_write);
			writer.close();
		} catch (Exception e) {

		}
    
    }
    
    public String readFile(String fileLocation){
    ///This is a very impotant function
        FileReader reader = null;
        try {
            StringBuilder b;
             reader = new FileReader(fileLocation); 
                int character;
                b = new StringBuilder();
                char chha = 0;
                String finalString="";
                while ((character = reader.read()) != -1) {
                    b.append((char)character);
                    
                }
//                        finalString=(String)chha;
//                        System.out.println(b.toString());
            
                        return b.toString();

		} catch (Exception e) {
                        return "";
            	}finally{
            try {
                reader.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
    
    public boolean executeBat(String batFileLocation){
        try {
            String[] command = {"cmd.exe", "/C", "Start", batFileLocation};
            Process p =  Runtime.getRuntime().exec(command);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    public boolean forceKill(String fileLocation){
    try {
        
        fileLocation=fileLocation.replace("/", "\\");
            String command = "DEL /F "+fileLocation+" \n pause";
            Process process = Runtime.getRuntime().exec(System.getenv("SystemDrive")+"/Windows/System32/cmd.exe "+command);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
        public boolean executeExe(String location){
      Process process;
                try {
                    process = Runtime.getRuntime().exec(location);
                    
                } catch (IOException ex) {
                    return false;
                }
                return true;
    }
    
}
