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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Form {

    public static String what;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private List<Transaction> transactions = new ArrayList<>();
    private List<FormRow> formRows;

    public Form(String what) throws IOException {
        this(Paths.get(what + ".csv"));
        Form.what = what;
    }

    private Form(Path path) throws IOException {
        Files.readAllLines(path).stream().skip(1).forEach(this::readLine);
    }

    private void readLine(String line) {
        try {
            String[] items = line.split(",");

            Date date = dateFormat.parse(items[0]);
            double amount = Double.parseDouble(items[1]);
            double price = Double.parseDouble(items[2]);

            NormalTransaction t = new NormalTransaction(date, amount, price);
            transactions.add(t);
        } catch (ParseException ex) {
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

    private double total(ToDoubleFunction<FormRow> df) {
        return formRows.stream().mapToDouble(df).sum();
    }
    
    private long total(ToLongFunction<FormRow> lf) {
        return formRows.stream().mapToLong(lf).sum();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("amount,date aquired,date sold,proceeds,cost,code,adjustments,gain");

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
