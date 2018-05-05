package com.pazukdev;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class DAOHotelCategory implements DAOInterface<HotelCategory> {

    @Override
    public void create(HotelCategory category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOHotelCategory create Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public HotelCategory read(HotelCategory category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            HotelCategory hc = em.find(HotelCategory.class, category.getId());
            em.getTransaction().commit();
            return hc;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOHotelCategory read Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(HotelCategory category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            HotelCategory hc = em.find(HotelCategory.class, category.getId());
            hc.setName(category.getName());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOHotelCategory update Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(HotelCategory category) {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            HotelCategory hc = em.find(HotelCategory.class, category.getId());
            em.remove(hc);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOHotelCategory delete Exception", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HotelCategory> getList() {
        EntityManager em = DataProvider.getEntityManager();
        try {
            em.getTransaction().begin();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<HotelCategory> criteria = cb.createQuery(HotelCategory.class);
            Root<HotelCategory> root = criteria.from(HotelCategory.class);
            criteria.select(root);
            List<HotelCategory> list = em.createQuery(criteria).getResultList();
            em.getTransaction().commit();
            return list;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("DAOHotelCategory getList Exception", e);
        } finally {
            em.close();
        }
    }
}
