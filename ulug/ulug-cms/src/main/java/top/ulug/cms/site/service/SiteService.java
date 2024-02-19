package top.ulug.cms.site.service;

import org.springframework.web.bind.annotation.RequestParam;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.dto.ChartDataSetDTO;
import top.ulug.cms.media.dto.ContentDTO;
import top.ulug.cms.media.dto.SubjectDTO;
import top.ulug.jpa.dto.PageDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2020-08-21.
 * 逝者如斯夫 不舍昼夜
 */
public interface SiteService {

    /**
     * 获取首页配置信息
     *
     * @param pageId   页面ID
     * @param pageMode 模式
     * @return info
     */
    WrapperDTO<Object> page(@RequestParam String pageId, String pageMode);

    /**
     * 获取主列表
     *
     * @param pageId   请求页面id
     * @param v        检索值
     * @param dl       时间范围
     * @param sid      主题id
     * @param pno      页码
     * @return PageDTO
     */
    WrapperDTO<PageDTO<Map>> focus(String pageId, String v, String dl, String sid, Integer pno);

    /**
     * 获取内容
     *
     * @param fid 内容id
     * @return info
     */
    WrapperDTO<ContentDTO> content(String fid);

    /**
     * 获取图表数据
     *
     * @param chartCode chart code
     * @return info
     */
    WrapperDTO<ChartDataSetDTO> chart(String chartCode);

    /**
     * 获取主题列表
     *
     * @param pageId 请求页面id
     * @param v      检索值
     * @param pno    页码
     * @return PageDTO
     */
    WrapperDTO<PageDTO<SubjectDTO>> subjectList(String pageId, String v, Integer pno);

    /**
     * 获取主题详细
     *
     * @param sid 主题ID
     * @return dto
     */
    WrapperDTO<SubjectDTO> subjectDetail(String sid);

    /**
     * 获取热门标签
     *
     * @param pageId 请求页面id
     * @param dl     时间范围
     * @return list
     */
    WrapperDTO<List<String>> tagList(String pageId, String dl);
}
