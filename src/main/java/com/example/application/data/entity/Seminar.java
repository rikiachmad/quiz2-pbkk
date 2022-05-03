package com.example.application.data.entity;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.*;
import java.time.*;
import java.util.Date;

@Entity
public class Seminar extends  AbstractEntity{

    @NotEmpty(message= "this field can't be empty")
    private String name = "";

    @NotEmpty(message= "this field can't be empty")
    private String address = "";

    private String description = "";
    private LocalDateTime date;

    @NotNull(message= "this field can't be empty")
    private Integer price = 0;

    @NotEmpty(message= "this field can't be empty")
    private String status = "Offline";

    @NotEmpty(message= "this field can't be empty")
    private String link;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public Integer getPrice(){
        return price;
    }

    public void setPrice(Integer price){
        this.price = price;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCustom() {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        DateFormat dateFormat = new SimpleDateFormat("dd");
        String strDate = dateFormat.format(out);

        LocalDateTime temp = getDate();
        Month month = temp.getMonth();
        int year = temp.getYear();

        String res = strDate + " " + month + " " + year;
        return res;
    }

    public String getPriceCustom(){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format((double) getPrice());
    }

    public String getTime(){
        String ans = "";
        LocalDateTime local = getDate();
        int hour = local.getHour();
        if(hour < 10){
            ans += "0";
        }
        ans+=hour + ":";
        int minute = local.getMinute();
        if(minute < 10){
            ans += 0;
        }
        ans+=minute;
        return ans;
    }
}
