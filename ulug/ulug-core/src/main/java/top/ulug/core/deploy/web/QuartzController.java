package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.domain.DeployQuartzTask;
import top.ulug.core.deploy.service.QuartzService;

import java.util.List;

/**
 * Created by liujf on 2020-10-28.
 * 逝者如斯夫 不舍昼夜
 */
@RestController
@RequestMapping("/quartz")
public class QuartzController {
    @Autowired
    QuartzService quartzService;

    @RequestMapping(value = "/dev/list", method = RequestMethod.POST)
    @ApiDocument(note = "任务列表", paramsExample = "", resultExample = "")
    public WrapperDTO<List<DeployQuartzTask>> list(@RequestParam(value = "taskName", required = false) String taskName) throws Exception {
        return quartzService.list(taskName);
    }

    @RequestMapping(value = "/dev/build", method = RequestMethod.POST)
    @ApiDocument(note = "任务创建", paramsExample = "", resultExample = "")
    public WrapperDTO<String> build(@ModelAttribute DeployQuartzTask task) throws Exception {
        return quartzService.build(task);
    }

    @RequestMapping(value = "/dev/remove", method = RequestMethod.POST)
    @ApiDocument(note = "任务移除", paramsExample = "", resultExample = "")
    public WrapperDTO<String> remove(@ModelAttribute DeployQuartzTask task) throws Exception {
        return quartzService.remove(task);
    }

    @RequestMapping(value = "/dev/control", method = RequestMethod.POST)
    @ApiDocument(note = "任务运行控制", paramsExample = "", resultExample = "")
    public WrapperDTO<String> run(@RequestParam(value = "taskId") Long taskId,
                                  @RequestParam(value = "tag") Integer tag) throws Exception {
        return quartzService.jobControl(taskId, tag);
    }
}
