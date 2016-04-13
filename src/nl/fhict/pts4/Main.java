package nl.fhict.pts4;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void printFile(File fn) {

        Photo photo = new Photo(fn);

        try {
            System.out.println(fn.getPath()+photo.getTitle());
        } catch (IOException e) {
            System.err.println("Can't read "+fn.getPath());
            e.printStackTrace();
        }

    }

    public static String DVD_PATH = "/mnt/iso";
    public static String STORAGE_PATH = "/tmp/dvds";

    public static List<Photo> photos = new ArrayList<>();

    public static void main(String[] args) {

        // Register DVD with the website

        File[] files = new File(DVD_PATH).listFiles();

        // Loop through all files
        for (File rootfile : files) {

            // If a directory is in the root folder, enter it
            if (rootfile.isDirectory()) {

                // Create a login code
                System.out.print("d ");

                for (File folderfile : rootfile.listFiles())
                {
                    System.out.print("- ");
                    printFile(folderfile);
                }
            }
            else {
                printFile(rootfile);
            }
        }

    }
}
