package noreduce;

import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

@Log4j
public class WordCount4 extends Configuration implements Tool {
    /*맵리듀스 실행 함수*/
    public static void main(String[] strings) throws Exception{
        if (strings.length != 1){
            log.info("분석결과가 저장될 폴더를 입력해야 합니다.");
            System.exit(-1);
        }

        int exitCode = ToolRunner.run(new WordCount4(), strings);
        System.exit(exitCode);
    }


    @Override
    public int run(String[] strings) throws Exception {
        /*캐시 메모리에 올릴 분석 파일*/
        String analysisFile = "/comedies";

        Configuration conf = this.getConf();
        String appName = conf.get("AppName");

        System.out.println("appName : " + appName);


        /*맵리듀스 실행을 위한 잡 객체 가져오기
         * 하둡이 실행되면, 기본적으로 잡객체를 메모리에 올림
         * */
        Job job = Job.getInstance(conf);

        /*호출이 발생하면, 메모리에 저장하여 캐시 처리 수행
         * 하둡분산파일시스템에 저장된 파일만 가능함*/
        job.addCacheFile(new Path(analysisFile).toUri());

        /*맵리듀스 잡이 시작되는 main함수 설정*/
        job.setJarByClass(WordCount4.class);

        /*맵리듀스 잡 이름 설정, 리소스 매니저 등 맵리듀스 실행 결과 및 로그 확인할 때 편리함*/
        job.setJobName(appName);

        /*분석할 파일(폴더) -- 첫번째 파라미터*/
        FileInputFormat.setInputPaths(job, new Path(analysisFile));

        /*분석 결과가 저장될 파일(폴더) --두번쨰 파라미터*/
        FileOutputFormat.setOutputPath(job, new Path(strings[0]));

        /*맵리듀스의 맵 역할을 수행하는 Mapper 자바 파일 설정*/
        job.setMapperClass(WordCount4Mapper.class);

        /*
        *맵리듀스 역할을 수행하는 Reducer 자바 파일 설정
        * 키 값에 대한 합계를 계산하는 Reducer 객체는 사용 빈도가 톺아 기본 제공함
        * 합계 값이 작은 경우 : IntSumReducer 사용
        * 합계가 값이 큰 경우 : LongSumReducer 사용*/
        job.setReducerClass(LongSumReducer.class);

        /*분석결과가 저장될 때 사용될 값의 데이터 타입*/
        job.setOutputKeyClass(Text.class);

        /*분석 결과가 저장될 때 사용될 값의 데이터 타입*/
        job.setOutputValueClass(LongWritable.class);

        /*맵리듀스 실행*/
        boolean success = job.waitForCompletion(true);

        return (success ? 0: 1);
    }

    @Override
    public void setConf(Configuration configuration) {
        /*앱이름 정의*/
        configuration.set("AppName", "No Reduce Test#######");
    }

    @Override
    public Configuration getConf() {
        /*맵리듀스 전체에 적용될 변수를 정의할 때 사용*/
        Configuration conf = new Configuration();

        /*변수 정의*/
        this.setConf(conf);
        return conf;
    }
}

/*

package noreduce;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.Tool;

@Slf4j
public class WordCount4 extends Configuration implements Tool{
    //맵리듀스 실행 함수
    public static void main(String[] args)throws Exception {
        // 피라미터는 분석할 파일(폴더)과 분석 결과가 저장될 파일(폴더)로 2개를 받음
        if (args.length != 1) {
            log.info("분석할 파일(폴더)과 분석 결과가 저장될 (폴더)를 입력해야 합니다.");
            System.exit(-1);
        }

        int exitCode = ToolRunner.run(new WordCount4(), args);

        System.exit(exitCode);
    }
    @Override
    public void setConf(Configuration configuration) {

        configuration.set("AppName", "No Reduce Test");
    }
    @Override
    public Configuration getConf() {
        //맵리듀스 전체에 적용될 변수를 정의할때 사용
        Configuration conf = new Configuration();
        //변수 정의
        this.setConf(conf);

        return conf;
    }
    @Override
    public int run(String[] args) throws Exception{
        //캐시 메모리에 올릴 분석 파일
        String anlysisFile = "/comedies";

        Configuration conf = this.getConf();
        String appName = conf.get("AppName");

        log.info("appName : " + appName);

        //맵리듀스 실핼을 위한 잡 객체를 가져오기
        //하둡이 실행되면, 기본적으러 잡 객체를 메모리에 올림
        Job job = Job.getInstance(conf);

        //맵리듀스 잡이 시작되는 main함수가 존재하는 파일 설정
        job.setJarByClass(WordCount4.class);

        //맵리듀스 잡 이름 설정, 리소스 매니저 등 맵리듀스 실행 결과 및 로그 확인할 때 편리함
        job.setJobName(appName);

        //호출이 발생하면, 메모리에 저장하여 캐시 처리 수행
        //하둡분산파일시스템에 저장된 파일만 가능함
        // /comedies 파일을 메모리에 올리기
        job.addCacheFile(new Path(anlysisFile).toUri());

        //분석할 폴더(파일) -- 첫번째 파라미터
        FileInputFormat.setInputPaths(job, new Path(anlysisFile));

        //분석 결과가 저장되는 폴더(파일) -- 두번째 파라미터
        FileOutputFormat.setOutputPath(job, new Path(args[0]));

        //맵리듀스의 맵 역활을 수행하는 Mapper 자바 파일 설정
        job.setMapperClass(WordCount4Mapper.class);
        //맵리듀스의 리듀스 역활을 수행하는 Reducer 자바 파일설정
        //키 값에 대한 합계를 계산하는 Reducer 객체는 사용 빈도가 높아 기본 제공함
        // 합계가 값이 작은 경우 : IntSumReducer 사용
        // 합계가 값이 큰 경우 : LingSumReducer 사용
        job.setReducerClass(LongSumReducer.class);

        //분석 결과가 저장될 때 사용될 키의 데이터 타입
        job.setOutputKeyClass(Text.class);

        //분석 결과가 저장될때 사용될 값의 데이터 타입
        job.setOutputValueClass(LongWritable.class);

        //맵리듀스 실행
        boolean success = job.waitForCompletion(true);
        return (success ? 0 : 1);

    }
}
 */
