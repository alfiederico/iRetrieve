/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.domain;

/**
 *
 * @author Alfie
 */
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.geo.Point;
import org.springframework.data.solr.core.geo.GeoConverters;

@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "type_id")
    private int typeId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "type")
    @NotEmpty(message = "*Please provide the type")
    private String type;

    @Column(name = "subject")
    @NotEmpty(message = "*Please provide the subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    @NotEmpty(message = "*Please provide the date found/lost")
    private String date;

    @Column(name = "location")
    private String location;

    @Column(name = "settle_id")
    private int settleId;

    @Column(name = "place")
    @NotEmpty(message = "*Please provide the place it was lost")
    private String place;

    private Point point;

 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Point getPoint() {
        String _location = this.getLocation();
        if (this.getLocation() == null) {
            _location = "-1,-1";
        }
        return GeoConverters.StringToPointConverter.INSTANCE.convert(_location);
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean hasLocation() {
        return (this.getLocation() != null);
    }

    /**
     * @return the place
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * @return the typeId
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the settleId
     */
    public int getSettleId() {
        return settleId;
    }

    /**
     * @param settleId the settleId to set
     */
    public void setSettleId(int settleId) {
        this.settleId = settleId;
    }

}
