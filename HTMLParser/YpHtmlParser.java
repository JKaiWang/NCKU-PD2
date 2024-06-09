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

class YpHtmlParser {
    public static void main(String[] args) {
        try {
            int mode = Integer.parseInt(args[0]);
            if(mode ==0 ){
                Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
                Element table = doc.selectFirst("table");
                Elements rows = table.select("tr");
                Elements companyName = rows.select("th");
                Elements stockPrice = rows.select("td");
                File file = new File("data.csv");
                boolean check = file.exists();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv"))){
                    if(check == false){
                        bw.write(companyName.text());
                        bw.write("\n");
                        bw.write(stockPrice.text());
                        bw.write("\n");
                    }
                    else{
                        bw.write(stockPrice.toString());
                        bw.write("\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if(mode == 1){
                try{
                    //FileReader filereader = new FileReader("data.csv");
                    String dataString  = Files.readString(Paths.get("data.csv"));
                    int task = Integer.parseInt(args[0]);
                    if(task == 0){
                        try (BufferedWriter bw  = new BufferedWriter(new FileWriter("output.csv" , true))){
                            String company = args[2];
                            int startDay = Integer.parseInt(args[3]);
                            int endDay = Integer.parseInt(args[4]);
                            bw.append(dataString);
                        
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if (task ==1 ){
                        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output.csv" , true))){
                            String company = args[2];
                            int startDay = Integer.parseInt(args[3]);
                            int endDay = Integer.parseInt(args[4]);
                            double average = 0.0 , sum = 0.0;
                            String[] arr = dataString.split("\n");
                            String[] companyName = dataString.split(",");
                            int companyNum = company.length();
                            int position =0;
                            bw.append(company+", ").append(String.valueOf(startDay)+",").append(String.valueOf(endDay)+",");
                            bw.newLine();
                            for(int i =0 ; i< companyNum ; i++){
                                if(companyName[i] == company){
                                    position = i;
                                }
                            }
                            for(int i=startDay ; i<=endDay-4 ; i++){
                                for(int j=0 ; j<5 ; j++){
                                    String[] dailyPrice = arr[i+j].split(",");
                                    sum = sum + Double.parseDouble(dailyPrice[companyNum]);
                                }
                                average = sum/5;
                                average = Math.round(average*100)/100.0;
                                if(i != endDay-4){
                                    bw.append(String.valueOf(average));
                                    bw.append(",");
                                }
                                else{
                                    bw.append(String.valueOf(average));
                                    bw.newLine();
                                }
                            }
                            

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }   
                    else if(task ==2){

                    }
                    else if(task ==3){

                    }
                    else if(task ==4){

                    }
                    }catch(IOException e){
                    e.printStackTrace();
                    }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class myhashMap{
    public HashMap<String , List<String>> myMap(String dataString){

        HashMap<String , List<String>> hm = new HashMap<>();
        return hm;
    }
}