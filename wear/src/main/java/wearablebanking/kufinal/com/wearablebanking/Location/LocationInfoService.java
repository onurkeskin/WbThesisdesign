package wearablebanking.kufinal.com.wearablebanking.Location;

/**
 * Created by PC on 10.11.2015.
 */
public class LocationInfoService {

    private static LocationInfoService instance;

    private LocationInfoService() {

    }

    public static LocationInfoService getInstance(){
        if(instance == null) instance = new LocationInfoService();

        return instance;
    }

    public GeneralLocationRequestService parseLocation(double[] latLang)
    {
        if(latLang.length < 2) return null;

        GeneralLocationRequestService toRet = new GeneralLocationRequestService(5);

        return toRet;
    }

}
