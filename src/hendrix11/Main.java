/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hendrix11;

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 */
public class Main {
    public static void main(String[] string) throws IOException {
        String what = "ltc";
        
        Form form = new Form(what, 2017, 2018);
        form.generate();
        //form.condense();
        String out = form.toStringForYear(2018);
        System.out.println(out);
        PrintWriter pw = new PrintWriter(what + ".form8949.csv");
        pw.print(out);
        pw.close();
        
    }
    

}
