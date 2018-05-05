package com.pazukdev;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
    private static CategoryService instance;
    private static final Logger LOGGER = Logger.getLogger(HotelCategory.class.getName());

    private final HashMap<Long, HotelCategory> categories = new HashMap<>();
    private DAOHotelCategory daoHotelCategory = new DAOHotelCategory();
    //private static HotelCategory nullCategory=new HotelCategory();


    private CategoryService() {}


    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
            //instance.ensureTestData();
        }
        return instance;
    }


    /*public static HotelCategory getNullCategory() {
        return nullCategory;
    }*/


    public synchronized List<HotelCategory> findAll() {
        List<HotelCategory> list = daoHotelCategory.getList();
        sortList(list);
        return list;
    }


    public synchronized HotelCategory findById(Long id) {
        if (id != null) {
            for(HotelCategory category : daoHotelCategory.getList()) {
                if(category.getId() == id) return category;
            }
            return null;
        }
        return null;
    }


    private void sortList(List<HotelCategory> list) {
        Collections.sort(list, new Comparator<HotelCategory>() {
            @Override
            public int compare(HotelCategory o1, HotelCategory o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
    }


    public synchronized Integer count() {
        return categories.size();
    }


    public synchronized void delete(HotelCategory category) {
        daoHotelCategory.delete(category);
    }


    public synchronized void save(HotelCategory category) {
        if (category == null) {
            LOGGER.log(Level.SEVERE, "Category is null.");
            return;
        }

        if (category.getId() != null) {
            daoHotelCategory.update(category);
            return;
        }
        daoHotelCategory.create(category);
    }


    public void ensureTestData() {
        //nullCategory.setName("No category");

        if (findAll().isEmpty()) {
            final String[] categoryData = new String[] {
                    "Hotel", "Hostel", "GuestHouse", "Apartments"
            };

            for (String categoryName : categoryData) {

                HotelCategory category = new HotelCategory();
                category.setName(categoryName);
                save(category);
            }
        }
    }

}
