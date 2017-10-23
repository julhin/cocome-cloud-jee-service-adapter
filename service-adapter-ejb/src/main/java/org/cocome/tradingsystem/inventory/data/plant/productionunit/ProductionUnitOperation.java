package org.cocome.tradingsystem.inventory.data.plant.productionunit;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;
import org.cocome.tradingsystem.inventory.data.plant.expression.Expression;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Represents an atomic operation on a production unit
 *
 * @author Rudolf Biczok
 */
@Entity
public class ProductionUnitOperation extends Expression implements NameableEntity {

    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private String operationId;

    private long expectedExecutionTime;

    private ProductionUnitClass productionUnitClass;

    /**
     * Gets identifier value
     *
     * @return The identifier value.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    /**
     * Sets identifier.
     *
     * @param id Identifier value.
     */
    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    @Basic
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return The operation id unique to the production plant
     */
    @NotNull
    @Basic
    public String getOperationId() {
        return operationId;
    }

    /**
     * @param operationId The operation id unique to the production plant
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return the expected execution time in milliseconds
     */
    @Min(1)
    @Basic
    public long getExpectedExecutionTime() {
        return expectedExecutionTime;
    }

    /**
     * @param expectedExecutionTime the in milliseconds
     */
    public void setExpectedExecutionTime(long expectedExecutionTime) {
        this.expectedExecutionTime = expectedExecutionTime;
    }

    /**
     * @return the associated {@link ProductionUnitClass}
     */
    @NotNull
    @ManyToOne
    public ProductionUnitClass getProductionUnitClass() {
        return productionUnitClass;
    }

    /**
     * @param productionUnitClass the associated production unit class
     */
    public void setProductionUnitClass(ProductionUnitClass productionUnitClass) {
        this.productionUnitClass = productionUnitClass;
    }
}
