package com.pazukdev;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {

    private static CategoryService instance;
    private static final Logger LOGGER = Logger.getLogger(HotelCategory.class.getName());

    private static HotelCategory nullCategory=new HotelCategory();

    private final HashMap<Long, HotelCategory> categories = new HashMap<>();
    //private long nextId = 0;


    private CategoryService() {}

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
            instance.ensureTestData();
        }
        return instance;
    }

    public static HotelCategory getNullCategory() {
        return nullCategory;
    }

    public synchronized List<HotelCategory> findAll() {
        DAOHotelCategory daoHotelCategory = new DAOHotelCategory();
        List<HotelCategory> list = daoHotelCategory.getList();
        Collections.sort(list, new Comparator<HotelCategory>() {
            @Override
            public int compare(HotelCategory o1, HotelCategory o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });
        return list;
    }

    /*public synchronized HotelCategory findByName(String categoryName) {
        DAOHotelCategory daoHotelCategory = new DAOHotelCategory();
        List<HotelCategory> list = daoHotelCategory.getList();
        HotelCategory category = null;

        for(HotelCategory c : list) {
            if(c.getName().equals(categoryName)) category=c;
        }

        return category;
    }*/

    public synchronized HotelCategory findById(Integer id) {
        if (id != null) {
            DAOHotelCategory daoHotelCategory = new DAOHotelCategory();
            HotelCategory category = daoHotelCategory.read(new HotelCategory(id));
            return category;
        }
        return null;
    }

    public synchronized Integer count() {
        return categories.size();
    }

    public synchronized void delete(HotelCategory category) {
        DAOHotelCategory daoHotelCategory = new DAOHotelCategory();
        daoHotelCategory.delete(category);
    }

    public synchronized void save(HotelCategory category) {
        if (category == null) {
            LOGGER.log(Level.SEVERE, "Category is null.");
            return;
        }
        DAOHotelCategory daoHotelCategory = new DAOHotelCategory();
        if (category.getId() != null) {
            daoHotelCategory.update(category);
            return;
        }
        daoHotelCategory.create(category);
    }

    public void ensureTestData() {

        nullCategory.setName("No category");

        if (findAll().isEmpty()) {
            final String[] categoryData = new String[] {
                    "Hotel", "Hostel", "GuestHouse", "Apartments"
            };

            for (String categoryName : categoryData) {

                HotelCategory category = new HotelCategory();
                category.setName(categoryName);
                //save(category);
            }
        }
    }

}
