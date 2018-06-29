/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hendrix11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import hendrix11.transactions.NormalTransaction;
import hendrix11.transactions.Transaction;

/**
 *
 */
public class Form {

    public static String what;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    private List<Transaction> transactions = new ArrayList<>();
    private List<FormRow> formRows;
    
    public Form(String what, int... years) throws IOException {    	
    	for(int year : years) {
    		init(what + "." + year + ".csv");
    	}
    	
    	Form.what = what;
    }

    public Form(String what) throws IOException {
        init(what + ".csv");
        Form.what = what;
    }
    
    private void init(String path) throws IOException {
    	init(Paths.get(path));
    }

    private void init(Path path) throws IOException {
        Files.readAllLines(path).stream().skip(1).forEach(this::readLine);
    }

    private void readLine(String line) {
        try {
            String[] items = line.split(",");

            LocalDateTime date = LocalDateTime.parse(items[0], dateFormat);
            double amount = Double.parseDouble(items[1]);
            double price = Double.parseDouble(items[2]);

            Transaction t = new NormalTransaction(date, amount, price);
            transactions.add(t);
        } catch (DateTimeParseException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generate() {
        Generator g = new Generator(transactions);
        formRows = g.generate();
    }

    public void condense() {
        Condenser condenser = new Condenser(formRows);
        formRows = condenser.condense();
    }

    public double totalAmount() {
        return total(FormRow::getAmount);
    }

    public long totalProceeds() {
        return total(FormRow::getProceeds);
    }

    public long totalCost() {
        return total(FormRow::getCost);
    }

    public long totalAdjustment() {
        return total(FormRow::getAdjustment);
    }

    public long totalGain() {
        return total(FormRow::gain);
    }
    
    public double totalAmount(int year) {
        return total(FormRow::getAmount, year);
    }

    public long totalProceeds(int year) {
        return total(FormRow::getProceeds, year);
    }

    public long totalCost(int year) {
        return total(FormRow::getCost, year);
    }

    public long totalAdjustment(int year) {
        return total(FormRow::getAdjustment, year);
    }

    public long totalGain(int year) {
        return total(FormRow::gain, year);
    }

    private double total(ToDoubleFunction<FormRow> df) {
        return formRows.stream().mapToDouble(df).sum();
    }
    
    private long total(ToLongFunction<FormRow> lf) {
        return formRows.stream().mapToLong(lf).sum();
    }
    
    private double total(ToDoubleFunction<FormRow> df, int year) {
        return formRows.stream().filter(row -> row.getSold().getYear() == year).mapToDouble(df).sum();
    }
    
    private long total(ToLongFunction<FormRow> lf, int year) {
        return formRows.stream().filter(row -> row.getSold().getYear() == year).mapToLong(lf).sum();
    }
    
    private String header() {
    	return "amount,date aquired,date sold,proceeds,cost,code,adjustments,gain";
    }
    
    public String toStringForYear(int year) {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(header());
        
        formRows.stream().filter(row -> row.getSold().getYear() == year)
        	.forEach((row) -> {
                if (row.getProceeds() > 0 && row.getCost() > 0) {
                    sj.add(row.toString());
                }
            });
        
        StringJoiner comma = new StringJoiner(",");
        comma.add(FormRow.coinFormat.format(totalAmount(year)) + " " + what)
                .add(",")
                .add(FormRow.dollarFormat.format(totalProceeds(year)))
                .add(FormRow.dollarFormat.format(totalCost(year)))
                .add("")
                .add(FormRow.dollarFormat.format(totalAdjustment(year)))
                .add(FormRow.dollarFormat.format(totalGain(year)));

        sj.add(comma.toString());
        
        return sj.toString();
    }
    
    

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(header());

        formRows.forEach((row) -> {
            if (row.getProceeds() > 0 && row.getCost() > 0) {
                sj.add(row.toString());
            }
        });

        StringJoiner comma = new StringJoiner(",");
        comma.add(FormRow.coinFormat.format(totalAmount()) + " " + what)
                .add(",")
                .add(FormRow.dollarFormat.format(totalProceeds()))
                .add(FormRow.dollarFormat.format(totalCost()))
                .add("")
                .add(FormRow.dollarFormat.format(totalAdjustment()))
                .add(FormRow.dollarFormat.format(totalGain()));

        sj.add(comma.toString());

        return sj.toString();
    }
}
