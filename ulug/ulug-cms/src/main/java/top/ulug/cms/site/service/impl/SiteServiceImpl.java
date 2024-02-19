package top.ulug.cms.site.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.util.ListUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaChart;
import top.ulug.cms.media.dto.*;
import top.ulug.cms.media.repository.*;
import top.ulug.cms.site.service.SiteService;
import top.ulug.core.api.service.OvService;
import top.ulug.jpa.dto.PageDTO;
import top.ulug.jpa.tool.HbNative;
import ug.template.engine.core.launcher.CombExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2020-08-21.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    OvService ovService;
    @Autowired
    CombExecutor combExecutor;
    @Autowired
    HbNative hbNative;
    @Autowired
    MediaChartRepository chartRepository;
    @Autowired
    MediaChartDataRepository dataRepository;
    @Autowired
    MediaSubjectRepository subjectRepository;
    @Autowired
    MediaContentRepository contentRepository;

    @Override
    public WrapperDTO<Object> page(String pageId, String pageMode) {
        if (StringUtils.isEmpty(pageId) || StringUtils.isEmpty(pageMode)) {
            return WrapperDTO.npe("pageId");
        }
        String oCode = pageId + "_" + pageMode;
        String pageInfo = ovService.getPublicOv("Y", oCode).getResultData();
        return WrapperDTO.success(JSON.parse(pageInfo));
    }

    @Override
    public WrapperDTO<PageDTO<Map>> focus(String pageId, String v, String dl, String sid, Integer pno) {
        if (StringUtils.isEmpty(pageId)) {
            return WrapperDTO.npe("pageId");
        }
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("v", v);
            params.put("dl", dl);
            params.put("sid", sid);
            params.put("pageId", pageId);
            String sql = combExecutor.getComb("site.focusList", params);
            pno = pno == null ? 0 : pno;
            PageDTO<Map> page = hbNative.queryPageList(sql, params, pno, 10, Map.class);
            return WrapperDTO.success(page);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapperDTO.fail(e.getMessage());
        }
    }

    @Override
    public WrapperDTO<ContentDTO> content(String fid) {
        if (StringUtils.isEmpty(fid)) {
            return WrapperDTO.npe("fid");
        }
        ContentDTO content = contentRepository.findPublic(Long.valueOf(fid));
        if (content == null) {
            return WrapperDTO.npe("content");
        }
        return WrapperDTO.success(content);
    }

    @Override
    public WrapperDTO<ChartDataSetDTO> chart(String chartCode) {
        if (StringUtils.isEmpty(chartCode)) {
            return WrapperDTO.npe("chartCode");
        }
        MediaChart chart = chartRepository.findByChartCode(chartCode);
        if (chart == null) {
            return WrapperDTO.npe("chart");
        }
        List<ChartDataDTO> list = dataRepository.findByChartCode(chartCode);
        ChartDataSetDTO setDTO = new ChartDataSetDTO();
        setDTO.setChart(JSON.parseObject(JSON.toJSONString(chart), ChartDTO.class));
        setDTO.setData(list);
        return WrapperDTO.success(setDTO);
    }

    @Override
    public WrapperDTO<PageDTO<SubjectDTO>> subjectList(String pageId, String v, Integer pno) {
        if (StringUtils.isEmpty(pageId)) {
            return WrapperDTO.npe("pageId");
        }
        v = (v == null) ? "" : v;
        pno = pno == null ? 0 : pno;
        int pageSize = "page_subjects".equals(pageId) ? 9 : 200;
        Pageable pageable = PageRequest.of(pno, pageSize);
        Page<SubjectDTO> page = subjectRepository.pagePublic(v, pageable);
        List<SubjectDTO> list = new ArrayList<>();
        List<SubjectDTO> l = page.getContent();
        if (!"page_subjects".equals(pageId)) {
            //page_subjects: 全部分页查询，其他：推荐页按搜索关键词推荐6条
            if (l.size() < 6) {
                Page<SubjectDTO> p = subjectRepository.pagePublic("", pageable);
                list.addAll(ListUtils.createRandomList(p.getContent(), 6));
            } else {
                list.addAll(ListUtils.createRandomList(l, 6));
            }
        }
        PageDTO dto;
        if ("page_subjects".equals(pageId)) {
            dto = new PageDTO<SubjectDTO>().convert(page);
        } else {
            dto = new PageDTO<SubjectDTO>();
            dto.setContent(list);
        }
        return WrapperDTO.success(dto);
    }

    @Override
    public WrapperDTO<SubjectDTO> subjectDetail(String sid) {
        if (StringUtils.isEmpty(sid)) {
            return WrapperDTO.npe("sid");
        }
        SubjectDTO subjectDTO = subjectRepository.findPublic(sid);
        if (subjectDTO == null) {
            return WrapperDTO.npe("subject");
        }
        return WrapperDTO.success(subjectDTO);
    }

    @Override
    public WrapperDTO<List<String>> tagList(String pageId, String dl) {
        if (StringUtils.isEmpty(pageId)) {
            return WrapperDTO.npe("pageId");
        }
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("dl", dl);
            params.put("pageId", pageId);
            String sql = combExecutor.getComb("site.tagList", params);
            PageDTO<Map> page = hbNative.queryPageList(sql, params, 0, 500, Map.class);
            List<Map> list = page.getContent();
            String tags = "";
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = list.get(i);
                tags += item.get("tags") + ",";
            }
            tags = tags.substring(0, tags.length() - 1);
            String[] array = tags.split(",");
            List<String> r = new ArrayList();
            for (int i = 0; i < Math.min(array.length, 10); i++) {
                if (!r.contains(array[i])) {
                    r.add(array[i]);
                }
            }
            return WrapperDTO.success(r);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapperDTO.fail(e.getMessage());
        }
    }
}
