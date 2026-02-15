
public class CampaignStats {
    long impressions;
    long clicks;
    double spend;
    long conversions;

    public void accumulate(long impressions,long clicks,double spend,long conversions){
        this.impressions+=impressions;
        this.clicks+=clicks;
        this.spend+=spend;
        this.conversions+=conversions;
    }

    public double getCTR(){
        if(impressions==0) return 0.0;
        return (double) clicks/impressions;
    }

    public Double getCPA(){
        if(conversions==0) return null;
        return spend/conversions;
    }

}
