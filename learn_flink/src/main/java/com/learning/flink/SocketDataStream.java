package com.learning.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SocketDataStream {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        env.getConfig().setGlobalJobParameters(params);
        DataStream<String> text = env.socketTextStream("localhost", 9999);
        DataStream<Tuple2<String, Integer>> counts = text.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                return s.startsWith("N");
            }
        }).map(new Main.Tokenizer()).keyBy(0).sum(1);
        counts.print();
        env.execute("Streaming WordCount");
    }
}
