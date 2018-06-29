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
public interface Transaction {

    double getAmount();

    double getCost();

    Date getDate();

    double getPrice();

    boolean isBuy();

    boolean isSell();
    
}
