package course.controller;

import course.dao.SiteRepository;
import course.domain.Render;
import course.domain.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Артем Константинович on 06.10.2016.
 */
@Controller
@RequestMapping("/")
public class SiteController {

    @Autowired
    private SiteRepository siteRepository;


    @Autowired
    public SiteController(SiteRepository siteRepository){
        this.siteRepository = siteRepository;
    }

    @RequestMapping(value = "/site/info/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Site> ViewSites(HttpSession httpSession, @PathVariable("id") long id){
        List<Site> sites;
        sites = siteRepository.findByOwnerId(id);
        return sites;
    }

    @RequestMapping(value = "/site/source/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String ViewSiteSource(HttpSession httpSession, @PathVariable("id") long id){
        Site site;
        site = siteRepository.findById(id);
        return site.getSource();
    }

    @RequestMapping(value = "/delete/site/{id}", method = RequestMethod.GET)
    public String DeleteSite(HttpSession httpSession, @PathVariable("id") long id){
        if ((long)httpSession.getAttribute("id") == siteRepository.findById(id).getOwnerId()){
            siteRepository.delete(id);
        }
        return "redirect:/profile?id="+httpSession.getAttribute("id").toString();
    }

    @RequestMapping(value = "/site/sortedByAlph/{count}", method = RequestMethod.GET)
    public @ResponseBody
    List<Site> ViewMainSites(HttpSession httpSession, @PathVariable("count") int count){
        List<Site> sites;
        List<Site> page = new ArrayList<Site>();
        sites = siteRepository.findAllByOrderByName();
        for(int j = 0;j<count;j++){
            if (j >= sites.size()) {
                break;
            }
            else{
                page.add(sites.get(j));
            }
        }
        return page;
    }

    @RequestMapping(value = "/site/sortedByCreate/{count}", method = RequestMethod.GET)
    public @ResponseBody
    List<Site> ViewMainCreatedSites(HttpSession httpSession, @PathVariable("count") int count){
        List<Site> sites;
        List<Site> page = new ArrayList<Site>();
        sites = siteRepository.findAllByOrderByCreateDate();
        for(int i = 0;i<count;i++){
            if (i >= sites.size()) {
                break;
            }
            else{
                page.add(sites.get(i));
            }
        }
        return page;
    }



    @RequestMapping(value = "/showsite/{id}", method = RequestMethod.GET)
    public  @ResponseBody
    Render RenderSite(Model model, HttpSession httpSession, @PathVariable("id") long id){
        String source = siteRepository.findById(id).getSource();
        while (source.contains("  ")) {
            source = source.replace( "  ", " " );

        }
        source = source.replace("\n","");
        source = source.replace( "{ \"A\": ["," <div class=\"container\"> " );
        source = source.replace("{ \"type\": \"container\", \"columns\": [ [",
                "<div class=\"row\"> \n" +
                        "<div class=\"col-md-6\">\n");
        source = source.replace("], [",
                "</div>\n" +
                        "<div class=\"col-md-6\">\n");
        source = source.replace("{ \"type\": \"item\", \"code\": \"","");
        source = source.replace("\" },","");
        source = source.replace("\" }","");
        source = source.replace( "},","");
        source = source.replace("] ","</div>" );
        source = source.replace("} ","");
        source = source.replace("]}","</div>");
        source = source.replace("\\n","");
        source = source.replace("\\","");
        return new Render(source);
    }


}
