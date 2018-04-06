package controllers.User;

import domain.Article;
import domain.NewsPaper;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ArticleService;
import services.NewsPaperService;
import services.UserService;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController {
    // Services --------------------------------------------

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;


    // Constructor --------------------------------------------

    public ArticleUserController() {
        super();
    }

    // Creation ------------------------------------------------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView result;
        Article article= null ;

        article = this.articleService.create();
        result = this.createEditModelAndView(article);

        return result;
    }

    // Listing -------------------------------------------------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        User user;
        Collection<Article> articles=null;

        SimpleDateFormat formatterEs;
        SimpleDateFormat formatterEn;
        String momentEs;
        String momentEn;


        formatterEs = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        momentEs = formatterEs.format(new Date());
        formatterEn = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        momentEn = formatterEn.format(new Date());

        user = userService.findByPrincipal();
        articles=this.articleService.findArticleByUser(user);

        result = new ModelAndView("article/list");
        result.addObject("articles", articles);
        result.addObject("requestURI","article/user/list.do");
        result.addObject("momentEs", momentEs);
        result.addObject("momentEn", momentEn);

        return result;

    }

    //  Edition ----------------------------------------------------------------

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int articleId) {
        final ModelAndView result;
        Article article;
        article = this.articleService.findOneToEdit(articleId);
        Assert.notNull(article);
        result = this.createEditModelAndView(article);
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid Article article, final BindingResult binding) {
        ModelAndView result;
        if (binding.hasErrors())
            result = this.createEditModelAndView(article);
        else
            try {
                this.articleService.save(article);
                result = new ModelAndView("redirect:list.do");
            } catch (final Throwable oops) {
                result = this.createEditModelAndView(article, "article.commit.error");
            }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST,params = "delete")
    public ModelAndView edit(Article article){
        ModelAndView result;

        try{
            articleService.delete(article);
            result = new ModelAndView("redirect:list.do");
        }catch (Throwable oops){
            result = createEditModelAndView(article,"article.commit.error");
        }

        return result;
    }

    // Ancillary methods ------------------------------------------------------

    protected ModelAndView createEditModelAndView(final Article article) {
        ModelAndView result;

        result = this.createEditModelAndView(article, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final Article article, final String messageCode) {
        ModelAndView result;
        result = new ModelAndView("article/edit");
        result.addObject("article", article);
        result.addObject("message", messageCode);

        return result;
    }
}
