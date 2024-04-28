import parcs.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class MillerRabinHost {

    private static final String TEST_FILE_NAME = "input.txt";
    private static final String TASK_JAR_NAME = "MillerRabinTask.jar";
    private static final String TASK_CLASS_NAME = "MillerRabinTask";
    private static final int WORKERS_AMOUNT = 2;

    public static void main(String[] args) throws Exception {
        task curTask = new task();
        curTask.addJarFile(TASK_JAR_NAME);

        String fileName = curTask.findFile(TEST_FILE_NAME);
        int k = readKFromInput(fileName);
        ArrayList<String> values = readInputData(fileName);

        int numWorkers = WORKERS_AMOUNT;
        int chunkSize = values.size() / numWorkers;

        AMInfo info = new AMInfo(curTask, null);

        point[] points = new point[numWorkers];
        channel[] channels = new channel[numWorkers];

        for (int currentWorkerIndex = 0; currentWorkerIndex < numWorkers; currentWorkerIndex++) {
            points[currentWorkerIndex] = info.createPoint();
            channels[currentWorkerIndex] = points[currentWorkerIndex].createChannel();
            points[currentWorkerIndex].execute(TASK_CLASS_NAME);

            int startIndex = currentWorkerIndex * chunkSize;
            int endIndex = (currentWorkerIndex == numWorkers - 1) ? values.size() - 1 - startIndex : startIndex + chunkSize;
            String data = encodeDataToTask(k, values, startIndex, endIndex);

            channels[currentWorkerIndex].write(data);
        }

        StringBuilder finalResult = new StringBuilder();

        for (int i = 0; i < numWorkers; i++) {
            System.out.println("Waiting for result from worker " + i + "...");
            String result = (String) channels[i].readObject();
            finalResult.append(result);
        }

        System.out.println("Final result: " + finalResult.toString());
        curTask.end();
    }

    private static String encodeDataToTask(
            int k,
            ArrayList<String> values,
            int startIndex,
            int endIndex
    ) {
        StringBuilder result = new StringBuilder();
        result.append(k).append(" ");
        for (int i = startIndex; i < endIndex; i++) {
            result.append(values.get(i)).append(" ");
        }
        return result.toString();
    }

    private static int readKFromInput(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        int k = Integer.parseInt(sc.nextLine());
        sc.close();
        return k;
    }

    private static ArrayList<String> readInputData(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        sc.nextLine();
        ArrayList<String> values = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                break;
            }
            values.add(line);
        }
        sc.close();
        return values;
    }
}
