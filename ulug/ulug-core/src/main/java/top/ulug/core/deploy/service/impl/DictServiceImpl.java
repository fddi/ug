package top.ulug.core.deploy.service.impl;

import com.alibaba.excel.EasyExcel;
import org.antlr.v4.runtime.misc.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.NormTreeDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.util.StringUtils;
import top.ulug.core.deploy.domain.CodeDict;
import top.ulug.core.deploy.dto.DictDTO;
import top.ulug.core.deploy.repository.CodeCatalogRepository;
import top.ulug.core.deploy.repository.CodeDictRepository;
import top.ulug.core.deploy.service.DictService;
import top.ulug.base.dto.PageDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2019/4/11.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class DictServiceImpl implements DictService {
    public static final String TYPE_TREE = "tree";
    @Autowired
    private CodeDictRepository dictRepository;
    @Autowired
    private CodeCatalogRepository catalogRepository;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<List<LabelDTO>> listCatalogDict(String catalog, String dictCode) {
        List<CodeDict> list;
        if (StringUtils.isEmpty(dictCode)) {
            list = dictRepository.findByCatalogOrderByDictSort(catalog);
        } else {
            CodeDict dict = dictRepository.findByCatalogAndDictCode(catalog, dictCode);
            list = dictRepository.findByCatalogAndDictPath(catalog, dict.getDictPath());
        }
        modelMapper.typeMap(CodeDict.class, LabelDTO.class).addMappings(mapper -> {
            mapper.map(CodeDict::getDictId,
                    LabelDTO::setId);
            mapper.map(CodeDict::getDictName,
                    LabelDTO::setLabel);
            mapper.map(CodeDict::getDictName,
                    LabelDTO::setTitle);
            mapper.map(CodeDict::getDictCode,
                    LabelDTO::setKey);
            mapper.map(CodeDict::getDictCode,
                    LabelDTO::setValue);
        });
        List<LabelDTO> labelDTOList = modelMapper.map(list,
                new TypeToken<List<LabelDTO>>() {
                }.getType());
        return WrapperDTO.success(labelDTOList);
    }

    @Override
    public WrapperDTO<TreeDTO> findCatalogDict(String catalog, String dictCode, String dataType) {
        List<CodeDict> list;
        TreeDTO root;
        NormTreeDTO normRoot;
        TreeDTO resultTree;
        if (StringUtils.isEmpty(dictCode)) {
            list = dictRepository.findByCatalogOrderByDictSort(catalog);
            root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                    -1, "", "", "", false, "");
            normRoot = new NormTreeDTO(0L, "0", "根目录", -1L);
        } else {
            CodeDict dict = dictRepository.findByCatalogAndDictCode(catalog, dictCode);
            if (dict == null) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "CodeDict");
            }
            list = dictRepository.findByCatalogAndDictPath(catalog, dict.getDictPath());
            root = new TreeDTO(dict.getDictId(), dict.getParentId(), dict.getDictCode(), dict.getDictCode(),
                    dict.getDictName(), dict.getDictName(),
                    dict.getDictSort(), dict.getIcon(), "", "1", false, "");
            normRoot = new NormTreeDTO(dict.getDictId(), dict.getDictCode(),
                    dict.getDictName(), dict.getParentId());
        }
        if (dataType == null || TYPE_TREE.equals(dataType)) {
            List<TreeDTO> treeDTOList = new ArrayList<>();
            for (CodeDict dict : list) {
                TreeDTO child = new TreeDTO(dict.getDictId(), dict.getParentId(), dict.getDictCode(), dict.getDictCode(),
                        dict.getDictName(), dict.getDictName(),
                        dict.getDictSort(), dict.getIcon(), "", "1", false, "");
                treeDTOList.add(child);
            }
            root.findChild(root, treeDTOList);
            resultTree = root;
        } else {
            List<NormTreeDTO> treeDTOList = new ArrayList<>();
            for (CodeDict dict : list) {
                NormTreeDTO child = new NormTreeDTO(dict.getDictId(), dict.getDictCode(),
                        dict.getDictName(), dict.getParentId());
                treeDTOList.add(child);
            }
            normRoot.findChild(normRoot, treeDTOList);
            resultTree = normRoot;
        }
        return WrapperDTO.success(resultTree);
    }

    @Override
    public WrapperDTO<String> save(CodeDict... dictArray) {
        List<CodeDict> list = Arrays.asList(dictArray);
        List<CodeDict> saveList = dictRepository.saveAll(list);
        for (CodeDict dict : saveList) {
            String path = "";
            if (dict.getParentId() != null && dict.getParentId() > 0) {
                path = dictRepository.findPath(dict.getParentId()) + ">";
            }
            path += dict.getDictId();
            dict.setDictPath(path);
        }
        dictRepository.saveAll(saveList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(CodeDict... dictArray) {
        List<CodeDict> list = Arrays.asList(dictArray);
        //删除下属所有数据
        List<CodeDict> delList = new ArrayList<>();
        for (CodeDict dict : list) {
            String path = dictRepository.findPath(dict.getDictId());
            List<CodeDict> childList = dictRepository.findByDictPath(path + ">");
            delList.addAll(childList);
        }
        delList.addAll(list);
        dictRepository.deleteAllInBatch(delList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<CodeDict> findOne(Long id) {
        Optional<CodeDict> optional = dictRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, String.valueOf(id));
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<CodeDict>> findPage(int pageSize, int pageNo, CodeDict dict) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CodeDict> page = dictRepository.findAll(pageable);
        return WrapperDTO.success(new PageDTO<CodeDict>().convert(page));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception {
        //同步读取,自动finish
        List<Object> list = EasyExcel.read(file.getInputStream())
                .head(DictDTO.class).sheet().doReadSync();
        List<CodeDict> saveList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DictDTO data = (DictDTO) list.get(i);
            if (StringUtils.isEmpty(data.getCatalog()) || StringUtils.isEmpty(data.getDictCode())
                    || StringUtils.isEmpty(data.getParentCode()) || StringUtils.isEmpty(data.getDictName())) {
                throw new Exception(String.format("第%d行出错：必填项存在空值，请修改！", i + 1));
            }
            saveList.add(data.convert());
        }
        if (StringUtils.isEmpty(dataType) || "list".equals(dataType)) {
            return this.save(saveList.toArray(new CodeDict[saveList.size()]));
        } else if ("tree".equals(dataType)) {
            List<CodeDict> treeList = dictRepository.saveAll(saveList);
            CodeDict root = new CodeDict();
            root.setDictCode("无");
            root.setDictId(0L);
            root.setDictPath("");
            this.findChild(root, treeList);
            dictRepository.saveAll(treeList);
        }
        return WrapperDTO.success();
    }

    private void findChild(CodeDict node, List<CodeDict> list) {
        String parentCode = node.getDictCode();
        for (CodeDict dict : list) {
            if (parentCode.equals(dict.getParentCode())) {
                dict.setParentId(node.getDictId());
                String path = "";
                if (!StringUtils.isEmpty(node.getDictPath())) {
                    path = node.getDictPath() + ">";
                }
                path += dict.getDictId();
                dict.setDictPath(path);
                findChild(dict, list);
            }
        }
    }
}
