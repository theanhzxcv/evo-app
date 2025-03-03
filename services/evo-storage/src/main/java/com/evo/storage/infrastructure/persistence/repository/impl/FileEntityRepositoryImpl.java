package com.evo.storage.infrastructure.persistence.repository.impl;

import com.evo.storage.domain.FileSt;
import com.evo.storage.domain.query.FileSearchQuery;
import com.evo.storage.infrastructure.persistence.entity.FileEntity;
import com.evo.storage.infrastructure.persistence.mapper.FileEntityMapperImpl;
import com.evo.storage.infrastructure.persistence.repository.FileEntityRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FileEntityRepositoryImpl implements FileEntityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private final FileEntityMapperImpl fileEntityMapper;

    @Override
    public List<FileEntity> search(FileSearchQuery query) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select f from FileEntity f "
                + createWhereQuery(query, values);
        Query queryFile = entityManager.createQuery(sql, FileEntity.class);

        values.forEach(queryFile::setParameter);

        queryFile.setFirstResult((query.getPageIndex() - 1) * query.getPageSize());
        queryFile.setMaxResults(query.getPageSize());

        return queryFile.getResultList();
    }

    @Override
    public List<FileEntity> publicSearch(FileSearchQuery fileSearchQuery) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select f from FileEntity f "
                + createPublicWhereQuery(fileSearchQuery, values);
        Query query = entityManager.createQuery(sql, FileEntity.class);

        values.forEach(query::setParameter);

        query.setFirstResult((fileSearchQuery.getPageIndex() - 1) * fileSearchQuery.getPageSize());
        query.setMaxResults(fileSearchQuery.getPageSize());

        return query.getResultList();
    }

    @Override
    public Long count(FileSearchQuery fileSearchQuery) {
        Map<String, Object> values = new HashMap<>();
        String sql = "select count(f) from FileEntity f " +
                createWhereQuery(fileSearchQuery, values);
        Query query = entityManager.createQuery(sql, Long.class);
        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    private String createWhereQuery(FileSearchQuery fileSearchQuery, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        sql.append("where f.deleted = false");

        if (fileSearchQuery.getKeyword() != null && !fileSearchQuery.getKeyword().trim().isEmpty()) {
            String keyword = "%" + fileSearchQuery.getKeyword().trim().toLowerCase() + "%";

            sql.append(
                    " AND (LOWER(f.originalName) LIKE :keyword "
                            + " OR LOWER(f.extension) LIKE :keyword"
                            + " OR LOWER(f.type) LIKE :keyword "
                            + " OR LOWER(f.owner) LIKE :keyword) "
            );
            values.put("keyword", keyword);
        }
        return sql.toString();
    }

    private String createPublicWhereQuery(FileSearchQuery fileSearchQuery, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder();
        sql.append("where f.deleted = false and f.visibility = 'PUBLIC'");

        if (fileSearchQuery.getKeyword() != null && !fileSearchQuery.getKeyword().trim().isEmpty()) {
            String keyword = "%" + fileSearchQuery.getKeyword().trim().toLowerCase() + "%";

            sql.append(
                    " AND (LOWER(f.originalName) LIKE :keyword "
                            + " OR LOWER(f.extension) LIKE :keyword"
                            + " OR LOWER(f.type) LIKE :keyword "
                            + " OR LOWER(f.owner) LIKE :keyword) "
            );
            values.put("keyword", keyword);
        }
        return sql.toString();
    }
}
