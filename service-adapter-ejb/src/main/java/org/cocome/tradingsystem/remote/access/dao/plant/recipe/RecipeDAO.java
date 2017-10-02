package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.CustomProduct;
import org.cocome.tradingsystem.inventory.data.plant.recipe.EntryPointInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.ParameterInteraction;
import org.cocome.tradingsystem.inventory.data.plant.recipe.PlantOperation;
import org.cocome.tradingsystem.inventory.data.plant.recipe.Recipe;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Recipe}
 *
 * @author Rudolf Biczok
 */
@Stateless
@LocalBean
public class RecipeDAO extends AbstractDAO<Recipe> {

    private static final String ID_COL = Recipe.class.getSimpleName() + "Id";
    private static final String CUSTOM_PRODUCT_ID_COL = CustomProduct.class.getSimpleName() + "Id";
    private static final String OP_ID_COL = PlantOperation.class.getSimpleName() + "Id";
    private static final String EP_ID_COL = EntryPointInteraction.class.getSimpleName() + "Id";
    private static final String PARAM_ID_COL = ParameterInteraction.class.getSimpleName() + "Id";

    @Override
    public Class<Recipe> getEntityType() {
        return Recipe.class;
    }

    @Override
    public Table<String> toTable(final List<Recipe> list) {
        final Table<String> table = new Table<>();
        table.addHeader(ID_COL, CUSTOM_PRODUCT_ID_COL, OP_ID_COL, EP_ID_COL, PARAM_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getCustomProduct().getId()));
            table.set(i, 2, joinValues(list.get(i).getOperations()));
            table.set(i, 3, joinValues(list.get(i).getEntryPointInteractions()));
            table.set(i, 4, joinValues(list.get(i).getParameterInteractions()));
        }
        return table;
    }

    @Override
    public List<Recipe> fromTable(final EntityManager em,
                                  final Table<String> table,
                                  final Notification notification,
                                  final String sourceOperation) {
        final int len = table.size();
        final List<Recipe> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Column<String> colId = table.getColumnByName(i, ID_COL);
            final Column<String> colCustomProductId = table.getColumnByName(i, CUSTOM_PRODUCT_ID_COL);
            final Column<String> colOpId = table.getColumnByName(i, OP_ID_COL);
            final Column<String> colEpId = table.getColumnByName(i, EP_ID_COL);
            final Column<String> colParamId = table.getColumnByName(i, PARAM_ID_COL);

            final Recipe recipe = getOrCreateReferencedEntity(Recipe.class, colId, em);
            try {
                recipe.setCustomProduct(getReferencedEntity(
                        CustomProduct.class,
                        colCustomProductId,
                        em));
                recipe.setOperations(getReferencedEntities(
                        PlantOperation.class,
                        colOpId,
                        em));
                recipe.setEntryPointInteractions(getReferencedEntities(
                        EntryPointInteraction.class,
                        colEpId,
                        em));
                recipe.setParameterInteractions(getReferencedEntities(
                        ParameterInteraction.class,
                        colParamId,
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(recipe);
        }
        return list;
    }
}
