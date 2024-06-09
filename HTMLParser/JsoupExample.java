import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

public class JsoupExample {
    public static void main(String[] args) {
        int mode = Integer.valueOf(args[0]);
        String fileName = "data.csv";
//I need to determine whether it is the first day to catch the stock price
        if(mode == 0){ //catch some information from HTML to data.csv   
            try { 
                String url = "https://pd2-hw3.netdb.csie.ncku.edu.tw/";
                
                Document doc = Jsoup.connect(url).get();
                // Get the title of the webpage
                String title = doc.title();
                //Get the elementv of the webpage
                Element table = doc.selectFirst("table");
                Elements rows = table.select("tr");
                Elements company = rows.select("th");
                Elements stockPrice = rows.select("td"); 
                File file = new File(fileName);
                boolean flag = file.exists();
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName ,true))){
                    if(flag == false){
                        bw.append(company.text());
                        bw.newLine();
                        bw.append(stockPrice.text());
                        bw.newLine();
                    }
                    else{
                        bw.append(stockPrice.text());
                        bw.newLine();
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        else if (mode == 1) { //analyze these information
            int taskNum = Integer.valueOf(args[1]);
            String stockName = "";
            int start = 0 , end =0 ;
            if(taskNum != 0){
                stockName = args[2];
                start = Integer.valueOf(args[3]);
                end = Integer.valueOf(args[4]);
            }
            Task t = new Task();
            try{
                FileReader fileReader = new FileReader("data.csv");
                String allString = Files.readString(Paths.get("data.csv"));
                //System.out.println(allString);
                t.task(taskNum , stockName , start , end , allString);
                
            }
            catch(IOException e){
                e.printStackTrace();
            }
            
        }
    }
}
//java HtmlParser {mode} {task} {stock} {start} {end}
class Task{
    public static void task(int taskNum , String stockName , int start , int end , String allString){
        String arr[] = allString.split("\n");
        String fileName = "output.csv";
        String[] companyName = arr[0].split(",");
        if(taskNum == 0){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
                bw.write(allString);
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(taskNum == 1){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName , true))){
                DecimalFormat df = new DecimalFormat("##.00");
                int companyPlace =0; 
                bw.append(stockName+",");
                bw.append(String.valueOf(start)+",");
                bw.append(String.valueOf(end));
                bw.newLine();
                for(int i=0 ; i<companyName.length ; i++){//find the company position
                    if(stockName.equals(companyName[i])){
                        companyPlace = i;
                        break;
                    }
                }

                for(int i=start ; i<=end-4;i++){// start to calculate the data
                    double sum = 0.0;
                    double average = 0.0;  
                    for(int j=0 ; j<5 ;j++){
                        String[] stockPriceArray = arr[i+j].split(",");
                        sum = sum + Double.valueOf(stockPriceArray[companyPlace]);
                        System.out.println(i+j);
                    }
                    double finalSum =0.0;
                    finalSum = sum/5;
                    average = Calculate.myRound(finalSum*100)/100.0;
                    //System.out.println(average);
                    if(i != end-4){
                        bw.append(String.valueOf(average)+",");
                    }
                    else{
                        bw.append(String.valueOf(average));
                        bw.newLine();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(taskNum ==2){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName , true))){
                bw.append(stockName+",");
                bw.append(String.valueOf(start)+",");
                bw.append(String.valueOf(end));
                bw.newLine();
                int num = end-start+1;
                int companyPlace = 0;
                List<String> ls = new ArrayList<>();
                for(int i=0 ; i<companyName.length ; i++){
                    if(stockName.equals(companyName[i])){
                        companyPlace = i;
                        
                        break;
                    }
                }
                //System.out.println(companyPlace);
                double sum =0.0;
                
                for(int i = start ; i <=end ; i++){
                    String[] stockPriceArray = arr[i].split(",");
                    ls.add(stockPriceArray[companyPlace]);
                    sum = sum+Double.valueOf(stockPriceArray[companyPlace]);
                }
                double average = sum/num;
                double[] priceArray = new double[ls.size()];
                int a=0; 
                for(String str : ls){
                    priceArray[a] = Double.valueOf(str);
                    a++;
                }
                Calculate cl = new Calculate();
                double input = cl.standardDeviation(priceArray , average , num);
                bw.append(String.valueOf(input));
                bw.newLine();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(taskNum == 3){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))){
                Map<String , List<Double>> sites = Readstock.read(allString);
                int x=0;  // find the space of the company
                int num = end-start +1;    
                double[] compareStandardDeviation = new double[companyName.length];
                double[] eachStockPrice = new double[num];
                String[] name = new String[companyName.length];
                for(Map.Entry<String , List<Double>> entry : sites.entrySet()){
                    name[x] = entry.getKey();
                    int a=0;
                    double sum =0.0;
                    double average =0.0;
                    List<Double> value = entry.getValue();
                    for(int i=(start-1) ; i<end ;i++){
                        sum = sum +value.get(i);
                        eachStockPrice[a] = value.get(i);
                        a++;
                    }
                    average = sum/num;
                    compareStandardDeviation[x] = Calculate.standardDeviation(eachStockPrice , average, num);
                    x++;
                }
                double top1 =0.0; int numtop1 = 0;
                double top2 =0.0; int numtop2 = 0;
                double top3 = 0.0; int numtop3 =0;
                
                for(double db : compareStandardDeviation){
                    if(db > top1){
                        top1 = db;
                    }
                }
                for(int i=0 ; i<compareStandardDeviation.length ; i++){
                    if(top1 == compareStandardDeviation[i]){
                        compareStandardDeviation[i] = -1.0;
                        numtop1 = i;
                        break;
                    }
                }
                for(double db : compareStandardDeviation){
                    if(db > top2){
                        top2 = db;
                    }
                }
                for(int i=0 ; i<compareStandardDeviation.length ; i++){
                    if(top2 == compareStandardDeviation[i]){
                        compareStandardDeviation[i] = -1.0;
                        numtop2 = i;
                        break;
                    }
                }
                for(double db : compareStandardDeviation){
                    if(db > top3){
                        top3 = db;
                    }
                }
                for(int i=0 ; i<compareStandardDeviation.length ; i++){
                    if(top3 == compareStandardDeviation[i]){
                        compareStandardDeviation[i] = -1.0;
                        numtop3 =i;
                        break;
                    }
                }
                String first = String.valueOf(top1);
                if(first.matches(".*\\.0+$")){
                    first = first.replaceAll("\\.0+$", "");
                }
                String second = String.valueOf(top2);
                if(second.matches(".*\\.0+$")){
                    second = second.replaceAll("\\.0+$", "");
                }
                String third = String.valueOf(top3);
                if(third.matches(".*\\.0+$")){
                    third = third.replaceAll("\\.0+$", "");
                }
                bw.append(name[numtop1]+",").append(name[numtop2]+",").append(name[numtop3]+",").append(String.valueOf(start)+",").append(String.valueOf(end));
                bw.newLine();
                bw.append(first+",").append(second+",").append(third);
                bw.newLine();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        else if(taskNum ==4){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                int num = end-start+1;
                int companyPlace = 0;
                List<String> ls = new ArrayList<>();
                for(int i=0 ; i<companyName.length ; i++){
                    if(stockName.equals(companyName[i])){
                        companyPlace = i;
                        
                        break;
                    }
                }
                for(int i = start ; i <=end ; i++){
                    String[] stockPriceArray = arr[i].split(",");
                    ls.add(stockPriceArray[companyPlace]);
                }
                double b0 = 0.0; // intercept
                double b1 = 0.0; // slope
                double timeSum = 0.0;
                for(int i = start ; i<=end ; i++){
                    timeSum = timeSum+i;
                }
                double timeAvg = timeSum/Double.valueOf(num);
                double sum = 0.0;
                for(String str : ls){
                    sum = sum +Double.valueOf(str);
                }
                
                double avg = sum/Double.valueOf(num);
                double numerator =0.0; // up
                double denominator = 0.0; //down
                for(int i=0 ; i<num ; i++){
                    int day = start+i;
                    numerator = numerator + (day-timeAvg)*(Double.valueOf(ls.get(i))-avg);
                    denominator = denominator + Math.pow((day - timeAvg), 2);
                }
                b1=numerator/denominator;
                b0 = avg - (b1*timeAvg);
                b1 = Calculate.myRound(b1*100)/100.0;
                b0 = Calculate.myRound(b0*100)/100.0;
                String finalb1 = String.valueOf(b1);
                String finalb0 = String.valueOf(b0);
                if(finalb1.matches(".*\\.0+$")){
                    finalb1 = finalb1.replaceAll("\\.0+$", "");
                }
                if(finalb0.matches(".*\\.0+$")){
                    finalb0 = finalb0.replaceAll("\\.0+$", "");
                }
                bw.append(stockName+",").append(String.valueOf(start)+",").append(String.valueOf(end));
                bw.newLine();
                bw.append(finalb1+",").append(finalb0);
                bw.newLine();
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

class Readstock{
    public static Map<String , List<Double>> read(String allString){
        Map<String , List<Double>> sites = new HashMap<>();
        String[] array = allString.split("\n");
        String[] companyName = array[0].split(",");
        for(String str : companyName){
            List<Double> ls = new ArrayList<>();
            sites.put(str, ls);
        }
        
        for(Map.Entry<String , List<Double>> entry : sites.entrySet()){
            for(int i=0 ; i<companyName.length ; i++){
                if(entry.getKey() == companyName[i]){
                    for(int j=1 ; j<array.length ; j++){
                        String[] data = array[j].split(",");
                        sites.get(companyName[i]).add(Double.parseDouble(data[i]));
                    }
                }
            }
        }

        return sites;
    }

    

}

class Calculate{
    public static double  standardDeviation(double[] x , double avg , int num){ 
        double finalAns = 0.0 ;
        double standardDeviationSum =0.0;
        double unsqrtStandardDeviation = 0.0;
        for(int i=0 ; i<x.length ;i++){
            double count = x[i] - avg;
            
            standardDeviationSum = standardDeviationSum + Math.pow(count,2);
            
        }
        unsqrtStandardDeviation = standardDeviationSum/(num-1);
        finalAns = Math.sqrt(unsqrtStandardDeviation);
        finalAns = Calculate.myRound(finalAns*100)/100.0;
        return finalAns ;
    }

    public static double myRound(double num){
        double finalAns = 0.0;
        int a = (int)finalAns;
        if((finalAns-a)>=0.5){
            a = a+1;
            return (double)a;
        }
        else{
            return (double)a;
        }
    
    }

}
