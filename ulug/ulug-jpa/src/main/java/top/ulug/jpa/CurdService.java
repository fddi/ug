package top.ulug.jpa;


import top.ulug.base.dto.WrapperDTO;
import top.ulug.jpa.dto.PageDTO;

/**
 * Created by liujf on 2019/3/25.
 * 逝者如斯夫 不舍昼夜
 */
public interface CurdService<E,D> {

    /**
     * 保存
     *
     * @param e
     * @return
     */
    WrapperDTO<String> save(E... e) throws Exception;

    /**
     * 删除
     *
     * @param e
     * @return
     */
    WrapperDTO<String> del(E... e);

    /**
     * 精确查询一项
     *
     * @param id id
     * @return dto
     */
    WrapperDTO<D> findOne(Long id);

    /**
     * 分页数据
     *
     * @param pageSize 页大小
     * @param pageNo   页码
     * @param e        查询条件
     * @return page
     */
    WrapperDTO<PageDTO<D>> findPage(int pageSize, int pageNo, E e);
}
