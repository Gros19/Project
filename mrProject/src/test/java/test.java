import javafx.scene.Parent;
import org.apache.hadoop.io.IntWritable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
abstract class Vehicle{
    String name;
    abstract public String getName(String val);
    public String getName(){
        return "Vehicle name : " + name;
    }
}
class Car extends Vehicle{
    private String name;
    public Car(String val){
        name = super.name = val;
    }
    public String getName(String val){
        return "Car name : " + val;
    }
    public String getName(byte[] val){
        return "Car name : " + val;
    }
}

public class test {
    public static void main(String[] args){
        Vehicle obj = new Car("Spa");
        System.out.println(obj.getName());



//        String in = "10.223.157.186 - - [15/Jul/2009:14:58:59 -0700] \"GET / HTTP/1.1\" 403 201\n10.223.157.186 - - [15/Jul/2009:14:58:59 -0700] \"GET / HTTP/1.1\" 403 201\n10.223.157.186 - - [15/Jul/2009:14:58:59 -0700] \"GET / HTTP/1.1\" 403 201";
//
//        Pattern pattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
//
//        for(String line : in.split("\n")){
//            Matcher matcher = pattern.matcher(line);
//            if(matcher.find()){
//                System.out.println(matcher.group());
//            }
//        }





    }
}
