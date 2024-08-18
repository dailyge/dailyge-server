package project.dailyge.app.core.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api-docs")
public class ApiDocsApi {

    @GetMapping
    public String getApiDocsPage() {
        return "/docs/index.html";
    }
}
