package combiner;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class IPCount2Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {

        /*분석할 파일의 한 줄*/
        String line = value.toString();

        /*로그로부터 추출된 ip*/
        String ip = "";

        /*반복횟수*/
        int forCnt = 0;

        for(String field : line.split("\\W+")){
            if(field.length() > 0){
                /*반복횟수 증가*/
                forCnt ++;
                ip += (field + ".");
                if(forCnt == 4){
                    ip = ip.substring(0, ip.length()-1);

                    context.write(new Text(ip), new IntWritable(1));
                }

            }
        }
    }
}
