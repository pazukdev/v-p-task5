package com.pazukdev.dao;

import com.pazukdev.db.DataProvider;
import com.pazukdev.entities.Category;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class DAOCategory implements DAOInterface<Category> {

    @Override
    public void create(Category category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOCategory create Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Category read(Category category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            Category hc = em.find(Category.class, category.getId());
            em.getTransaction().commit();
            return hc;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOCategory read Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Category category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            Category hc = em.find(Category.class, category.getId());
            hc.setName(category.getName());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOCategory update Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Category category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            Category hc = em.find(Category.class, category.getId());
            em.remove(hc);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOCategory delete Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> getList() {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Category> criteria = cb.createQuery(Category.class);
            Root<Category> root = criteria.from(Category.class);
            criteria.select(root);
            List<Category> list = em.createQuery(criteria).getResultList();
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOCategory getList Exception", e);
        } finally {
            em.close();
        }
    }
}
