package de.tub.da.callcenter;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by philipp on 17.01.17.
 */
public class CallCenter {
    public static void main(String[] args) throws IOException, InterruptedException {

        String tempdir = System.getProperty("user.home");

        String fileDir = tempdir+"/callcenter/out.txt";
        System.out.println(fileDir);
        File f = new File(fileDir);

        f.getParentFile().mkdirs();
        f.createNewFile();


        System.out.print("Callcenter ins running");
        while(!Thread.interrupted()) {
            System.out.println("Callcenter ins writing");
            FileWriter fw = new FileWriter(f);
            fw.write(generateOrder());
            fw.write("\n");
            fw.flush();
            fw.close();
            Thread.sleep(10000);
        }


    }

    public static String generateOrder(){
        Random random = new Random(555);
        //CustomerID, Fullname, Number of surfboards, Number of diving suits
        return "<" + random.nextInt() + ","+ "Klaus Müller " + "," + random.nextInt() + "," + random.nextInt()+">";

    }


}
