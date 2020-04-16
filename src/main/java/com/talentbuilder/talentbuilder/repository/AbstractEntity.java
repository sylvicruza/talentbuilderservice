package com.talentbuilder.talentbuilder.repository;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentbuilder.talentbuilder.model.SerializableEntity;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The {@code AbstractEntity} abstract class is a superclass for all entities.
 * Entities that are subclasses of this {@code AbstractEntity} inherits the variables and methods defined here
 *
 * @author Showboy
 */


@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractEntity implements Serializable, SerializableEntity<AbstractEntity> {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    protected Long id;

    @Version
    protected int version;

    protected String delFlag = "N";

    protected Date deletedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEntity other = (AbstractEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }



    @Override
    public String serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(this);
        return data;
    }

    @Override
    public void deserialize(String data) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        AbstractEntity readValue = mapper.readValue(data, this.getClass());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(readValue, this);

    }

    @JsonIgnore
    public List<String> getDefaultSearchFields(){
        return new ArrayList<String>();
    };

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", version=" + version +
                        ", delFlag='" + delFlag + '\'' +
                        ", deletedOn=" + deletedOn +
                        '}';
    }



}
