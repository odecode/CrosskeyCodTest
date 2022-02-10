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
            String toPrint = app.printThis(row, app);
            if(toPrint.length() > 0){
                System.out.println(toPrint);

            }

           
        }
    }

    protected String[] parseLine(String line){
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
        for(int i = 0; i < data.length; i++){
            if(data[i] == null || data[i].length() < 1){
                data[i] = "Unknown";
            }
        }
        return data;


    }

    protected String printThis(String[] dataRow,App app){
        String name = dataRow[0];
            if(name.contains("\"")){
                name = name.replace("\"", "");

            }
            String loanValue = dataRow[1];
            String interestRate = dataRow[2];
            String paymentTime = dataRow[3];
            boolean invalidString = false;
            for (String s : dataRow) {
                if(s == null || s.length() < 1){
                    invalidString = true;
                }
            }
            if(invalidString) return "";

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
            String result = name+" wants to borrow "+ loanValue + " eur for a period of "+ paymentTime+ " years and pay "+finalstr+" eur each month";
            
            return result;
    }


    protected double convertToNum(String num){
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

    protected double calculateMontlyPayment(double interest, double loanVal, double payTime){
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
