package top.ulug.core.auth.service.impl;

import com.alibaba.excel.EasyExcel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthOrg;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.dto.OrgDTO;
import top.ulug.core.auth.repository.AuthOrgRepository;
import top.ulug.core.auth.service.AuthService;
import top.ulug.core.auth.service.OrgService;
import top.ulug.core.deploy.repository.CodeDictRepository;
import top.ulug.core.deploy.service.CacheService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2019/4/2.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class OrgServiceImpl implements OrgService {
    public static final String TYPE_ORG_UNIT = "01";
    @Autowired
    private AuthOrgRepository orgRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private CodeDictRepository dictRepository;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CacheService cacheService;

    @Override
    public WrapperDTO<TreeDTO> findChildrenOrg(String areaCode, Long parentId) {
        if (StringUtils.isEmpty(areaCode)) {
            String appId = requestUtils.getCurrentAppId();
            String token = requestUtils.getCurrentToken();
            AuthDTO authDTO = cacheService.getAuth(appId, token);
            assert authDTO != null;
            areaCode = authDTO.getAccount().getAreaCode();
        }
        parentId = parentId == null ? 0 : parentId;
        TreeDTO root;
        if (0 == parentId) {
            root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                    -1, "", "", "", false, "");
        } else {
            AuthOrg org = orgRepository.findById(parentId).get();
            root = new TreeDTO(org.getOrgId(), org.getParentId(),
                    org.getOrgCode(), org.getOrgCode(),
                    org.getOrgName(), org.getOrgName(),
                    org.getOrgSort(), "", "", org.getStatus(), false, org.getOrgType());
        }
        List<TreeDTO> treeDTOList = new ArrayList<>();
        for (AuthOrg org : this.listOrg(areaCode)) {
            treeDTOList.add(new TreeDTO(org.getOrgId(), org.getParentId(),
                    org.getOrgCode(), org.getOrgCode(),
                    org.getOrgName(), org.getOrgName(),
                    org.getOrgSort(), "", "", org.getStatus(), false, org.getOrgType()));
        }
        root.findChild(root, treeDTOList);
        return WrapperDTO.success(root);
    }

    @Override
    public List<AuthOrg> listOrg(String areaCode) {
        List<AuthOrg> list = new ArrayList<>();
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return list;
        }
        if (authService.checkDevOps()) {
            //开发账号查询全部
            list = orgRepository.findByAreaCode(areaCode);
        } else {
            //查询所属单位机构
            list = orgRepository.findByUnitCodeOrderByOrgSort(
                    authDTO.getAccount().getUnitCode());
        }
        return list;
    }

    @Override
    public WrapperDTO<List<LabelDTO>> search(String searchKey, String orgType) {
        searchKey = StringUtils.isEmpty(searchKey) ? "" : searchKey;
        orgType = StringUtils.isEmpty(orgType) ? TYPE_ORG_UNIT : orgType;
        List<AuthOrg> list = orgRepository.search(searchKey, orgType);
        modelMapper.typeMap(AuthOrg.class, LabelDTO.class).addMappings(mapper -> {
            mapper.map(AuthOrg::getOrgId,
                    LabelDTO::setId);
            mapper.map(AuthOrg::getOrgName,
                    LabelDTO::setLabel);
            mapper.map(AuthOrg::getOrgName,
                    LabelDTO::setTitle);
            mapper.map(AuthOrg::getOrgCode,
                    LabelDTO::setKey);
            mapper.map(AuthOrg::getOrgCode,
                    LabelDTO::setValue);
        });
        List<LabelDTO> labelDTOList = modelMapper.map(list,
                new TypeToken<List<LabelDTO>>() {
                }.getType());
        return WrapperDTO.success(labelDTOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception {
        //同步读取,自动finish
        List<Object> list = EasyExcel.read(file.getInputStream())
                .head(OrgDTO.class).sheet().doReadSync();
        List<AuthOrg> saveList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            OrgDTO data = (OrgDTO) list.get(i);
            if (StringUtils.isEmpty(data.getOrgName()) || StringUtils.isEmpty(data.getOrgCode())
                    || StringUtils.isEmpty(data.getAreaCode()) || StringUtils.isEmpty(data.getUnitCode())
                    || StringUtils.isEmpty(data.getParentCode())) {
                throw new Exception(String.format("第%d行出错：必填项存在空值，请修改！", i + 1));
            }
            saveList.add(data.convert());
        }
        if (StringUtils.isEmpty(dataType) || "list".equals(dataType)) {
            return this.save(saveList.toArray(new AuthOrg[saveList.size()]));
        } else if ("tree".equals(dataType)) {
            List<AuthOrg> treeList = orgRepository.saveAll(saveList);
            AuthOrg root = new AuthOrg();
            root.setOrgCode("无");
            root.setOrgId(0L);
            root.setOrgPath("");
            this.findChild(root, treeList);
            orgRepository.saveAll(treeList);
        }
        return WrapperDTO.success(null);
    }

    @Override
    public WrapperDTO<String> dragDrop(Long dragId, Long dropId) {
        Optional<AuthOrg> optDrag = orgRepository.findById(dragId);
        if (!optDrag.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthOrg");
        }
        AuthOrg org = optDrag.get();
        if (TYPE_ORG_UNIT.equals(org.getOrgType())) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
        }
        String path = "";
        if (dropId > 0) {
            path = orgRepository.findPath(dropId) + ">";
        }
        path += org.getOrgId();
        org.setParentId(dropId);
        org.setOrgPath(path);
        orgRepository.save(org);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> save(AuthOrg... orgs) {
        List<AuthOrg> list = Arrays.asList(orgs);
        for (AuthOrg org : list) {
            if (!authService.checkDevOps() && TYPE_ORG_UNIT.equals(org.getOrgType())) {
                //非开发者不能新建单位
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
            }
            AuthOrg findOrg = orgRepository.findByUnitCode(org.getUnitCode());
            if (TYPE_ORG_UNIT.equals(org.getOrgType()) &&
                    findOrg != null && !findOrg.getOrgId().equals(org.getOrgId())) {
                //单位代码重复
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_ORG_REPEAT, null);
            }
            //单位代码与上级机构一致
            if (org.getParentId() != null && org.getParentId() > 0) {
                String unitCode = orgRepository.findUnitCode(org.getParentId());
                org.setUnitCode(unitCode);
            }
        }
        //保存路径
        List<AuthOrg> saveList = orgRepository.saveAll(list);
        for (AuthOrg org : saveList) {
            String path = "";
            if (org.getParentId() != null && org.getParentId() > 0) {
                path = orgRepository.findPath(org.getParentId()) + ">";
            }
            path += org.getOrgId();
            org.setOrgPath(path);
        }
        orgRepository.saveAll(saveList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(AuthOrg... orgList) {
        List<AuthOrg> list = Arrays.asList(orgList);
        //删除下属所有数据
        List<AuthOrg> delList = new ArrayList<>();
        for (AuthOrg org : list) {
            Optional<AuthOrg> op = orgRepository.findById(org.getOrgId());
            if (op.isEmpty() || (!authService.checkDevOps() &&
                    !authService.checkUnitCode(op.get().getUnitCode()))) {
                //不能删除非本单位机构
                continue;
            }
            String path = orgRepository.findPath(org.getOrgId());
            delList.add(op.get());
            if (!StringUtils.isEmpty(path)) {
                List<AuthOrg> childList = orgRepository.findByOrgPath(path + ">");
                delList.addAll(childList);
            }
        }
        orgRepository.deleteAllInBatch(delList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<AuthOrg> findOne(Long id) {
        Optional<AuthOrg> optional = orgRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<AuthOrg>> findPage(int pageSize, int pageNo, AuthOrg authOrg) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AuthOrg> page = orgRepository.page(
                authOrg.getAreaCode(), authOrg.getOrgName(), pageable);
        return WrapperDTO.success(new PageDTO<AuthOrg>().convert(page));
    }

    private void findChild(AuthOrg node, List<AuthOrg> list) {
        String parentCode = node.getOrgCode();
        for (AuthOrg org : list) {
            if (parentCode.equals(org.getParentCode())) {
                org.setParentId(node.getOrgId());
                String path = "";
                if (!StringUtils.isEmpty(node.getOrgPath())) {
                    path = node.getOrgPath() + ">";
                }
                path += org.getOrgId();
                org.setOrgPath(path);
                findChild(org, list);
            }
        }
    }
}
