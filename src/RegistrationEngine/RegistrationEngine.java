
package RegistrationEngine;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class RegistrationEngine {
    public String name,number;
    public WebEngine webEngine;
    WebView webLoader;
    public OnlineRegistration on;
    public RegistrationEngine(String n,String n2,WebEngine e){
            on=new OnlineRegistration(name, name, webEngine);
    }
    public RegistrationEngine(){}
    public int getRegistrationState(){
    return new Registration().check_registered();
    }

    public class OnlineRegistration {
    public String name,number;
    public WebEngine webEngine;
    WebView webLoader;
    JLoader Japp=new JLoader();
    public OnlineRegistration(String n,String n2,WebEngine w){
    this.name=n;
    this.number=n2;
    this.webEngine=w;
    webLoader=new WebView();
    webLoader.getEngine().setJavaScriptEnabled(true);
    webLoader.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>(){

        @Override
        public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
            if(newValue.equals(Worker.State.FAILED)){
            webEngine.executeScript("Toast(\"There is no internet Connection\")");
            }
            
            else if (newValue.equals(Worker.State.SUCCEEDED)){
                
                JSObject win=(JSObject)webLoader.getEngine().getDocument();
                win.setMember("App", (JLoader)Japp);
                String rfo=webLoader.getEngine().getDocument().getElementById("result").getTagName().toLowerCase();
                if(rfo.equals("r_1")){
                new Registration().registerUser();
                webEngine.executeScript("Toast(\"<img src='img/registered.png'/><br>"
                        + "<b>Your Links Antivirus Has been Successfully registered!<br><br>Your Name : "+name+"<br>Phone Number : "+number+"</b><br><br>"
                                + "<i>Thank you for purchasing...</i>\",10,50)");
                
                //save metadata of the user
                LinksDataStructure lds=new LinksDataStructure();
                GSLib gs=new GSLib();
                Date d=new Date();
                
                gs.appendFile(lds.LINKS_USER_DATA, name.replace(" ", "_")+"#"+d.toString().replace(" ", "/")+"#"+"Family_edition#"+number);
                //This Pc is now registered tp $name
                
                }else if(rfo.equals("l_1")){
                webEngine.executeScript("Toast(\"<img src='img/aimode.png'/><br>"
                        + "<b>Unable to register this number, make sure that you have puchased"
                        + "through our official Links Antivirus Agents or contact us through :<br><br>Phone : +265 882 931 367<br>Email : kaluawongan@gmail.com</b><br><br>"
                                + "<i>If you have purchased recently, wait for 10 minutes</i>\",10,50)");
                }else{
                webEngine.executeScript("Toast(\"<img src='img/aimode.png'/><br>"
                        + "<b>This number was registered to another computer."
                        + "To swap accounts confirm that it is your on our website<br>"
                                + "<i>Number alraedy registered</i>\",10,50)");
                }
                System.out.println();
            }
        }
    });
    
    
    }
    
    public void registerOnline(){
        try {
            String s=new Registration().getSerialNumber();
            String url="http://linksantivirus.byethost33.com/links203/register.php?s="+s+"&n="+name+"&p="+number;
            webLoader.getEngine().load(url);
        } catch (Exception ex) {
           new GSLib().appendFile(new LinksDataStructure().SYSTEM_DIR+"/links antivirus log.txt", "Error Loading registration site : "
                    + ex.getMessage());
        } 
        
    }
    
    public class JLoader{
        public void testConnection(){
            System.err.println("testing the internet connectivity");
        }
    }
}
}
