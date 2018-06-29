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
public class WashTransaction implements Transaction {
    
    private double cost;

    public WashTransaction(double cost) {
        this.cost = cost;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public boolean isBuy() {
        return false;
    }

    @Override
    public boolean isSell() {
        return true;
    }

}
