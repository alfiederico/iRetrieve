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
import java.awt.Image;
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
import javax.persistence.Lob;
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
@Table(name = "hotspot")
public class Hotspot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "*Please provide the name")
    private String name;

   

    @Column(name = "location")
    @NotEmpty(message = "*Please provide the location")
    private String location;

    @Column(name = "date_created")
    private String date_created;

    @Column(name = "last_updated")
    private String last_updated;

    @Column(name = "place")
    @NotEmpty(message = "*Please provide the place it was lost")
    private String place;

    private Point point;
    
    @Column(name = "contact")
    @NotEmpty(message = "*Please provide contact details")
    private String contact;
    
    
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getType() {
        return getName();
    }

    public void setType(String type) {
        this.setName(type);
    }

   

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String datecreated) {
        this.date_created = datecreated;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    

}
