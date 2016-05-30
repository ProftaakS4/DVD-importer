package nl.fhict.pts4;


import nl.fhict.pts4.Database.Database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.nio.file.StandardCopyOption.*;

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

    private static void copyFolder(File source, File target) throws IOException
    {
        System.out.println("Copying "+ source.getPath() + " -> " + target.getPath());
        if (source.isDirectory())
        {
            if (!target.exists())
            {
                target.mkdir();
            }

            String files[] = source.list();

            for (String file : files)
            {
                File sourceFile = new File(source, file);
                File targetFile = new File(target, file);

                copyFolder(sourceFile, targetFile);
            }
        }
        else
        {
            Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
        }
    }

    public static String DVD_PATH = "/mnt/iso/";
    // public static String STORAGE_PATH = "/tmp/dvds/";


    // public static String STORAGE_PATH = "/tmp/sshfs_fileserver/dvd_storage";
    public static String STORAGE_PATH = "/mnt/sshfs_fileserver/dvd_storage/";
    public static List<Photo> photos = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        File storage = new File(STORAGE_PATH);
        Long freeGiB = storage.getUsableSpace()/1024/1024/1024;

        System.out.println("Storage: " + freeGiB + " GiB free");

        // Register DVD with the website
        System.out.println("Connecting to the database...");
        Database db = new Database();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Photographer ID: ");
        Integer photographerId = Integer.parseInt(scanner.next());

        // TODO: check if photographer exists
        Integer dvdid = db.newDvd(photographerId);

        File storagefolder = new File(STORAGE_PATH + dvdid + "/");
        // storagefolder.mkdirs();
        // storagefolder.setWritable(true)

        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);

        System.out.println(storagefolder);
        System.out.println(storagefolder.canWrite());

        try {
            File source = new File(DVD_PATH);
            File target = storagefolder;

            copyFolder(source, target);

        } catch (IOException e) {
            System.err.println("Could not copy files");
            e.printStackTrace();
        }

        File[] files = storagefolder.listFiles();

        // Loop through all files
        for (File rootfile : files) {

            int publicDirectory = db.newDirectory(dvdid, rootfile.getAbsolutePath(), false);

            // If a directory is in the root folder, enter it
            if (rootfile.isDirectory()) {

                // Create a login code

                int directory = db.newDirectory(dvdid, rootfile.getAbsolutePath(), true);

                String logincode = db.newLoginCode(directory, photographerId);

                System.out.println("Login code for " + rootfile.getName() + ": " + logincode);

                for (File folderfile : rootfile.listFiles())
                {
                    try {
                        Photo photo = new Photo(folderfile);
                        db.newPhoto(directory, photographerId, photo);
                        photo.getThumbnail();
                    } catch (IOException e) {
                        System.err.println("Could not read photo file " + folderfile.getPath());
                        throw e;
                    }
                }
            } else {
                try {
                    Photo photo = new Photo(rootfile);
                    db.newPhoto(publicDirectory, photographerId, photo);

                } catch (IOException e) {
                    System.err.println("Could not read photo file " + rootfile.getPath());
                    throw e;
                }
            }
        }
    }
}
