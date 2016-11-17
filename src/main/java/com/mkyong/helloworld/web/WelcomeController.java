package com.mkyong.helloworld.web;

import com.mkyong.helloworld.service.HelloWorldService;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Map;


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
	@RequestMapping(value = "face", method = RequestMethod.POST)
	public ResponseEntity<MatVector>  faces(@RequestParam(value = "facesData[]") String[] facesData) throws IOException {
		System.out.println(facesData.length);
		String encodingPrefix = "base64,";
		String dataUrl = facesData[1];
		int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
		byte[] imageData = DatatypeConverter.parseBase64Binary(dataUrl.substring(contentStartIndex));
		System.out.println(imageData.length);

		Mat mat = new Mat();
		mat.data().put(imageData);


//        InputStream inputStream = new ByteArrayInputStream(imageData);
//        BufferedImage bufferedImage = ImageIO.read(inputStream);
//        ImageIO.write(bufferedImage, "png", new File("image.png"));
		return new ResponseEntity<MatVector>(HttpStatus.OK);
	}

}