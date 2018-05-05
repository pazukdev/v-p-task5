package com.pazukdev.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DataProvider {
    private final static EntityManagerFactory entityManagerFactory;
    private final static DataProvider instance;

    static {
        String providerName = null;
        try {
            InputStream propertiesIS = DataProvider.class.getClassLoader()
                    .getResourceAsStream("META-INF/properties.properties");
            Properties properties = new Properties();
            properties.load(propertiesIS);
            providerName = properties.getProperty("persistence_unit_name");
            propertiesIS.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(providerName);
        instance = new DataProvider();
    }

    private DataProvider() {}

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static DataProvider getInstance() {
        return instance;
    }

    @Override
    protected void finalize() throws Throwable {
        entityManagerFactory.close();
    }
}
