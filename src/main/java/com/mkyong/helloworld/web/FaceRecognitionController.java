package com.mkyong.helloworld.web;

import org.bytedeco.javacpp.opencv_core.MatVector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Controller
public class FaceRecognitionController
{
    private final FaceRecognizerWrapper recognizerWrapper = new FaceRecognizerWrapper("LearnedData.yml");

    @RequestMapping(value= "/train", method = RequestMethod.GET)
    public void train(Mat image, Mat labels){
        FaceDetectorWrapper detector = new FaceDetectorWrapper();
        MatVector faces = detector.detect(image);
        recognizerWrapper.train(faces, labels);
        recognizerWrapper.close();
    }

    @RequestMapping(value = "/predict", method = RequestMethod.GET)
    public int[] predict(MatVector faces) {
        int[] predicted = recognizerWrapper.predict(faces);
        return predicted;
    }
}
