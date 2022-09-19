package ip;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;



/*
* 맵 역할을 수행하기 위해서는 Mapper 자바 파일을 상속받아야합
*
*
* */
public class IPCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {


    /*
    * 부모 Mapper 자바 파일에 작성된 map 함수를 덮어쓰기 수행
    *
    * */
    @Override  // 키는 Long 값은 Text
    protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {


        //분석할 한줄한줄한줄....
        String line = value.toString();

        //로그부터 추출된 IP
        String ip = "";

        //반복 횟수
        int forCnt = 0;


        //\W+ 공백단위로 잘라
        for(String word : line.split("\\W+")){
            //공백단위로 나눠진
            //word가 ""가 아니라면
            if(word.length() > 0){
                //반복 횟수 증가
                forCnt++;
                //IP값 저장함
                ip += (word + ".");

                // IP는 4옥탯
                if(forCnt == 4){
                    //ip변수 값은 192.168.0.127. 와 같이 마지막에도 .붙음
                    //마지막 .을 제거하기 위해 0부터 마지막 위치까지 -1 값까지 문자열을 짜름
                    ip = ip.substring(0, ip.length()-1);

                    //shuffle and sort로 데이터 전달
                    //하둡에서 쓰는 문자 Text()로 변경
                    //하둡에서 쓰는 숫자 IntWritable()로 변경
                    // (192.168.0.127 : 1) 이런 식으로
                    context.write(new Text(ip), new IntWritable(1));

                }
            }
        }
    }
}
