package com.pazukdev;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {

    private static CategoryService instance;
    private static final Logger LOGGER = Logger.getLogger(Category.class.getName());

    private static Category nullCategory=new Category();

    private final HashMap<Long, Category> categories = new HashMap<>();
    private long nextId = 0;


    private CategoryService() {}

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
            instance.ensureTestData();
        }
        return instance;
    }

    public static Category getNullCategory() {
        return nullCategory;
    }

    public synchronized List<Category> findAll() {
        return findAll(null);
    }

    public synchronized List<Category> findAll(String stringFilter) {
        ArrayList<Category> arrayList = new ArrayList<>();
        for (Category category : categories.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || category.toString().toLowerCase().contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(category.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(arrayList, new Comparator<Category>() {

            @Override
            public int compare(Category o1, Category o2) {
                return (int)(o2.getId() - o1.getId());
            }
        });
        return arrayList;
    }



    public synchronized Integer count() {
        return categories.size();
    }

    public synchronized void delete(Category category) {
        categories.remove(category.getId());
    }

    public synchronized void save(Category entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE, "Category is null");
            return;
        }
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (Category) entry.clone();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        categories.put(entry.getId(), entry);
    }

    public void ensureTestData() {

        nullCategory.setName("No category");

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
