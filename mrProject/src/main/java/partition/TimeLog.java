package partition;

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
public class TimeLog extends Configuration implements Tool {
    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            log.info("분석결과가 저장될 폴더를 입력해야 합니다.");
            System.exit(-1);
        }


        int exitCode = ToolRunner.run(new TimeLog(), args);

        System.exit(exitCode);

    }
    @Override
    public int run(String[] strings) throws Exception {

        Configuration conf = this.getConf();
        String appName = conf.get("AppName");
        log.info("appName" + appName);

        Job job = Job.getInstance(conf);

        /*호출이 발생하면, 메모리에 저장하여 캐시 처리 수행
        * 하둡분산파일시스템에 저장된 파일만 가능함*/
        job.addCacheFile(new Path("/access_log").toUri());

        job.setJarByClass(TimeLog.class);

        job.setJobName(appName);


        FileInputFormat.setInputPaths(job, new Path(strings[0]));

        FileOutputFormat.setOutputPath(job, new Path(strings[1]));



        job.setMapperClass(TimeLogMapper.class);

        job.setReducerClass(TimeLogReducer.class);

        /*리듀스를 분산해서 실행하기 위해 사용되는 파티셔너 객체 설정*/
        job.setPartitionerClass(TimeLogPartitioner.class);

        /*1일 24시간으로 리듀스 24를 생성하여 리듀스 1개당 1시간씩 처리
        * 예를 들어 0시느 0번 리듀스, 23시는 23번 리듀스가 데이터를 처리함*/
        job.setNumReduceTasks(24);

        /*Mapper -> Shuffle and Sort로 전달하는 키 데이터 타입*/
        job.setMapOutputKeyClass(Text.class);
        /*Mapper -> Shuffle and Sort로 전달하는 값 데이터 타입*/
        job.setMapOutputValueClass(Text.class);

        /*분석 결과가 저장될 때 사용될 키의 데이터 타입*/
        job.setOutputKeyClass(Text.class);
        /*분석 결과가 저장될 때 사용될 값의 데이터 타입*/
        job.setOutputValueClass(IntWritable.class);

        boolean success = job.waitForCompletion(true);
        return (success ? 0:1);
    }

    @Override
    public void setConf(Configuration configuration) {

        configuration.set("AppName", "### Partitioner Time Test");

    }

    @Override
    public Configuration getConf() {
        Configuration conf = new Configuration();
        this.setConf(conf);
        return conf;
    }
}