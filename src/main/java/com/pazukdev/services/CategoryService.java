package com.pazukdev.services;

import com.pazukdev.dao.DAOCategory;
import com.pazukdev.entities.Category;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
    private static CategoryService instance;
    private static final Logger LOGGER = Logger.getLogger(Category.class.getName());

    private final HashMap<Long, Category> categories = new HashMap<>();
    private com.pazukdev.dao.DAOCategory DAOCategory = new DAOCategory();
    //private static Category nullCategory=new Category();


    private CategoryService() {}


    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
            //instance.ensureTestData();
        }
        return instance;
    }


    /*public static Category getNullCategory() {
        return nullCategory;
    }*/


    public synchronized List<Category> findAll() {
        List<Category> list = DAOCategory.getList();
        sortList(list);
        return list;
    }


    public synchronized Category findById(Long id) {
        if (id != null) {
            for(Category category : DAOCategory.getList()) {
                if(category.getId() == id) return category;
            }
            return null;
        }
        return null;
    }


    private void sortList(List<Category> list) {
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
    }


    public synchronized Integer count() {
        return categories.size();
    }


    public synchronized void delete(Category category) {
        DAOCategory.delete(category);
    }


    public synchronized void save(Category category) {
        if (category == null) {
            LOGGER.log(Level.SEVERE, "Category is null.");
            return;
        }

        if (category.getId() != null) {
            DAOCategory.update(category);
            return;
        }
        DAOCategory.create(category);
    }


    public void ensureTestData() {
        //nullCategory.setName("No category");

        if (findAll().isEmpty()) {
            final String[] categoryData = new String[] {
                    "Hotel", "Hostel", "GuestHouse", "Apartments"
            };

            for (String categoryName : categoryData) {

                Category category = new Category();
                category.setName(categoryName);
                save(category);
            }
        }
    }

}
