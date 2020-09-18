package org.why.myvhr.model;

import java.io.Serializable;
import java.util.Objects;

public class Nation implements Serializable {
    private Integer id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        org.why.myvhr.model.Nation nation = (org.why.myvhr.model.Nation) o;
        return Objects.equals(name, nation.name);
    }

    public Nation() {
    }

    public Nation(String name) {

        this.name = name;
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}