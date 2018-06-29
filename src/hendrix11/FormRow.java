/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hendrix11;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.StringJoiner;

/**
 *
 */
public class FormRow {
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DecimalFormat coinFormat = new DecimalFormat("0.00000000");
    public static final DecimalFormat dollarFormat = new DecimalFormat("$0.00");
    
    private double amount;
    private LocalDateTime aquired;
    private LocalDateTime sold;
    private double proceeds;
    private double cost;

    public FormRow(double amount, LocalDateTime aquired, LocalDateTime sold, double proceeds, double cost) {
        this.amount = amount;
        this.aquired = aquired;
        this.sold = sold;
        this.proceeds = proceeds;
        this.cost = cost;
    }

    public FormRow(double amount, LocalDateTime sold, double proceeds, double cost) {
        this(amount, null, sold, proceeds, cost);
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getAquired() {
        return aquired;
    }    

    public LocalDateTime getSold() {
        return sold;
    }

    public long getProceeds() {
        return Math.round(proceeds);
    }

    public long getCost() {
        return Math.round(cost);
    }
    
    public boolean isWash() {
        return proceeds < cost;
    }
    
    public long getAdjustment() {
        if(isWash()) {
            return getCost() - getProceeds();
        } else {
            return 0;
        }
    }
    
    public long gain() {
        return getProceeds() - getCost() + getAdjustment();
    }
    
    @Override
    public String toString() {       
        StringJoiner sj = new StringJoiner(",");
        sj.add(coinFormat.format(amount) + " " + Form.what)
                .add(aquired != null ? dateFormat.format(aquired) : "various")
                .add(dateFormat.format(sold))
                .add(dollarFormat.format(getProceeds()))
                .add(dollarFormat.format(getCost()))
                .add(isWash() ? "W" : "")
                .add(isWash() ? dollarFormat.format(getAdjustment()) : "")
                .add(dollarFormat.format(gain()));
        
        return sj.toString();
    }
}
