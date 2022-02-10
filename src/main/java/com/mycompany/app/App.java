package com.mycompany.app;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, URISyntaxException
    {
        App app = new App();

        ArrayList<String[]> dataTable = new ArrayList<String[]>();
        Path path = Paths.get("prospects.txt");
        System.out.println(path);
        File dataFile = path.toFile();
        Scanner scanner = new Scanner(dataFile,"utf-8");
        scanner.nextLine();
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.length() >= 7){  // a line must be at least 7 characters long for us to consider it, minimum line might be: A,1,1,3
                String[] parsedLine = app.parseLine(line);
                dataTable.add(parsedLine);
            }
        }
        scanner.close();
       
        for (String[] row : dataTable) {

            String name = row[0];
            String loanValue = row[1];
            String interestRate = row[2];
            String paymentTime = row[3];
            double dloanValue = app.convertToNum(loanValue);
            double dinterestRate = app.convertToNum(interestRate);
            double dpaymentTime = app.convertToNum(paymentTime);
            double payPerMonth = app.calculateMontlyPayment(dinterestRate, dloanValue, dpaymentTime);
            String payPerMonthStr = Double.toString(payPerMonth);
            int dotindex = payPerMonthStr.indexOf(".");
            String finalstr = "";
            for(int i = 0; i < dotindex+3; i++){
                finalstr += payPerMonthStr.charAt(i);
            }

            System.out.println(name+" wants to borrow "+ loanValue + " eur for a period of "+ paymentTime+ " years and pay "+finalstr+" eur each month");
           
        }
    }

    private String[] parseLine(String line){
        String word = "";
        int wordindex = 0;
        String[] data = new String[4];
        boolean firstIsQuote = false;
        for(int index = 0; index < line.length(); index++){
            if(index == 0 && line.charAt(index) == '"'){
                firstIsQuote = true;
            }
            if(firstIsQuote){
                while(line.charAt(index+1) != '"'){
                    word += line.charAt(index+1);
                    index++;
                }
                firstIsQuote = false;
                // data[wordindex] = word;
                // wordindex++;
                // word = "";
                // index++;
            }
            else{
                while(index < line.length()){
                    if(line.charAt(index) == ',' ){
                        data[wordindex] = word;
                        wordindex++;
                        word = "";
                         
                    }
                    else if(index == line.length()-1 && line.charAt(index) !=','){
                        word += line.charAt(index);
                        if(wordindex < 4) data[wordindex] = word;
                        
                    }
                    else{
                        word += line.charAt(index);
                    }
                    index++;
                }
            }
        }

        return data;


    }

    private double convertToNum(String num){
        if(num.contains(".")){
            double result = Double.parseDouble(num);
            return result;
        }
        else{
            int res = Integer.parseInt(num);
            double result = res;
            return result;
        }
    }

    private double calculateMontlyPayment(double interest, double loanVal, double payTime){
        double numPayments = payTime*12;
        double monthlyInterest = (interest/100)/12;
        double partial = 1.0;
        for(int i = 0; i < numPayments; i++){
            partial *= (1+monthlyInterest);
        }
        double monthlyPayment = loanVal*(monthlyInterest*partial/((partial)-1));
        return monthlyPayment;
    }   

}
