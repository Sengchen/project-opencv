import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("begin: ");

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Scanner scanner = new Scanner(System.in);
        String folder = scanner.next();

        File file = new File(folder);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File img : files) {
                    if (isImage(img)) {
                        try {
                            getFace(img);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                if (isImage(file)) {
                    try {
                        getFace(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static boolean getFace(File file) throws Exception {
        Mat mat = Imgcodecs.imread(file.getAbsolutePath());
        CascadeClassifier cascadeClassifier = new CascadeClassifier("C:\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml");

        MatOfRect matOfRect = new MatOfRect();
        cascadeClassifier.detectMultiScale(mat, matOfRect);
        // Creating a rectangular box showing faces detected
        for (Rect rect : matOfRect.toArray()) {
            Imgproc.rectangle(mat, new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        // Saving the output image
        Imgcodecs.imwrite(
                "C:\\face"
                        + File.separator
                        // file.getName().substring(0, file.getName().lastIndexOf("."))
                        + new Date().getTime()
                        + "_face"
                        + file.getName().substring(file.getName().lastIndexOf("."),
                        file.getName().length()), mat);
        return true;
    }

    private static boolean isImage(File file) {
        try {
            Image image = ImageIO.read(file);
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }
}
