package resume.application.repository;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import resume.application.model.Resume;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

/**
 * Работа с базой данных
 */
@Repository
@Transactional
public class ResumeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Добавление резюме пользователей или обновление существующих.
     *
     * @param resumes Список резюме для добавления
     */
    public void insertOrUpdate(List<Resume> resumes) {
        for (Resume resume : resumes) {
            entityManager.merge(resume);
        }
    }

    /**
     * Поиск времени обновления самого нового резюме
     *
     * @return Время обновления или null
     */
    public Timestamp findMaxTimestamp() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Resume.class).get();

        Query query = queryBuilder.all().withConstantScore().createQuery();
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Resume.class);
        Sort sort = new Sort(new SortField("updateDate", SortField.LONG, true));
        fullTextQuery.setSort(sort);
        fullTextQuery.setMaxResults(1);

        @SuppressWarnings("unchecked")
        List<Resume> resumes = fullTextQuery.getResultList();

        return resumes.size() == 0 ? null : resumes.get(0).getUpdateDate();
    }

    /**
     * Поиск резюме с использованием HibernateSearch.
     *
     * @param keywords Ключевые слова для поиска
     */
    public List<Resume> search(String keywords) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Resume.class).get();
        Query query = queryBuilder.keyword().onFields("title", "description").matching(keywords).createQuery();
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Resume.class);
        fullTextQuery.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        @SuppressWarnings("unchecked")
        List<Resume> resumes = fullTextQuery.getResultList();

        return resumes;
    }


}
