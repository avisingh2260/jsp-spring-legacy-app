package com.assetdesk.web;

import com.assetdesk.model.Asset;
import com.assetdesk.model.AssetStatus;
import com.assetdesk.service.AssetService;
import com.assetdesk.validator.AssetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetValidator assetValidator;

    @InitBinder("asset")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(assetValidator);
    }

    @RequestMapping(value = "/assets.do", method = RequestMethod.GET)
    public ModelAndView listAssets(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            HttpSession session) {

        List<Asset> assets = assetService.searchAssets(query, status);
        ModelAndView mav = new ModelAndView("assetList");
        mav.addObject("assets", assets);
        mav.addObject("selectedStatus", status);
        mav.addObject("statuses", AssetStatus.values());

        Object flash = session.getAttribute("flashMessage");
        if (flash != null) {
            mav.addObject("flashMessage", flash);
            session.removeAttribute("flashMessage");
        }
        return mav;
    }

    @RequestMapping(value = "/newAsset.do", method = RequestMethod.GET)
    public ModelAndView showCreateForm() {
        ModelAndView mav = new ModelAndView("assetForm");
        mav.addObject("asset", new Asset());
        return mav;
    }

    @RequestMapping(value = "/createAsset.do", method = RequestMethod.POST)
    public ModelAndView createAsset(
            @ModelAttribute("asset") Asset asset,
            BindingResult result,
            HttpSession session) {

        assetValidator.validate(asset, result);
        if (result.hasErrors()) {
            return new ModelAndView("assetForm");
        }
        assetService.createAsset(asset);
        session.setAttribute("flashMessage", "Added asset " + asset.getSerialNumber());
        return new ModelAndView("redirect:/assets.do");
    }

    @RequestMapping(value = "/checkoutForm.do", method = RequestMethod.GET)
    public ModelAndView showCheckoutForm(@RequestParam("id") Long id) {
        Asset asset = assetService.getById(id);
        ModelAndView mav = new ModelAndView("checkoutForm");
        mav.addObject("asset", asset);
        return mav;
    }

    @RequestMapping(value = "/checkout.do", method = RequestMethod.POST)
    public ModelAndView checkout(
            @RequestParam("id") Long id,
            @RequestParam("assigneeName") String assigneeName,
            @RequestParam("assigneeBadgeId") String assigneeBadgeId,
            HttpSession session) {
        try {
            assetService.checkoutAsset(id, assigneeName, assigneeBadgeId);
            session.setAttribute("flashMessage", "Asset checked out to " + assigneeName);
        } catch (RuntimeException ex) {
            ModelAndView mav = new ModelAndView("checkoutForm");
            mav.addObject("asset", assetService.getById(id));
            mav.addObject("errorMessage", ex.getMessage());
            return mav;
        }
        return new ModelAndView("redirect:/assets.do");
    }

    @RequestMapping(value = "/return.do", method = RequestMethod.POST)
    public ModelAndView returnAsset(@RequestParam("id") Long id, HttpSession session) {
        try {
            assetService.returnAsset(id);
            session.setAttribute("flashMessage", "Asset returned to inventory.");
        } catch (RuntimeException ex) {
            session.setAttribute("flashMessage", "Error: " + ex.getMessage());
        }
        return new ModelAndView("redirect:/assets.do");
    }
}
