package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.util.StringUtils;
import top.ulug.core.deploy.domain.MultiMessage;
import top.ulug.core.deploy.repository.MultiMessageRepository;
import top.ulug.core.deploy.service.MultiMessageService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:30 星期五
 */
public class MultiMessageServiceImpl implements MultiMessageService {
    @Autowired
    private MultiMessageRepository multiMessageRepository;

    @Override
    public WrapperDTO<String> save(MultiMessage... e) throws Exception {
        List<MultiMessage> list = Arrays.asList(e);
        multiMessageRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(MultiMessage... e) {
        List<MultiMessage> list = Arrays.asList(e);
        multiMessageRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<MultiMessage> findOne(Long id) {
        Optional<MultiMessage> optional = multiMessageRepository.findById(id);
        return optional.map(WrapperDTO::success).orElseGet(() -> WrapperDTO.npe(String.valueOf(id)));
    }

    @Override
    public WrapperDTO<PageDTO<MultiMessage>> findPage(int pageSize, int pageNo, MultiMessage multiMessage) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("gmtModified").descending());
        String title = StringUtils.isEmpty(multiMessage.getTitle()) ? "" : multiMessage.getTitle();
        Page<MultiMessage> page = multiMessageRepository.findByTitleLike(title, pageable);
        return WrapperDTO.success(new PageDTO<MultiMessage>().convert(page));
    }
}
