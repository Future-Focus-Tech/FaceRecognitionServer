package com.mkyong.helloworld.web;

import com.mkyong.helloworld.service.HelloWorldService;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.springframework.http.ResponseEntity.ok;


@Controller
public class WelcomeController {

    private final HelloWorldService helloWorldService;

    @Autowired
    public WelcomeController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {

        model.put("title", helloWorldService.getTitle(""));
        model.put("msg", helloWorldService.getDesc());

        return "index";
    }

    @RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
    public ModelAndView hello(@PathVariable("name") String name) {

        ModelAndView model = new ModelAndView();
        model.setViewName("index");

        model.addObject("title", helloWorldService.getTitle(name));
        model.addObject("msg", helloWorldService.getDesc());

        return model;

    }

    @RequestMapping(value = "/face", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> faces(@RequestParam(value = "facesData[]") String[] facesData) throws IOException {
        int numberOfFaces = facesData.length;
        MatVector faces = new MatVector(numberOfFaces);

        for (int faceIndex = 0; faceIndex < numberOfFaces; faceIndex++) {
            Mat matImage = createFaceMatrix(facesData[faceIndex]);
			imwrite("image" +faceIndex+".png", matImage);
            faces.put(faceIndex, matImage);
        }

        return ok("thank you");
    }

    private Mat createFaceMatrix(String imageDataUrl) throws IOException {
        String encodingPrefix = "base64,";
        int contentStartIndex = imageDataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = DatatypeConverter.parseBase64Binary(imageDataUrl.substring(contentStartIndex));
        InputStream inputStream = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage,"png", new File("image.png"));
        return new Mat(imread("image.png"));
//        OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
//        return cv.convertToMat(new Java2DFrameConverter().convert(bufferedImage));
    }

}