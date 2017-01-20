package de.tub.da.callcenter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is a simulator for a simple call center.
 * It writes a file to ~/callcenter/output.
 */
public class CallCenter {


    private final String[] userList = {"Klaus Müller", "Peter Fischer", "Hans Schmitt", "Bastian Schulze"};

    CallCenter() throws IOException, InterruptedException {

        String tempdir = System.getProperty("user.home");
        String fileDir = tempdir+"/callcenter/out.txt";
        System.out.println(fileDir);
        File f = new File(fileDir);

        f.getParentFile().mkdirs();
        f.createNewFile();

        System.out.println("------The Callcenter is running-------");
        while(!Thread.interrupted()) {

            FileWriter fw = new FileWriter(f);
            String newOrder = generateOrder();
            System.out.println("Callcenter is writing: "+ newOrder);
            fw.write(newOrder);
            fw.write("\n");
            fw.flush();
            fw.close();
            Thread.sleep(10000);
        }
    }

    private String generateOrder(){
        ThreadLocalRandom random = ThreadLocalRandom.current();

       // int userID = random.nextInt(0, 3);
       int userID = 1;
        String userName = this.userList[userID];

        int surfboards = random.nextInt(0,5);
        int suits = random.nextInt(0,5);

        //CustomerID, Fullname, Number of surfboards, Number of diving suits
        return "<" + userID + ","+ userName + "," + surfboards + "," + suits+">";

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        new CallCenter();
    }


}
