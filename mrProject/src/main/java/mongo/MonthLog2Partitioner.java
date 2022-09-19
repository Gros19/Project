package mongo;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;
import java.util.Map;

public class MonthLog2Partitioner extends Partitioner<Text, Text> {
    /*월과 리듀서를 매칭하기 위한 객체
    * 1월은 0번 리듀스, 2월은 1번 리듀스...*/
    Map<String, Integer> months = new HashMap<>();

    /*생성자에 매칭 정보 저장*/
    public MonthLog2Partitioner(){
        this.months.put("Jan", 0);
        this.months.put("Feb", 1);
        this.months.put("Mar", 2);
        this.months.put("Apr", 3);
        this.months.put("May", 4);
        this.months.put("Jun", 5);
        this.months.put("Jul", 6);
        this.months.put("Aug", 7);
        this.months.put("Sep", 8);
        this.months.put("Oct", 9);
        this.months.put("Nov", 10);
        this.months.put("Dec", 11);
    }

    /** Partitioner 객체에 정의된 함수를 오버라이드
     *
     * @param key               맵에서 Shuffle and Sort로 전달한 키(IP)
     * @param value
     * @param numReduceTasks
     * */


    @Override
    public int getPartition(Text key, Text value, int numReduceTasks) {
        /*실행될 리듀스 번호를 월에 따라 매핑*/
        return months.get(key.toString());
    }
}
