package success;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ResultCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {




    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //IP별 빈도수를 계산하기 위한 변수
        int resultCodeCount = 0;

        //Shuffle and Sort로 인해 단어별로 데이터들으 ㅣ값들이 List 구조로 저장됨
        //200 : {1, 1, 1, 1, 1, 1}
        //모든 값은 1이이게 모두 더하기 해도 됨
        for(IntWritable value : values){
            //값을 모두 더하기
            resultCodeCount += value.get();
        }
        //분석결과 파일에 데이터 저장하기
        context.write(key, new IntWritable(resultCodeCount));

    }

}
