import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import interfaces.NumStream;

public class OptimizedComputationImplementation implements ComputeEngineComputation {

    private ResultTree resultTree = new ResultTree();
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @Override
    public EngineResponse doFactorial(NumStream numStream) {
        if(numStream == null) {
            throw new IllegalArgumentException("NumStream cannot be null");
        }
        ArrayList<Integer> resultList = new ArrayList<>();
        factorialLoop(numStream.getIntegers(), resultList);
        return new EngineResponseImplementation(ResponseCode.SUCCESSFUL, resultList);
    }

    private void factorialLoop(List<Integer> inputList, List<Integer> resultList) {
        if(inputList == null || inputList.isEmpty()) {
            throw new IllegalArgumentException("Input list cannot be null or empty");
        }
        if(resultList == null) {
            throw new IllegalArgumentException("Output list cannot be null");
        }

        List<Future<FactorialResult>> futureList = new ArrayList<Future<FactorialResult>>();

        Integer[] resultArray = new Integer[inputList.size()];

        for (int i = 0; i < inputList.size(); i++) {
            futureList.add(threadPool.submit(new FactorialTask(i,inputList.get(i),resultTree)));
        }

        for (Future<FactorialResult> future : futureList) {
            try {
                FactorialResult result = future.get();
                resultArray[result.getOrder()] = result.getResult();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }

        for (Integer integer : resultArray) {
            resultList.add(integer);
        }
    }
    
    private class FactorialResult{
        private int order;
        private int input;
        private int result;
        public FactorialResult(int order, int inputNum, int result) {
            this.order = order;
            this.input = inputNum;
            this.result = result;
        }
        public int getOrder() {
            return order;
        }
        public void setOrder(int order) {
            this.order = order;
        }
        public int getInput() {
            return input;
        }
        public void setInput(int inputNum) {
            this.input = inputNum;
        }
        public int getResult() {
            return result;
        }
        public void setResult(int result) {
            this.result = result;
        }


    }

    private class FactorialTask implements Callable<FactorialResult>{

        private int order;
        private int input;
        private ResultTree resultTree;

        public FactorialTask(int order, int input, ResultTree tree){
            this.order = order;
            this.input = input;
            this.resultTree = tree;
        }

        @Override
        public FactorialResult call() throws Exception {
            if(input < 0) {
                throw new IllegalArgumentException("Cannot compute the factorial of " + input);
            }

            int result = 1;

            Entry<Integer, Integer> closestPair = resultTree.getClosest(input);

            if (closestPair != null){
                if (closestPair.getKey() == input){
                    result = closestPair.getValue();
                } else {
                    result = closestPair.getValue();
                    if (closestPair.getKey() < input){
                        for (int i = closestPair.getKey()+1; i <= input ; i++) {
                            result *= i;
                        }
                    } else {
                        for (int i = closestPair.getKey(); i > input ; i--) {
                            result /= i;
                        }
                    }
                }
            } else {
                for (int interval = input; interval > 0; interval--) {
                    result = result * interval;
                }
            }
            if (!resultTree.contains(input)){
                resultTree.add(input,result);
            }

            int resultSum = 0;

            while (result != 0) {
                resultSum += result % 10;
                result /= 10;
            }

            return new FactorialResult(order, input, resultSum);
        }

    }

    private class ResultTree {

        private TreeMap<Integer,Integer> previousResults = new TreeMap<>();
        private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private final Lock writeLock = readWriteLock.writeLock();
        private final Lock readLock = readWriteLock.readLock();

        public void add(int key, int value){
            writeLock.lock();
            try {
                previousResults.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }

        public Entry<Integer, Integer> getClosest(int key){
            readLock.lock();
            try {
                Entry<Integer, Integer> floor = previousResults.floorEntry(key);
                Entry<Integer, Integer> ceil = previousResults.ceilingEntry(key);
                if (floor != null && ceil != null){
                    return Math.abs(floor.getKey() - key) < Math.abs(ceil.getKey() - key) ? floor : ceil;
                } else {
                    return floor != null ? floor : ceil;
                }
            } finally {
                readLock.unlock();
            }
        }

        public boolean contains(int key){
            readLock.lock();
            try {
                return previousResults.containsKey(key);
            } finally {
                readLock.unlock();
            }
        }

    }
}
