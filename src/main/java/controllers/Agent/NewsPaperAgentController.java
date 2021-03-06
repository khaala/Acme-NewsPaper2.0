package controllers.Agent;

import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.AdvertisementService;
import services.AgentService;
import services.NewsPaperService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
@RequestMapping("/newsPaper/agent")
public class NewsPaperAgentController extends AbstractController {

    //Services -------------------------------------------------------

    @Autowired
    private NewsPaperService newsPaperService;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private ActorService actorService;

    public NewsPaperAgentController() { super();}

    // Display ----------------------------------------------------------------

    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam int newsPaperId, HttpServletRequest request) {
        ModelAndView result;
        NewsPaper newsPaper;
        newsPaper = this.newsPaperService.findOne(newsPaperId);

        HttpSession session = request.getSession();

        result = new ModelAndView("newsPaper/display");
        result.addObject("newsPaper", newsPaper);
        result.addObject("cancelUriSession", request.getSession().getAttribute("cancelUriSession"));

        session.setAttribute("cancelUriSession", request.getRequestURI()+ "?newsPaperId=" + newsPaperId);


        return result;
    }

    // Listing -------------------------------------------------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView result;
        Agent agent;

        result = new ModelAndView("newsPaper/listNewsPaperAdvertisement");
        agent = agentService.findByPrincipal();

        HttpSession session = request.getSession();
        session.setAttribute("cancelUriSession", request.getRequestURI());

        result.addObject("newsPapersWithAdds", newsPaperService.findNewsPaperPlacedAdvertisement(agent.getId()));
        result.addObject("newsPapersWithNoAdds",newsPaperService.newsPapersWithNoAdds());
        result.addObject("requestURI","/newsPaper/agent/list.do");

        return result;
    }

}
