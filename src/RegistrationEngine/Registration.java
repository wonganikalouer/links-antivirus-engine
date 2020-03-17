
package RegistrationEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Registration {
LinksDataStructure data=new LinksDataStructure();
GSLib gs=new GSLib();
    public int check_registered(){
        try {
            String serial=getSerialNumber();
            StringBuilder sb=new StringBuilder(serial);
            sb.reverse();
            
            String regCode=convert(sb);
            String userCode=gs.readFile(data.REGISTRATION_FILE);
            
            if(userCode.equals("")){
            //this is unregistered user
              return 0;  
            }else{
                if(userCode.equals(regCode)){
                    //user is registered
                    return 1;
                }else{
                    //this users files were pretransfered
                    return -1;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    static String getSerialNumber() throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder("wmic", "memorychip", 
            "get", "serialnumber");
    Process process = pb.start();
    process.waitFor();
    String serialNumber = "";
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
            process.getInputStream()))) {
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.length() < 1 || line.startsWith("SerialNumber")) {
                continue;
            }
            serialNumber = line;
            break;
        }
    }
    return serialNumber;
}
    
     public int getTrialDates(){
        String dates=gs.readFile(data.DATE_FILE);
        int dr=Integer.parseInt(dates);
        int dtr=data.TRIAL_PERIOD-dr;
        return dtr;
    }
    
    public void incrementDates(){
        String dates=gs.readFile(data.DATE_FILE);
        int dr=Integer.parseInt(dates)+1;
        new File(data.DATE_FILE).delete();
        gs.appendFile(data.DATE_FILE, dr+"");
    }
    
    public String convert(StringBuilder d){
        //encrypt the serial number here
        String d2b=d.toString().replace(" ", "").toLowerCase();
        String a[]=d2b.split("");
//        System.out.println("We have "+a.length+" characters in "+d2b);
        ArrayList<String> s=new ArrayList<String>();
        for(String z:a){
        //start serializing
            if(z.equals("a")){
                s.add("13");
            }else if(z.equals("b")){
            s.add("16");
            }else if(z.equals("c")){
            s.add("12");
            }
            else if(z.equals("d")){
            s.add("10");
            }
            else if(z.equals("e")){
            s.add("11");
            }
            else if(z.equals("f")){
            s.add("15");
            }
            else if(z.equals("g")){
            s.add("19");
            }
            else if(z.equals("h")){
            s.add("14");
            }
            else if(z.equals("i")){
            s.add("18");
            }
            else if(z.equals("j")){
            s.add("17");
            }
            else if(z.equals("k")){
            s.add("20");
            }
            else if(z.equals("l")){
            s.add("90");
            }
            else if(z.equals("m")){
            s.add("91");
            }
            else if(z.equals("n")){
            s.add("92");
            }
            else if(z.equals("o")){
            s.add("93");
            }
            else if(z.equals("p")){
            s.add("95");
            }
            else if(z.equals("q")){
            s.add("94");
            }
            else if(z.equals("r")){
            s.add("99");
            }
            else if(z.equals("s")){
            s.add("96");
            }
            else if(z.equals("t")){
            s.add("98");
            }
            else if(z.equals("u")){
            s.add("97");
            }
            else if(z.equals("v")){
            s.add("28");
            }
            else if(z.equals("w")){
            s.add("27");
            }
            else if(z.equals("x")){
            s.add("26");
            }
            else if(z.equals("y")){
            s.add("25");
            }
            else if(z.equals("z")){
            s.add("23");
            }
            else if(z.equals("0")){
            s.add("29");
            }
            else if(z.equals("1")){
            s.add("22");
            }
            else if(z.equals("2")){
            s.add("21");
            }
            else if(z.equals("3")){
            s.add("24");
            }
            else if(z.equals("4")){
            s.add("50");
            }
            else if(z.equals("5")){
            s.add("52");
            }
            else if(z.equals("6")){
            s.add("54");
            }
            else if(z.equals("7")){
            s.add("56");
            }
            else if(z.equals("8")){
            s.add("58");
            }
            else if(z.equals("9")){
            s.add("53");
            }
            else{
            s.add("0$");
            }
        }
 
        String store=s.toString().replace(",", "").replace("[","").replace("]", "").replace(" ", "");
        return store;    
    }

    public void registerUser() {
        try {
            String serial=getSerialNumber();
            StringBuilder sb=new StringBuilder(serial);
            sb.reverse();
            
            String regCode=convert(sb);
            new File(data.REGISTRATION_FILE).delete();
            gs.appendFile(data.REGISTRATION_FILE, regCode);
            //has registered user
        }catch(Exception e){}
    }
}

