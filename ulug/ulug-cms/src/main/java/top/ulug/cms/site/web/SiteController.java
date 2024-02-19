package top.ulug.cms.site.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.cms.media.dto.ChartDataSetDTO;
import top.ulug.cms.media.dto.ContentDTO;
import top.ulug.cms.media.dto.SubjectDTO;
import top.ulug.cms.media.service.MediaService;
import top.ulug.cms.media.service.NetFileShareService;
import top.ulug.cms.site.service.SiteService;
import top.ulug.jpa.dto.PageDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2020-08-21.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping(value = "/ug")
public class SiteController {
    @Autowired
    NetFileShareService shareService;
    @Autowired
    SiteService siteService;
    @Autowired
    MediaService mediaService;

    @RequestMapping(value = "/file/a/{key}")
    @ApiDocument(note = "云盘文件下载", paramsExample = "", resultExample = "")
    public ResponseEntity<byte[]> getFile(@PathVariable("key") String key) throws Exception {
        return shareService.dl(key, null);
    }

    @RequestMapping(value = "/file/c")
    @ApiDocument(note = "云盘文件下载", paramsExample = "", resultExample = "")
    public ResponseEntity<byte[]> getFileByCode(@RequestParam(value = "key") String key,
                                                @RequestParam(value = "code") String code) throws Exception {
        return shareService.dl(key, code);
    }

    @RequestMapping(value = "/pc", method = RequestMethod.POST)
    @ApiDocument(note = "站点首页配置", paramsExample = "", resultExample = "")
    public WrapperDTO<Object> page(@RequestParam String pageId, @RequestParam String pageMode) throws Exception {
        return siteService.page(pageId, pageMode);
    }

    @RequestMapping(value = "/focus", method = RequestMethod.POST)
    @ApiDocument(note = "站点聚焦", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<Map>> focus(@RequestParam String pageId,
                                          @RequestParam(required = false) String v,
                                          @RequestParam(required = false) String dl,
                                          @RequestParam(required = false) String sid,
                                          @RequestParam(required = false) Integer pno) throws Exception {
        return siteService.focus(pageId, v, dl, sid, pno);
    }

    @RequestMapping(value = "/fsc", method = RequestMethod.POST)
    @ApiDocument(note = "站点内容", paramsExample = "", resultExample = "")
    public WrapperDTO<ContentDTO> content(@RequestParam String fid) throws Exception {
        return siteService.content(fid);
    }

    @RequestMapping(value = "/sts", method = RequestMethod.POST)
    @ApiDocument(note = "站点主题列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<SubjectDTO>> subjectList(@RequestParam String pageId,
                                                       @RequestParam(required = false) String v,
                                                       @RequestParam(required = false) Integer pno) {
        return siteService.subjectList(pageId, v, pno);
    }

    @RequestMapping(value = "/stc", method = RequestMethod.POST)
    @ApiDocument(note = "站点主题详细", paramsExample = "", resultExample = "")
    public WrapperDTO<SubjectDTO> subjectDetail(@RequestParam String sid) {
        return siteService.subjectDetail(sid);
    }

    @RequestMapping(value = "/tls", method = RequestMethod.POST)
    @ApiDocument(note = "站点热点集合", paramsExample = "", resultExample = "")
    public WrapperDTO<List<String>> tagList(@RequestParam String pageId,
                                            @RequestParam(required = false) String dl) {
        return siteService.tagList(pageId, dl);
    }

    @RequestMapping(value = "/chart", method = RequestMethod.POST)
    @ApiDocument(note = "站点图表", paramsExample = "", resultExample = "")
    public WrapperDTO<ChartDataSetDTO> chart(@RequestParam String chartCode) {
        return siteService.chart(chartCode);
    }

    @RequestMapping(value = "/file/img/{imageKey}", method = RequestMethod.GET)
    @ApiDocument(note = "站点图片", paramsExample = "", resultExample = "")
    public ResponseEntity<byte[]> readImage(
            @PathVariable String imageKey) throws Exception {
        return mediaService.readImage(imageKey);
    }
}
