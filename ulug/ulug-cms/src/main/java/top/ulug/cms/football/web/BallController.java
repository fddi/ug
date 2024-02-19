package top.ulug.cms.football.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.cms.football.domain.BallClub;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.domain.BallMatch;
import top.ulug.cms.football.domain.BallSystem;
import top.ulug.cms.football.dto.MatchDTO;
import top.ulug.cms.football.service.BallClubService;
import top.ulug.cms.football.service.BallCountryService;
import top.ulug.cms.football.service.BallMatchService;
import top.ulug.cms.football.service.BallSystemService;
import top.ulug.jpa.dto.PageDTO;

/**
 * Created by liujf on 2020/11/1.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/ball")
public class BallController {
    @Autowired
    private BallSystemService systemService;
    @Autowired
    private BallMatchService matchService;
    @Autowired
    private BallCountryService countryService;
    @Autowired
    private BallClubService clubService;

    @RequestMapping(value = "/system-list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛事列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<BallSystem>> page(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @ModelAttribute BallSystem system) {
        return systemService.findPage(pageSize, pageNo, system);
    }

    @RequestMapping(value = "/file/system-upload", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛事保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@ModelAttribute BallSystem system,
                                   @RequestParam(value = "image", required = false) MultipartFile image) {
        return systemService.upload(system, image);
    }

    @RequestMapping(value = "/system-del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛事删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@ModelAttribute BallSystem system) {
        return systemService.del(system);
    }


    @RequestMapping(value = "/match-page", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛程列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<MatchDTO>> matchPage(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @ModelAttribute BallMatch ballMatch) {
        return matchService.findPage(pageSize, pageNo, ballMatch);
    }

    @RequestMapping(value = "/match-save", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛程保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> matchSave(@ModelAttribute BallMatch match) throws Exception {
        return matchService.save(match);
    }

    @RequestMapping(value = "/match-del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛程删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> matchDel(@ModelAttribute BallMatch match) {
        return matchService.del(match);
    }

    @RequestMapping(value = "/country-tree", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "国家列表树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> countryTree() {
        return countryService.countryExtra();
    }

    @RequestMapping(value = "/system-tree", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "足球赛事树", paramsExample = "", resultExample = "")
    public WrapperDTO<TreeDTO> systemTree(@ModelAttribute BallMatch match) {
        return systemService.systemExtra();
    }


    @RequestMapping(value = "/country-page", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "国家信息列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<BallCountry>> pageCountry(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        @ModelAttribute BallCountry country) {
        return countryService.findPage(pageSize, pageNo, country);
    }

    @RequestMapping(value = "/file/country-upload", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "国家信息保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveCountry(@ModelAttribute BallCountry country,
                                          @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        return countryService.upload(country, image);
    }

    @RequestMapping(value = "/country-del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "国家信息删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> delCountry(@ModelAttribute BallCountry country) {
        return countryService.del(country);
    }


    @RequestMapping(value = "/club-page", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "俱乐部列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<BallClub>> pageClub(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @ModelAttribute BallClub club) {
        return clubService.findPage(pageSize, pageNo, club);
    }

    @RequestMapping(value = "/file/club-upload", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "俱乐部保存", paramsExample = "", resultExample = "")
    public WrapperDTO<String> saveClub(@ModelAttribute BallClub club,
                                       @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        return clubService.upload(club, image);
    }

    @RequestMapping(value = "/club-del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "俱乐部删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> delClub(@ModelAttribute BallClub club) {
        return clubService.del(club);
    }


}
