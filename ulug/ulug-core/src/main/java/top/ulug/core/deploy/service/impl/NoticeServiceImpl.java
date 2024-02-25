package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployNotice;
import top.ulug.core.deploy.repository.DeployNoticeRepository;
import top.ulug.core.deploy.service.NoticeService;
import top.ulug.base.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2022/7/27.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    DeployNoticeRepository noticeRepository;

    @Override
    public WrapperDTO<String> save(DeployNotice... e) throws Exception {
        List<DeployNotice> list = Arrays.asList(e);
        noticeRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(DeployNotice... e) {
        List<DeployNotice> list = Arrays.asList(e);
        noticeRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<DeployNotice> findOne(Long id) {
        Optional<DeployNotice> optional = noticeRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe(String.valueOf(id));
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<DeployNotice>> findPage(int pageSize, int pageNo, DeployNotice deployNotice) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Sort sort = Sort.by("gmtModified").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<DeployNotice> page = noticeRepository.page(pageable);
        return WrapperDTO.success(new PageDTO<DeployNotice>().convert(page));
    }

    @Override
    public WrapperDTO<PageDTO<DeployNotice>> pagePublic(int pageSize, int pageNo) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Sort sort = Sort.by("gmtModified").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<DeployNotice> page = noticeRepository.pagePublic(pageable);
        return WrapperDTO.success(new PageDTO<DeployNotice>().convert(page));
    }
}
