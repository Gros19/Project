package combiner;

import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


@Log4j
public class IPCount2TimeCheck2 extends Configuration implements Tool {
    /*맵리듀스 실행합수*/
    public static void main(String[] args) throws Exception{
        /*파라미터는 분석할 파일(폴더)와 분석 결과가 저장될 파일(폴더)로 2개를 받음*/
        if(args.length != 2){
            log.info("분석할 폴더(파일) 및 분석결과가 저장될 폴더를 입력해야 합니다.");
            System.exit(-1);
        }

        /*컴바이너가 적용된 맵리듀스 실행 전 시간*/
        long startTime = System.nanoTime();
        int exitCode = ToolRunner.run(new IPCount2TimeCheck2(), args);

        /*컴바이너가 적용된 맵리듀스 실행 완료 시간*/
        long endTime = System.nanoTime();
        log.info("##########2 TIME: " + (endTime - startTime) + "ns");
        System.exit(exitCode);
    }
    @Override
    public void setConf(Configuration configuration) {
        /*App 이름 정의*/
        configuration.set("AppName", "Combiner Test!!!!!!");

    }
    @Override
    public Configuration getConf() {

        /*맵리듀스 전체에 적용될 변수를 정의할 때 사용*/
        Configuration conf = new Configuration();

        /*변수 정의*/
        this.setConf(conf);

        return conf;
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = this.getConf();
        String appName = conf.get("AppName");

        log.info("appName: " + appName);

        /*잡 객체
         * 하둡이 실행되면, 기본적으로 잡 객체를 메모리에 올림
         * */
        Job job = Job.getInstance(conf);

        /*맵리듀스 잡이 시작되는 main합수가 존재하는 파일 설정*/
        job.setJarByClass(IPCount2.class);

        /*맵리듀스 잡 이름 설정, 리소스 매니저 등 맵리듀스 실행 결과 및 로그 확인할 때 편리*/
        job.setJobName("Combiner IP Count2");

        /*분석할 폴더(파일) -- 첫번째 파라미터*/
        FileInputFormat.setInputPaths(job, new Path(strings[0]));

        /*분석 결과가 저장되는 폴더(파일) -- 두 번째 파라미터*/
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        /*맵리듀스의 맵 역할을 수행하는 Mapper 자바 파일 설정*/
        job.setMapperClass(IPCount2Mapper.class);

        /*맵리듀스의 리듀스 역할을 수행하는 Reducer 자바 파일 설정*/
        job.setReducerClass(IPCount2Reducer.class);


        /*분석 결과가 저장될 때 사용될 키의 데이터 타입*/
        job.setOutputKeyClass(Text.class);

        /*분석결과가 저장될 때 사용될 값의 데이터 타입*/
        job.setOutputValueClass(IntWritable.class);

        /*맵리듀스 실행*/
        boolean success = job.waitForCompletion(true);


        return (success ? 0 : 1);
    }





}