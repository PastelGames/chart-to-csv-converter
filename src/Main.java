import java.io.*;
import java.util.ArrayList;
import org.json.simple.*;
import org.apache.commons.csv.*;

public class Main {

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\Downloads\\Mii.chart");

        BufferedReader br = new BufferedReader(new FileReader(file));

        float resolution = 0;
        float BPM = 0;
        ArrayList<Float> track1 = new ArrayList<>();
        ArrayList<Float> track2 = new ArrayList<>();
        ArrayList<Float> track3 = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {

            line = line.trim();

            if (line.startsWith("Resolution")) {
                String[] split = line.split(" ");
                resolution = Float.parseFloat(split[2].trim());
            }

            if (line.startsWith("0 = B")) {
                String[] split = line.split(" ");
                BPM = Float.parseFloat(split[3].trim());
                BPM = BPM / 1000;
            }

            if (line.matches("\\d* = N.*")) {
                String[] split = line.split(" ");
                float time;
                int track;
                time = Float.parseFloat(split[0].trim());
                track = Integer.parseInt(split[3].trim());

                if (track == 0) {
                    track1.add(time/resolution);
                } else if (track == 1) {
                    track2.add(time/resolution);
                } else if (track == 2) {
                    track3.add(time/resolution);
                }
            }

        }

        System.out.println(resolution);
        System.out.println(BPM);
        for(float pos: track1) {
            System.out.println(pos);
        }
        System.out.println("-------------------------------------");
        for(float pos: track2) {
            System.out.println(pos);
        }
        System.out.println("-------------------------------------");
        for(float pos: track3) {
            System.out.println(pos);
        }




        br.close();

        try (FileWriter fileWriter = new FileWriter("SongTrackData.csv");

        CSVPrinter csvp = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Name", "Song BPM", "Track 1 Note Positions In Beats", "Track 2 Note Positions In Beats", "Track 3 Note Positions In Beats"))

        ) {

            csvp.printRecord("Track", String.valueOf(BPM), specialArrayPrint(track1),  specialArrayPrint(track2), specialArrayPrint(track3));

            csvp.flush();

        }

    }

    private static String specialArrayPrint(ArrayList<Float> array) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i < array.size(); i++) {
            sb.append(array.get(i));
            if (i < array.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

}