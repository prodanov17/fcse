package labs7.labs7_4;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency{
    Map<String, Integer> wordOccurences = new TreeMap<>();
    public TermFrequency(InputStream inputStream, String[] stopWords){
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().trim().split("\\s+");
            Arrays
                    .stream(line)
                    .map(x -> x.replaceAll("[,.]+$", ""))
                    .map(String::toLowerCase)
                    .filter(x -> !x.isEmpty())
                    .filter(e-> !Arrays.asList(stopWords).contains(e))
                    .forEach(x->wordOccurences.put(x, wordOccurences.getOrDefault(x, 0) + 1));
        }
    }

    public int countTotal(){
        return wordOccurences.values().stream().mapToInt(x->x).sum();
    }
    public int countDistinct(){
        return wordOccurences.size();
    }

    public List<String> mostOften(int k){
        return wordOccurences
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
