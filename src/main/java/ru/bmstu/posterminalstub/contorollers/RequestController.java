package ru.bmstu.posterminalstub.contorollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.bmstu.posterminalstub.model.ModelView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.io.Writer;


@Controller
public class RequestController {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private static final String URL_UPDATER = "https://localhost:8085/";
    private static final String XML_INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";

    @Autowired
    public RequestController(HttpHeaders headers, RestTemplate restTemplate) {
        this.headers = headers;
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/")
    public String index(Model model) {
        if (!model.containsAttribute("ModelView")) {
            model.addAttribute("ModelView", new ModelView());
        }
        return "index";
    }

    @PostMapping(value = "/createRequest")
    public String createRequest(ModelView modelView, RedirectAttributes redirectAttributes
    ) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        HttpEntity<String> request = new HttpEntity<>(modelView.getRq(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL_UPDATER, request, String.class);
        modelView.setRs(formatXml(response.getBody()));
        redirectAttributes.addFlashAttribute("ModelView", modelView);
        return "redirect:/";
    }

    private String formatXml(String str) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.parse(new StringBufferInputStream(str));

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        tf.setOutputProperty(XML_INDENT_AMOUNT, "4");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        return out.toString();
    }
}