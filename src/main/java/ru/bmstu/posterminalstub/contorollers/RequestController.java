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
import ru.bmstu.posterminalstub.model.ModelView;


@Controller
public class RequestController {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private static final String URL_UPDATER = "https://localhost:8085/";

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
    ) {
        HttpEntity<String> request = new HttpEntity<>(modelView.getRq(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL_UPDATER, request, String.class);
        modelView.setRs(response.getBody());
        redirectAttributes.addFlashAttribute("ModelView", modelView);
        return "redirect:/";
    }
}