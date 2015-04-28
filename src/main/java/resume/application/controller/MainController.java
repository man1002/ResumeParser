package resume.application.controller;

import org.springframework.web.servlet.ModelAndView;
import resume.application.model.Resume;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import resume.application.repository.ResumeRepository;
import resume.parser.Parser;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private Parser parser;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping("/startParser")
    @ResponseBody
    public void create() {
        if(!parser.isWorking()) {
            parser.startParser();
        }
    }

    @RequestMapping("/isWorking")
    @ResponseBody
    public boolean isWorking() {
        return parser.isWorking();
    }

    @RequestMapping("/search")
    @ResponseBody
    public List<Resume> search(String keywords) {
        return resumeRepository.search(keywords);
    }

}
