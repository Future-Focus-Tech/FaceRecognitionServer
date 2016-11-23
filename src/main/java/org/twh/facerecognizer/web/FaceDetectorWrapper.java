package org.twh.facerecognizer.web;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

public class FaceDetectorWrapper {
    //haarcascade.xml from opencv 2.4.0
    public static final String XML_FILE = "haarcascade_frontalface_default.xml";
    private static final CvHaarClassifierCascade classifier;

    static {
        // Preload the opencv_objdetect module to work around a known bug.
        Loader.load(opencv_objdetect.class);
        // We can "cast" Pointer objects by instantiating a new object of the desired class.
        classifier = new CvHaarClassifierCascade(cvLoad(XML_FILE));
    }

    FaceDetectorWrapper() {
        if (classifier.isNull()) {
            System.err.println("Error loading classifier file \"" + XML_FILE + "\".");
            System.exit(1);
        }
    }

    MatVector detect(Mat matImage) {
        IplImage image = new IplImage(matImage);
        CvMemStorage storage = CvMemStorage.create();
        CvSeq sign = cvHaarDetectObjects(
                image,
                classifier,
                storage,
                1.1,
                3,
                CV_HAAR_DO_CANNY_PRUNING);

        cvClearMemStorage(storage);

        int total_Faces = sign.total();
//        System.out.println(total_Faces); // To print total no. of faces
// found in the image

        MatVector faces = new MatVector(total_Faces);

        for (int i = 0; i < total_Faces; i++) {
            CvRect r = new CvRect(cvGetSeqElem(sign, i));
            cvSetImageROI(image, r);
//            cvSaveImage(i + "grayImage.png", image);
            Mat newMatImage = new Mat(image);
//            imwrite(i+"matgary.png",newMatImage);
            faces.put(i, newMatImage);
        }
//        System.out.println(faces.size());
        return faces;
    }

}
