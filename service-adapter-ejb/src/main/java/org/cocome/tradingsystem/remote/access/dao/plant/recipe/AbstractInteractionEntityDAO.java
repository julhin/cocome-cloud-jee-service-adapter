package org.cocome.tradingsystem.remote.access.dao.plant.recipe;

import de.kit.ipd.java.utils.framework.table.Column;
import de.kit.ipd.java.utils.framework.table.Table;
import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.plant.recipe.InteractionEntity;
import org.cocome.tradingsystem.remote.access.Notification;
import org.cocome.tradingsystem.remote.access.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract DAO for {@link InteractionEntity}
 *
 * @author Rudolf Biczok
 */
public abstract class AbstractInteractionEntityDAO<FromType extends NameableEntity,
        ToType extends NameableEntity,
        T extends InteractionEntity<FromType, ToType>>
        extends AbstractDAO<T> {

    private static final String ID_COL = "Id";
    private static final String TO_ID_COL = "ToId";
    private static final String FROM_ID_COL = "FromId";

    @Override
    public Table<String> toTable(final List<T> list) {
        final Table<String> table = new Table<>();
        final String prefix = this.getEntityType().getSimpleName();
        table.addHeader(
                prefix + ID_COL,
                prefix + TO_ID_COL,
                prefix + FROM_ID_COL);
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            table.set(i, 0, String.valueOf(list.get(i).getId()));
            table.set(i, 1, String.valueOf(list.get(i).getTo().getId()));
            table.set(i, 2, String.valueOf(list.get(i).getFrom().getId()));
        }
        return table;
    }

    @Override
    public List<T> fromTable(final EntityManager em,
                             final Table<String> table,
                             final Notification notification,
                             final String sourceOperation) {
        final int len = table.size();
        final List<T> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final String prefix = this.getEntityType().getSimpleName();
            final Column<String> colId = table.getColumnByName(i, prefix + ID_COL);
            final Column<String> colToId = table.getColumnByName(i, prefix + TO_ID_COL);
            final Column<String> colFromId = table.getColumnByName(i, prefix + FROM_ID_COL);


            final T entryPoint = getOrCreateReferencedEntity(this.getEntityType(), colId, em);
            try {
                entryPoint.setTo(getReferencedEntity(entryPoint.getToClass(),
                        Long.valueOf(colToId.getValue()),
                        em));
                entryPoint.setFrom(getReferencedEntity(entryPoint.getFromClass(),
                        Long.valueOf(colFromId.getValue()),
                        em));
            } catch (final EntityNotFoundException e) {
                notification.addNotification(
                        sourceOperation,
                        Notification.FAILED,
                        String.format("%s: Referenced entity not available: %s", getClass().getSimpleName(),
                                e.getMessage()));
                continue;
            }

            list.add(entryPoint);
        }
        return list;
    }
}
