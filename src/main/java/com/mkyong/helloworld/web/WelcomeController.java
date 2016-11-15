package com.mkyong.helloworld.web;

import com.mkyong.helloworld.service.HelloWorldService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.io.File;


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
	public void faces(@RequestParam(value = "facesData[]") String[] facesData) throws IOException {
		System.out.println(facesData.length);
		String encodingPrefix = "base64,";
		String dataUrl = facesData[1];
		int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
		byte[] imageData = Base64.decode(dataUrl.substring(contentStartIndex));
		System.out.println(imageData.length);
//		Mat matImageData = imdecode(imageData);


        InputStream inputStream = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        ImageIO.write(bufferedImage, "png", new File("raju.png"));
//		return new ResponseEntity<MatVector>(HttpStatus.OK);
	}

}