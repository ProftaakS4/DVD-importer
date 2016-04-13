package nl.fhict.pts4;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.*;
import com.drew.metadata.iptc.IptcDirectory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by before on 3/30/16.
 */
public class Photo {
    private File file;
    private File thumbnail;

    public static final int THUMBNAIL_WIDTH = 300;
    public static final int THUMBNAIL_HEIGHT = 300;

    Photo(File file) {
        this.file = file;
    }

    public String getTitle() throws IOException {
        String title = null;
        try {
            Metadata metadata = JpegMetadataReader.readMetadata(file);
            Directory iptc = metadata.getFirstDirectoryOfType(IptcDirectory.class);

            /*
             * 632 = Caption/abstract
             * 617 = Headline
             * 517 = Object name
             */
            title = iptc.getString(617);
            if (title == null) { title = iptc.getString(517);}
            if (title == null) { title = iptc.getString(632);}
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        }

        return title;
    }

    private String getPath(){
        return file.getPath();
    }

    private String getResolution() throws IOException {
        BufferedImage bimg = ImageIO.read(file);
        int width          = bimg.getWidth();
        int height         = bimg.getHeight();

        return width + "x" + height;
    }

    private void createThumbnail() throws IOException {
        BufferedImage bimg = ImageIO.read(file);

        BufferedImage thumb;
        thumb = (BufferedImage) bimg.getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH);

        String folder = file.getParent();
        String[] filename = file.getName().split(".");

        this.thumbnail = new File(folder + "/thumb_"+filename[0]+".png");
        ImageIO.write(thumb, "png",this.thumbnail);
    }

    public File getThumbnail() throws IOException {
        if (thumbnail == null)
            createThumbnail();
        return thumbnail;
    }
}
