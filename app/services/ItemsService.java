package services;

import models.ItemModel;
import play.db.jpa.JPA;
import play.libs.F;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ItemsService {

    public F.Promise<ItemModel> get(long id) {
        return F.Promise.pure(new ItemModel());
    }

    public F.Promise<List<ItemModel>> getUserItems(long userId) {
        // TODO: Ir a la API de items y traer todos los items del user
        return F.Promise.pure(new ArrayList<>());
    }

    public F.Promise<Long> createItem() {
        try {
            return F.Promise.promise(() -> {
                // TODO: Aplicar repository pattern (usar JPA en capa de infr)
                ItemModel item1 = new ItemModel();
                item1.title = "articulo 1";
                item1.price = 200D;
                item1.quantity = 10;

                return JPA.withTransaction(() -> {
                   EntityManager entityManager = JPA.em();
                    entityManager.persist(item1);
                    entityManager.flush();
                    return item1.id;
                });
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public F.Promise<ItemModel> getItem(long id) {
        try {
            return F.Promise.promise(() -> {

                return JPA.withTransaction(() -> JPA.em()
                    .find(ItemModel.class, id));
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }


}