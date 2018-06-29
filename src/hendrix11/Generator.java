/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hendrix11;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import hendrix11.transactions.NormalTransaction;
import hendrix11.transactions.Transaction;
import hendrix11.transactions.WashTransaction;

/**
 *
 */
public class Generator {
    
    private Stack<Transaction> lifo = new Stack<>();
    private List<FormRow> formRows;
    private List<Transaction> transactions;

    public Generator(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    private void process(Transaction transaction) {
        if(transaction.isBuy()) {
            lifo.push(transaction);
        } else if(transaction.isSell()) {
            double amount = transaction.getAmount();
            double cost = 0;
            LocalDateTime date = lifo.peek().getDate();
                
            while(amount < 0) {
                Transaction buy = lifo.pop();
                date = date != buy.getDate() ? null : date;
                amount += buy.getAmount();
                    
                if(amount <= 0) {
                    cost += buy.getCost();
                } else {
                    cost += buy.getPrice() * (buy.getAmount() - amount);
                    NormalTransaction newBuy = new NormalTransaction(buy.getDate(), amount, buy.getPrice());
                    lifo.push(newBuy);
                }               
            }
            
            if(-transaction.getCost() < cost) {
                while(lifo.peek().getAmount() == 0) {
                    Transaction wash = lifo.pop();
                    cost += wash.getCost();
                }
            }
                
            FormRow row = new FormRow(-transaction.getAmount(), date, transaction.getDate(), -transaction.getCost(), cost);
            formRows.add(row);
                
            if(row.isWash()) {
                Transaction wash = new WashTransaction(cost + transaction.getCost());
                lifo.push(wash);
            }
        }
    }
    
    public List<FormRow> generate() {
        formRows = new ArrayList<>();
        transactions.forEach(this::process);        
        return formRows;
    }
}
