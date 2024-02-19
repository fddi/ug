package top.ulug.jpa.tool;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.base.util.StringUtils;
import top.ulug.jpa.dto.PageDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by liujf on 2022/3/8.
 * 逝者如斯夫 不舍昼夜
 */
public class HbNative {
    private EntityManager entityManager;

    public HbNative(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = Exception.class)
    public int execute(String sql, Map<String, Object> params) {
        int result = -1;
        try {
            NativeQuery query = entityManager.createNativeQuery(sql).unwrap(NativeQuery.class);
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            result = query.executeUpdate();
        } catch (Exception e) {
            result = -1;
            e.printStackTrace();
        } finally {
            return result;
        }
    }


    /**
     * @param sql    执行语句
     * @param params 参数集
     * @param tClass 示例类型
     * @param <T>    实例
     * @return 结果集
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public <T> List<T> queryList(String sql, Map<String, Object> params, Class<T> tClass)
            throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new Exception("SQL语句为空");
        }
        List list;
        NativeQuery query = entityManager.createNativeQuery(sql).unwrap(NativeQuery.class);
        if (Map.class == tClass) {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else {
            query.addEntity(tClass);
        }
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (sql.contains(":" + entry.getKey())) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        list = query.list();
        return list;
    }


    /**
     * @param sql     语句
     * @param params  参数
     * @param pno     页码(与JPA统一从0开始)
     * @param perSize 查询大小
     * @param tClass  示例类型
     * @param <T>     实例
     * @return 结果集
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public <T> PageDTO<T> queryPageList(String sql, Map<String, Object> params,
                                        Integer pno, Integer perSize, Class<T> tClass) throws Exception {
        return queryPageList(sql, null, params, pno, perSize, tClass);
    }

    @Transactional(readOnly = true)
    public <T> PageDTO<T> queryPageList(String sql, String totalSql, Map<String, Object> params,
                                        Integer pno, Integer perSize, Class<T> tClass) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new Exception("SQL语句为空");
        }
        pno = pno == null ? 0 : pno;
        int start = pno * perSize;
        int size = perSize;
        List list;
        NativeQuery query = entityManager.createNativeQuery(sql).unwrap(NativeQuery.class);
        if (tClass == Map.class) {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else {
            query.addEntity(tClass);
        }
        query.setFirstResult(start);
        query.setMaxResults(size);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (sql.contains(":" + entry.getKey())) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        list = query.getResultList();
        PageDTO<T> page = new PageDTO<>();
        page.setContent(list);
        page.setSize(perSize);
        page.setNumber((int) Math.ceil(start / perSize));
        if (StringUtils.isEmpty(totalSql)) {
            totalSql = "select count(*) from (" + sql + ") ct";
        }
        int total = this.queryTotal(totalSql, params);
        page.setTotalElements(total);
        page.setTotalPages((int) Math.ceil(total / perSize));
        return page;
    }

    @Transactional(readOnly = true)
    public int queryTotal(String sql, Map<String, Object> params)
            throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new Exception("SQL语句为空");
        }
        int total = 0;
        Query query = entityManager.createNativeQuery(sql);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (sql.contains(":" + entry.getKey())) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        Object data = query.getSingleResult();
        if (data != null) {
            total = Integer.parseInt(String.valueOf(data));
        }
        return total;
    }

}
