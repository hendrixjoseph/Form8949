/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hendrix11;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Condenser {
    private List<FormRow> formRows;
    private List<FormRow> condensedRows;
    private Map<LocalDate, List<FormRow>> map = new LinkedHashMap<>();

    public Condenser(List<FormRow> formRows) {
        this.formRows = formRows;
    }
    
    private void process(LocalDate date) {
        double[] amount = {0, 0};
        double[] cost = {0, 0};
        double[] proceeds = {0, 0};
        LocalDateTime[] sold = {null, null};
        LocalDateTime[] bought = {null, null};
        
        boolean[] first = {true, true};

        map.get(date).forEach(row -> {            
            if (row.isWash()) {
                amount[0] += row.getAmount();
                cost[0] += row.getCost();
                proceeds[0] += row.getProceeds();
                sold[0] = row.getSold();
                
                if(first[0]) {
                    bought[0] = row.getAquired();
                    first[0] = false;
                } else {
                    bought[0] = sameDate(bought[0], row.getAquired()) ? bought[0] : null;
                }
            } else {
                amount[1] += row.getAmount();
                cost[1] += row.getCost();
                proceeds[1] += row.getProceeds();
                sold[1] = row.getSold();
                if(first[1]) {
                    bought[1] = row.getAquired();
                    first[1] = false;
                } else {
                    bought[1] = sameDate(bought[1], row.getAquired()) ? bought[1] : null;
                }
            }
        });

        if (amount[0] > 0) {
            condensedRows.add(new FormRow(amount[0], bought[0], sold[0], proceeds[0], cost[0]));
        }

        if (amount[1] > 0) {
            condensedRows.add(new FormRow(amount[1], bought[1], sold[1], proceeds[1], cost[1]));
        }
    }
    
    private boolean sameDate(LocalDateTime date1, LocalDateTime date2) {
        if(date1 == null || date2 == null) {
            return false;
        } else {
            return date1.toLocalDate().equals(date2.toLocalDate());
        }
    }
           
    private void process(FormRow row) {        
        LocalDate date = row.getSold().toLocalDate();

        if (map.get(date) == null) {
            map.put(date, new ArrayList<>());
        }

        List<FormRow> get = map.get(date);
        get.add(row);
    }
    
    public List<FormRow> condense() {
        condensedRows = new ArrayList<>();
        formRows.forEach(this::process);
        map.keySet().forEach(this::process);
        return condensedRows;
    }
}
