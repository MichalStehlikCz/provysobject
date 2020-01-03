package com.provys.provysobject.impl;

import com.provys.common.datatype.DtUid;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

/**
 * Class is common ancestor of object value builders. It can be used to either create completely new value object (using
 * build method) or create object that uses properties of original object, but applies changes to properties that has
 * been modified in builder (using buildFrom method)
 *
 * @param <V> is value type this builder is used for
 */
@SuppressWarnings("WeakerAccess") // library class, used as ancestor for objects in other packages
@XmlTransient
public abstract class ProvysObjectValueBuilder<B extends ProvysObjectValueBuilder<B, V>, V extends ProvysObjectValue> {

    @Nullable
    private DtUid id;

    /**
     * Constructor that creates clean copy of builder
     */
    public ProvysObjectValueBuilder() {}

    /**
     * Constructor that creates copy of builder with values set based on supplied value object
     *
     * @param value is value obejct values should be copied from
     */
    public ProvysObjectValueBuilder(V value) {
        this.id = value.getId();
    }

    /**
     * Constructor that creates clone of supplied builder
     *
     * @param value is original builder that is to be cloned
     */
    public ProvysObjectValueBuilder(B value) {
        this.id = value.getId();
    }

    protected abstract B self();

    /**
     * @return value of Id property
     */
    @Nullable
    public DtUid getId() {
        return id;
    }

    /**
     * Set value of Id property
     *
     * @param id is new value
     * @return self to support fluent build
     */
    public B setId(@Nullable DtUid id) {
        this.id = id;
        return self();
    }

    /**
     * Clone builder - create new builder with exactly same set of modified properties
     *
     * @return new builder with the same properties as this. Does not copy referenced objects
     */
    public abstract B copy();

    /**
     * Build new value object based on values in this builder. Does validation first. Defaults are used for properties
     * that has not been set.
     *
     * @return new value object
     */
    public abstract V build();

    /**
     * Apply values from source object on all fields that were not set in this builder. Should be overridden in each
     * child that adds new fields. Does not touch fields that has been already set.
     *
     * @param source is source object values should be taken from
     * @return self to support fluent build
     */
    public B apply(V source) {
        if (id == null) {
            setId(source.getId());
        }
        return self();
    }

    /**
     * Indicates if this builder would change source object if applied on it. Sort of weird equals against different
     * type of object. Comparison ignores fields that are not set
     *
     * @param source is value object to be compared to
     * @return true if application of this builder would change source object
     */
    public boolean notChanged(V source) {
        return ((id == null) || (source.getId().equals(id)));
    }

    /**
     * Build value object from builder, fill in missing values from supplied source object. Does not modify builder -
     * e.g. same builder can be repeatedly used on multiple value objects. Application rules are used even on Id of
     * supplied object - if builder had no Id, resulting value object has Id of source, but if builder already had Id,
     * new value object contains this Id and thus is not related to supplied source.
     *
     * @param source is source object, used to fill in missing values
     * @return value object with applied changes; note that this might be source object if no changes has been applied
     */
    public V buildFrom(V source) {
        if (notChanged(source)) {
            return source;
        }
        return copy().apply(source).build();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvysObjectValueBuilder<?, ?> that = (ProvysObjectValueBuilder<?, ?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProvysObjectValueBuilder{" +
                "id=" + id +
                '}';
    }
}
