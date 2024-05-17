import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
@SuppressWarnings("unused")
public class Regex{

    //判斷是否迴文
    static void Palindrome(String str){ 
        int a = str.length();
        str = str.toLowerCase();
        char[] arr = str.toCharArray();
        int count =0;
        boolean check = true;
        if(a>=1){
            if((a&1) ==1){
                for(int i=0 ; i<((a-1)/2) ; i++){
                    if(arr[i] != arr[a-i-1]){
                        
                        check = false;
                        break;
                    }
                }
            }
    
            else{
                for(int i=0 ; i< a/2 ; i++){
                    if(arr[i] != arr[a-i-1]){
                        check = false;
                        break;
                    }
                }
            }
        }
        else check =true;
        if(check==false) System.out.print("N,");
        else System.out.print("Y,");
    }

    //判斷是否有特定字串
    static void SpecificString(String str , String subString){
        int num1 = str.length();
        int num2 = subString.length();
        boolean check = false; 

        for(int i=0 ; i <=num1-num2 ; i++){
            if(str.charAt(i) == subString.charAt(0)){
                for(int j=0; j<num2 ; j++){
                    if(str.charAt(i+j) != subString.charAt(j)){
                        check = false;
                        break;
                    }
                    else check = true;
                }

            }
            if(check == true) break;
        }

        if(check == false) System.out.print("N,");
        else System.out.print("Y,");
        
    } 

    //判斷特定字串是否超過規定數量
    static void countSpecificString(String str , String subString , int a){
        int count =0;
        int num1 = str.length();
        int num2 = subString.length();
        boolean check = false;

        for(int i =0 ; i <= num1-num2 ; i++){
            if(str.charAt(i) == subString.charAt(0)){
                for(int j =0 ; j < num2 ; j++){
                    if(str.charAt(i+j) != subString.charAt(j)){
                        check = false;
                        break;
                    }
                    check =true;
                }
                if(check == true){
                    count +=1;
                }
            }
        }
        if(count >= a) System.out.print("Y,");
        else System.out.print("N,");
    }
    
    //數數囉
    static void countAandB(String str){
        String a= "a";
        String bb = "bb";
        boolean check = false;
        boolean ax =false , bx = false;
        int num1 = str.length() , num2=a.length() , num3=bb.length();
        int counta =0, countb=0;

        for(int i=0 ; i < (num1-num2) ; i++){
            if(str.charAt(i) == 'a'){
                counta = i;
                ax = true;
                break;
            }
        }

        for(int i=(num1-num2-1) ; i<num1-1 ; i++){
            if(str.charAt(i) == 'b' && str.charAt(i-1) == 'b'){
                bx = true;
                countb = i-1;
                break;

            }
        }
        if(ax == true && bx == true) check = true;

        if(check ==true && counta<=countb) System.out.println("Y");
        else System.out.println("N");

    }

    public static void main(String[] args) throws IOException{
        String str1 = args[1].toLowerCase();
        String str2 = args[2].toLowerCase();
        int s2Count = Integer.parseInt(args[3]);

        try {
            BufferedReader buf = new BufferedReader(new FileReader(args[0]));
            String str=new String();
            while ((str = buf.readLine()) != null) {
                str = str.toLowerCase();
                Palindrome(str);
                SpecificString(str, str1);
                countSpecificString(str, str2, s2Count);
                countAandB(str);
            }
            buf.close();

        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}