// Tightens Spring Data REST: catalog endpoints become read-only and expose entity ids.
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
// Without this, Spring Data REST would expose public write endpoints for catalog tables.
@Configuration
public class RestConfig implements RepositoryRestConfigurer {
    // Needed to list every managed entity from JPA's metamodel.
    private final EntityManager entityManager;
    @Autowired
    public RestConfig(EntityManager _entityManager){
        entityManager = _entityManager;
    }
    // Main hook: disable writes, expose ids, and allow GET-only CORS for SDR endpoints.
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        // Catalog tables may only be read through SDR; writes go through admin controllers.
        disableHttpMethods(Product.class,config);
        disableHttpMethods(ProductCategory.class,config);
        disableHttpMethods(City.class,config);
        // Ids must appear in responses so the frontend can use them in links and queries.
        exposeIds(config);
        // SDR doesn't inherit the Web MVC CORS rules, so reads declare their own here (writes are disabled anyway).
        cors.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    // Blocks PUT, POST, DELETE and PATCH on both single items and collections of this entity.
    public void disableHttpMethods(Class entityClass,RepositoryRestConfiguration config){
        // The four write verbs nobody may call on catalog endpoints.
        HttpMethod[] unsupportedMethods = {HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE,HttpMethod.PATCH};
        config.getExposureConfiguration().forDomainType(entityClass).withItemExposure((metadata, httpMethods)->httpMethods.disable(unsupportedMethods)).withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedMethods));

    }
    // Adds every entity id field to SDR responses, which they are hidden by default.
    private void exposeIds(RepositoryRestConfiguration config) {
        // All entity types JPA currently manages.
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        // Turn them into plain classes for exposeIdsFor.
        List<Class> entityClasses = new ArrayList<>();
        for (EntityType entityType:entities){
            entityClasses.add(entityType.getJavaType());
        }
        Class[] entityClassesArray  = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(entityClassesArray);


    }
}
