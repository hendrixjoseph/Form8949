/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hendrix11;

import java.util.Date;

/**
 *
 */
public class NormalTransaction implements Transaction {
    private Date date;
    private double amount;
    private double price;

    public NormalTransaction(Date date, double amount, double price) {
        this.date = date;
        this.amount = amount;
        this.price = price;
        
        if(price < 0) {
            throw new RuntimeException("Price can't be negative!");
        }
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public double getPrice() {
        return price;
    }
    
    @Override
    public double getCost() {
        return price * amount;
    }
    
    @Override
    public boolean isBuy() {
        return amount > 0;
    }
    
    @Override
    public boolean isSell() {
        return amount < 0;
    }
}
