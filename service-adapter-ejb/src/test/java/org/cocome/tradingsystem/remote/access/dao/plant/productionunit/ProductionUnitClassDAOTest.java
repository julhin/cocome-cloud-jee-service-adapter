package org.cocome.tradingsystem.remote.access.dao.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.TradingEnterprise;
import org.cocome.tradingsystem.inventory.data.plant.Plant;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitClass;
import org.cocome.tradingsystem.inventory.data.plant.productionunit.ProductionUnitOperation;
import org.cocome.tradingsystem.remote.access.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProductionUnitClassDAOTest {

    private ProductionUnitClassDAO pucDAO = TestUtils.injectFakeEJB(ProductionUnitClassDAO.class);

    @Test
    public void convertToTableContent() throws Exception {
        final TradingEnterprise enterprise = new TradingEnterprise();
        enterprise.setName("CoCoME SE");
        final EntityManager em = TestUtils.TEST_EMF.createEntityManager();
        final EntityTransaction tx = em.getTransaction();

        final ProductionUnitClass puc = new ProductionUnitClass();
        puc.setEnterprise(enterprise);
        puc.setName("xPPU v 0.1 Beta");

        tx.begin();
        em.persist(puc);
        tx.commit();

        final List<ProductionUnitClass> queryedInstances = TestUtils.TEST_EMF.createEntityManager()
                .createQuery("SELECT puc from ProductionUnitClass puc WHERE puc.enterprise.id = "
                        + enterprise.getId(), ProductionUnitClass.class).getResultList();

        final String expectedTableContent = String.format("TradingEnterpriseId;ProductionUnitClassId;ProductionUnitClassName\n"
        + "%d;%d;xPPU v 0.1 Beta", enterprise.getId(), puc.getId());

        Assert.assertNotNull(queryedInstances);
        Assert.assertFalse(queryedInstances.isEmpty());
        Assert.assertEquals(expectedTableContent, TestUtils.toCSV(pucDAO.toTable(queryedInstances)));
    }
}
