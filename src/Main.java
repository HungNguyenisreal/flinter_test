import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputFile=null;
        String outputDir=".";

        for(int i=0;i<args.length;i++){
            if("--input".equals(args[i])) inputFile=args[++i];
            if("--output".equals(args[i])) outputDir=args[++i];
        }

        if(inputFile==null){
            System.out.println("Usage: java Main --input ad_data.csv --output results/");
            return;
        }

        long start=System.currentTimeMillis();

        Map<String,CampaignStats> map=new HashMap<>();

        try(BufferedReader br= Files.newBufferedReader(Paths.get(inputFile))){
            String line=br.readLine();//skip header

            while((line=br.readLine())!=null){
                String[] parts=line.split(",");

                String campaignId=parts[0];
                long impressions=Long.parseLong(parts[2]);
                long clicks=Long.parseLong(parts[3]);
                double spend=Double.parseDouble(parts[4]);
                long conversions=Long.parseLong(parts[5]);

                CampaignStats stats=map.computeIfAbsent(campaignId,k->new CampaignStats());
                stats.accumulate(impressions,clicks,spend,conversions);
            }
        }

        List<Map.Entry<String,CampaignStats>> entries=new ArrayList<>(map.entrySet());

        Comparator<Map.Entry<String,CampaignStats>> ctrComparator=Comparator.comparingDouble(
                e->e.getValue().getCTR()
        );

        Comparator<Map.Entry<String,CampaignStats>> cpaComparator=Comparator.comparingDouble(
                e->{
                    Double cpa=e.getValue().getCPA();
                    return cpa==null?Double.MAX_VALUE:cpa;
                }
        );

        PriorityQueue<Map.Entry<String,CampaignStats>> topCTR=new PriorityQueue<>(ctrComparator);
        PriorityQueue<Map.Entry<String,CampaignStats>> topCPA=new PriorityQueue<>(cpaComparator.reversed());

        for(Map.Entry<String,CampaignStats> e:entries){
            //Top CTR
            topCTR.offer(e);
            if(topCTR.size()>10) topCTR.poll();

            //Top CPA (exclude zero conversions)
            if(e.getValue().getCPA()!=null){
                topCPA.offer(e);
                if(topCPA.size()>10) topCPA.poll();
            }
        }

        writeCSV(topCTR,outputDir+"/top10_ctr.csv");
        writeCSV(topCPA, outputDir + "/top10_cpa.csv");

    }

    //xuat file csv
    private static void writeCSV(PriorityQueue<Map.Entry<String,CampaignStats>> pq,String file)
            throws Exception{
        List<Map.Entry<String,CampaignStats>> list=new ArrayList<>(pq);
        list.sort((a,b)->Double.compare(
                b.getValue().getCTR(),
                a.getValue().getCTR()
        ));

        try(PrintWriter pw=new PrintWriter(new FileWriter(file))){
            pw.println("campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA");

            for(Map.Entry<String,CampaignStats> e:list){

                CampaignStats s=e.getValue();
                Double cpa=s.getCPA();

                pw.printf("%s,%d,%d,%.2f,%d,%.4f,%s%n",
                        e.getKey(),s.impressions,s.clicks,s.spend,s.conversions,s.getCTR(),
                        cpa==null?"null":String.format("%.2f",cpa));
            }
        }
    }
}