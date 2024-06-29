package top.ulug.core.deploy.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ulug.base.inf.ApiDocument;
import top.ulug.core.deploy.service.SseMessageService;

/**
 * @Author liu
 * @Date 2024/3/24 23:03 星期日
 */
@RestController
@RequestMapping("/sse")
public class SseMessageController {
    @Autowired
    SseMessageService sseMessageService;

    @GetMapping(value = "/connect/{token}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiDocument(note = "SSE连接", paramsExample = "", resultExample = "")
    public SseEmitter connect(@PathVariable("token") String token) {
        return sseMessageService.connect(token);
    }

}
