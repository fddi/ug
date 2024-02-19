package top.ulug.cms.media.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaNetFile;
import top.ulug.cms.media.domain.MediaNetFileShare;
import top.ulug.cms.media.dto.NetDiskDTO;
import top.ulug.cms.media.dto.NetFileDTO;
import top.ulug.cms.media.service.NetDiskService;
import top.ulug.cms.media.service.NetFileService;
import top.ulug.cms.media.service.NetFileShareService;
import top.ulug.jpa.dto.PageDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/net-disk")
public class NetDiskController {
    @Autowired
    private NetDiskService diskService;
    @Autowired
    private NetFileService fileService;
    @Autowired
    private NetFileShareService shareService;

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "云盘信息", paramsExample = "", resultExample = "")
    public WrapperDTO<NetDiskDTO> info(@RequestParam(value = "subjectId") Long subjectId) throws Exception {
        return diskService.info(subjectId);
    }


    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "云盘文件上传", paramsExample = "", resultExample = "")
    public WrapperDTO<String> save(@RequestParam(value = "diskId") Long diskId,
                                   @RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upload(diskId, file);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "云盘文件删除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> del(@RequestParam(value = "fileKeys") String fileKeys) throws Exception {
        List<MediaNetFile> list = new ArrayList<>();
        if (!StringUtils.isEmpty(fileKeys)) {
            String[] keys = fileKeys.split(",");
            for (int i = 0; i < keys.length; i++) {
                MediaNetFile netFile = new MediaNetFile();
                netFile.setFileId(Long.parseLong(keys[i]));
                list.add(netFile);
            }
        }
        MediaNetFile[] data = new MediaNetFile[list.size()];
        return fileService.del(list.toArray(data));
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "云盘文件列表", paramsExample = "", resultExample = "")
    public WrapperDTO<PageDTO<NetFileDTO>> list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @ModelAttribute MediaNetFile netFile) throws Exception {
        return fileService.findPage(pageSize, pageNo, netFile);
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST)
    @ResponseBody
    @ApiDocument(note = "云盘文件共享", paramsExample = "", resultExample = "")
    public String shareControl(@RequestParam(value = "fileId") Long fileId,
                               @ModelAttribute MediaNetFileShare fileShare) throws Exception {
        return shareService.shareControl(fileId, fileShare).toString();
    }

    @RequestMapping(value = "/share-list")
    @ApiDocument(note = "云盘文件共享链接列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<MediaNetFileShare>> shareList(@RequestParam(value = "fileId") Long fileId) throws Exception {
        return shareService.shareList(fileId);
    }

}
