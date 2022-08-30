package zinsoft.util;

public class Distance {
    
    double lat1; 
    double lon1; 
    double lat2; 
    double lon2;
    
    public Distance(double lat1, double lon1, double lat2, double lon2) {
        // TODO Auto-generated constructor stub
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
    }
    
    double getDistance(double sLat, double sLong, double dLat, double dLong)
    {
        final int radius=6371009;
        double uLat=Math.toRadians(sLat-dLat);
        double uLong=Math.toRadians(sLong-dLong);

        double a = Math.sin(uLat/2) * Math.sin(uLat/2) + 
                Math.cos(Math.toRadians(sLong)) * Math.cos(Math.toRadians(dLong)) *  
                Math.sin(uLong/2) * Math.sin(uLong/2);  

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));  
        double distance = radius * c;

        return distance/1000;
    }
    
    public static double distance(double startPointLon, double startPointLat, double endPointLon, double endPointLat) throws Exception {
        double d2r = Math.PI / 180;
        double dLon = (endPointLon - startPointLon) * d2r;
        double dLat = (endPointLat - startPointLat) * d2r;
        
        double a = Math.pow(Math.sin(dLat / 2.0), 2)
                    + Math.cos(startPointLat * d2r)
                    * Math.cos(endPointLat * d2r)
                    * Math.pow(Math.sin(dLon / 2.0), 2);
        
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * 2;
            
        double distance = c * 6378;
        
        return distance;
    }
    
    public double getDistance() {
        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; // 단위 mile 에서 km 변환.
        dist = dist * 1000.0; // 단위 km 에서 m 로 변환

        return dist;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }


}
