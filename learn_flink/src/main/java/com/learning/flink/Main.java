package com.learning.flink;


import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;

public class Main {
    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);

        env.getConfig().setGlobalJobParameters(params);
        DataSet<String> text = env.readTextFile("/Users/devlogex/Projects/Learning/learn_flink/input.txt");
        DataSet<String> filtered = text.filter(new FilterFunction<String>() {
            public boolean filter(String value) {
                return value.startsWith("n");
            }
        });
        DataSet<Tuple2<String, Integer>> tokenized = filtered.map(new Tokenizer());
        DataSet<Tuple2<String, Integer>> counts = tokenized.groupBy(0).sum(1);

        if(params.has("output")) {
            counts.writeAsCsv("/Users/devlogex/Projects/Learning/learn_flink/output.csv");
            env.execute("WordCount Example");
        }
    }
    public static final class Tokenizer implements MapFunction<String, Tuple2<String, Integer>> {

        @Override
        public Tuple2<String, Integer> map(String s) throws Exception {
            return new Tuple2<String, Integer>(s, 1);
        }
    }
}
