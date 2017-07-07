package org.cocome.tradingsystem.remote.access.dao.plant;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.IData;
import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.store.Store;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.DataAccessObject;
import org.cocome.tradingsystem.remote.access.dao.enterprise.TradingEnterpriseDAO;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Plant}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class PlantDAO implements DataAccessObject<Plant> {

    @EJB
    private TradingEnterpriseDAO tradingEnterpriseDAO;

    @PersistenceUnit(unitName = IData.EJB_PERSISTENCE_UNIT_NAME)
    private EntityManagerFactory emf;

    @Override
    public String getEntityTypeName() {
        return "store";
    }

    @Override
    public Notification createEntities(final List<Plant> entities) throws IllegalArgumentException {
        final Notification notification = new Notification();
        if (entities != null) {
            final EntityManager em = this.emf.createEntityManager();
            for (final Plant nextStore : entities) {
                final TradingEnterprise _enterprise = tradingEnterpriseDAO.queryEnterprise(em, nextStore.getEnterprise());
                if (_enterprise == null) {
                    notification.addNotification(
                            "createStore", Notification.FAILED,
                            "Creation Plant failed, Enterprise not available:"
                                    + nextStore.getEnterprise() + "," + nextStore);
                    continue;
                }
                final Store store = new Store();
                store.setName(nextStore.getName());
                store.setLocation(nextStore.getLocation());
                store.setEnterprise(_enterprise);
                _enterprise.getStores().add(store);
                em.persist(store);
                em.merge(_enterprise);
                notification.addNotification(
                        "createPlant", Notification.SUCCESS,
                        "Creation Plant:" + store);
            }
            em.flush();
            em.close();
            return notification;
        }
        throw new IllegalArgumentException("[createPlant]given arguments are null");
    }

    @Override
    public Notification updateEntities(List<Plant> entities) throws IllegalArgumentException {
        final EntityManager em = this.emf.createEntityManager();
        final Notification notification = new Notification();
        Plant _store;
        for (final Plant nextStore : entities) {
            _store = this.queryPlantById(em, nextStore);
            if (_store == null) {
                notification.addNotification(
                        "updateStore", Notification.FAILED,
                        "Store not available:" + nextStore);
                continue;
            }
            // update location
            _store.setLocation(nextStore.getLocation());
            // update name
            _store.setName(nextStore.getName());

            em.merge(_store);
            notification.addNotification(
                    "updateStore", Notification.SUCCESS,
                    "Update Store:" + _store);
        }
        em.flush();
        em.close();
        return notification;
    }

    @Override
    public Table<String> toTable(final List<Plant> list) {
        final Table<String> table = new Table<>();
        table.addHeader("EnterpriseId", "PlantId", "PlantName", "PlantLocation");
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getEnterprise().getId()));
            table.set(i, 1, String.valueOf(list.get(i).getId()));
            table.set(i, 2, list.get(i).getName());
            table.set(i, 3, list.get(i).getLocation());
        }
        return table;
    }

    @Override
    public List<Plant> fromTable(final Table<String> table) {
        final int len = table.size();
        final List<Plant> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colEnterprise = table.getColumnByName(i, "EnterpriseId");
            final TradingEnterprise t = new TradingEnterprise();
            t.setId(Long.parseLong(colEnterprise.getValue()));
            final Column<String> colId = table.getColumnByName(i, "PlantId");
            final Column<String> colName = table.getColumnByName(i, "PlantName");
            final Column<String> colLocation = table.getColumnByName(i, "PlantLocation");
            final Plant plant = new Plant();
            if (colId != null) {
                plant.setId(Long.parseLong(colId.getValue()));
            } else {
                plant.setId(-1L);
            }
            plant.setEnterprise(t);
            plant.setLocation(colLocation.getValue());
            plant.setName(colName.getValue());
            list.add(plant);
        }
        return list;
    }

    Plant queryPlantById(final EntityManager em, final Plant plant) {
        return querySingleInstance(em.createQuery(
                "SELECT p FROM Plant p WHERE p.id = :pId",
                Plant.class).setParameter("pId", plant.getId()));
    }
}
