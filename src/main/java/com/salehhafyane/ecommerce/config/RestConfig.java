package com.salehhafyane.ecommerce.config;

import com.salehhafyane.ecommerce.entity.City;
import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    private EntityManager entityManager;
    @Autowired
    public RestConfig(EntityManager _entityManager){
        entityManager = _entityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        //disable http methods : put, post, delete.
        disableHttpMethods(Product.class,config);
        disableHttpMethods(ProductCategory.class,config);
        disableHttpMethods(City.class,config);


        exposeIds(config);
    }
    public void disableHttpMethods(Class entityClass,RepositoryRestConfiguration config){
        HttpMethod[] unsupportedMethods = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        config.getExposureConfiguration().forDomainType(entityClass).withItemExposure((metdata, httpMethods)->httpMethods.disable(unsupportedMethods)).withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods));

    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //get entity types from EntityManager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        //get entity classes
        List<Class> entityClasses = new ArrayList<>();
        for (EntityType entityType:entities){
            entityClasses.add(entityType.getJavaType());
        }
        //exposes entity ids
        Class[] entityClassesArray  = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(entityClassesArray);


    }
}
