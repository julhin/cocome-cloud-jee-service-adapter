package org.cocome.tradingsystem.inventory.data.plant.recipe;

import org.cocome.tradingsystem.inventory.data.enterprise.NameableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Represents an operation provided by an plant
 *
 * @author Rudolf Biczok
 */
@Entity
public class RecipeOperation implements Serializable, NameableEntity {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;

    private Collection<EntryPoint> inputEntryPoint;
    private Collection<EntryPoint> outputEntryPoint;

    /**
     * @return A unique identifier of this Plant.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }

    /**
     * @param id a unique identifier of this Plant
     */
    @Override
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Returns the name of the Plant.
     *
     * @return Plant name.
     */
    @Override
    @NotNull
    @Basic
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name of the Plant
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return all material classes that are required for operation execution
     */
    @JoinTable(
            name = "RECIPESTEP_INPUTENTRYPOINT",
            joinColumns = {
                    @JoinColumn(name = "ENTRYPOINT_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "RECIPESTEP_ID"),
            })
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<EntryPoint> getInputEntryPoint() {
        return inputEntryPoint;
    }

    /**
     * @param inputMaterial all material classes that are required for operation execution
     */
    public void setInputEntryPoint(Collection<EntryPoint> inputMaterial) {
        this.inputEntryPoint = inputMaterial;
    }

    /**
     * @return all material classes that results after the operation execution
     */
    @JoinTable(
            name = "RECIPESTEP_OUTPUTENTRYPOINT",
            joinColumns = {
                    @JoinColumn(name = "ENTRYPOINT_ID"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "RECIPESTEP_ID"),
            })
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Collection<EntryPoint> getOutputEntryPoint() {
        return outputEntryPoint;
    }

    /**
     * @param outputMaterial all material classes that results after the operation execution
     */
    public void setOutputEntryPoint(Collection<EntryPoint> outputMaterial) {
        this.outputEntryPoint = outputMaterial;
    }
}