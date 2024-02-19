package top.ulug.cms.football.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.football.domain.BallMatch;
import top.ulug.cms.football.domain.BallSystem;
import top.ulug.cms.football.dto.MatchDTO;
import top.ulug.cms.football.repository.BallMatchRepository;
import top.ulug.cms.football.repository.BallSystemRepository;
import top.ulug.cms.football.service.BallMatchService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author liu
 * @Date 2022/9/5 21:11 星期一
 */
@Service
public class BallMatchServiceImpl implements BallMatchService {
    @Autowired
    BallMatchRepository ballMatchRepository;
    @Autowired
    BallSystemRepository systemRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<String> save(BallMatch... e) throws Exception {
        List<BallMatch> list = Arrays.asList(e);
        for (BallMatch match : list) {
            Optional<BallSystem> optional = systemRepository.findById(match.getSystemId());
            if (optional.isEmpty()) {
                return WrapperDTO.npe("BallSystem");
            }
            match.setSystem(optional.get());
        }
        ballMatchRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(BallMatch... e) {
        List<BallMatch> list = Arrays.asList(e);
        ballMatchRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<MatchDTO> findOne(Long id) {
        Optional<BallMatch> optional = ballMatchRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("BallMatch");
        }
        return WrapperDTO.success(modelMapper.map(optional.get(), MatchDTO.class));
    }

    @Override
    public WrapperDTO<PageDTO<MatchDTO>> findPage(int pageSize, int pageNo, BallMatch ballMatch) {
        if (ballMatch.getSystemId() == null) {
            return WrapperDTO.npe("getSystemId");
        }
        Optional<BallSystem> system = systemRepository.findById(ballMatch.getSystemId());
        if (system.isEmpty()) {
            return WrapperDTO.npe("BallSystem");
        }
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String name = ballMatch.getHomeName() == null ? "" : ballMatch.getHomeName();
        Page<MatchDTO> page;
        if ("1".equals(system.get().getSystemType())) {
            page = ballMatchRepository.pageCountry(ballMatch.getSystemId(), name, pageable);
        } else {
            page = ballMatchRepository.pageClub(ballMatch.getSystemId(), name, pageable);
        }
        return WrapperDTO.success(new PageDTO<MatchDTO>().convert(page));
    }
}
