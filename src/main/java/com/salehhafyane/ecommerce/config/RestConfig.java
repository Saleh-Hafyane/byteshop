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
/**
 * Configuration class to customize the behavior of Spring Data REST repositories.
 * It configures HTTP methods for specific entities, CORS support, and entity ID exposure.
 */
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    // EntityManager provides access to the persistence context and the metamodel of managed entities.
    private final EntityManager entityManager;
    // Constructor for dependency injection of EntityManager.
    @Autowired
    public RestConfig(EntityManager _entityManager){
        entityManager = _entityManager;
    }
/**
 * Configures the repository REST settings, including disabling specific HTTP methods,
 * enabling CORS, and exposing entity IDs.
 */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        //disable http methods : put, post, delete.
        disableHttpMethods(Product.class,config);
        disableHttpMethods(ProductCategory.class,config);
        disableHttpMethods(City.class,config);
        // Expose entity IDs in REST responses
        exposeIds(config);
    }
    // Disables the specified HTTP methods (PUT, POST, DELETE) for a given entity type.
    public void disableHttpMethods(Class entityClass,RepositoryRestConfiguration config){
        // HTTP methods to disable
        HttpMethod[] unsupportedMethods = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};
        // Disable the specified methods for both item and collection resources
        config.getExposureConfiguration().forDomainType(entityClass).withItemExposure((metadata, httpMethods)->httpMethods.disable(unsupportedMethods)).withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods));

    }
/**
 * Exposes the IDs of all entities in the persistence context.
 * By default, Spring Data REST does not expose entity IDs in responses, but this can be overridden.
 */
    private void exposeIds(RepositoryRestConfiguration config) {
        //get entity types from EntityManager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        //get entity classes
        List<Class> entityClasses = new ArrayList<>();
        for (EntityType entityType:entities){
            entityClasses.add(entityType.getJavaType());
        }
        // Convert the list of classes to an array and configure ID exposure for each entity
        Class[] entityClassesArray  = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(entityClassesArray);


    }
}
